package cn.nilcode.server.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.nilcode.server.entity.*;
import cn.nilcode.server.service.*;
import cn.nilcode.server.util.Result;
import cn.nilcode.server.util.ResultCode;
import cn.nilcode.server.vo.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * <p>
 * 博客表 前端控制器
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@RestController
@RequestMapping("/server/blog")
public class BlogController {

    @Autowired
    private IBlogService iBlogService;

    @Autowired
    private ITagsService iTagsService;

    @Autowired
    private IBlogTagsService iBlogTagsService;

    @Autowired
    private IClassificationsService iClassificationsService;

    @Autowired
    private IBlogClassificationsService iBlogClassificationsService;

    @Autowired
    private IArchiveService iArchiveService;

    @Autowired
    private IBlogArchiveService iBlogArchiveService;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;


    @PostMapping("/saveOrUpdate")
    public String addOrSaveBlog(BlogVo blogVo)  {

        Blog one = iBlogService.getOne(Wrappers.<Blog>lambdaQuery().eq(Blog::getTitle, blogVo.getTitle()));
        // 更新
        if (ObjectUtil.isNotNull(one)) {
            one.setContent(blogVo.getContent()).setTitle(blogVo.getTitle()).setCoverImg(blogVo.getCoverImg()).setDraft(blogVo.getDraft()).setPublishedDate(DateUtil.parseDate(blogVo.getPublishedDate()).toSqlDate().toLocalDate());
        } else {
            // 新增
            one = new Blog().setTitle(blogVo.getTitle()).setCoverImg(blogVo.getCoverImg()).setContent(blogVo.getContent()).setDraft(blogVo.getDraft()).setPublishedDate(DateUtil.parseDate(blogVo.getPublishedDate()).toSqlDate().toLocalDate());
        }
        TransactionStatus transactionStatus = null;
        try {
            // 开启事务
            transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            iBlogService.saveOrUpdate(one);

            // 创建归档
            int year = DateUtil.parse(blogVo.getPublishedDate()).year();
            Archive archive = iArchiveService.getOne(Wrappers.<Archive>lambdaQuery().eq(Archive::getDate, year));

            if (ObjectUtil.isNull(archive)) {
                archive = new Archive().setDate(year);
                iArchiveService.save(archive);
            }

            //

            Map<SFunction<BlogArchive, ?>, Object> filter = new HashMap<>(2);

            filter.put(BlogArchive::getArchiveId, archive.getArchiveId());
            filter.put(BlogArchive::getBlogId, one.getBlogId());
            BlogArchive one2 = iBlogArchiveService.getOne(Wrappers.<BlogArchive>lambdaQuery().allEq(filter));
            // 不存在归档
            if (ObjectUtil.isNull(one2)) {
                one2 = new BlogArchive().setArchiveId(archive.getArchiveId()).setBlogId(one.getBlogId());
            }

            iBlogArchiveService.saveOrUpdate(one2);

            // 分类
            List<BlogClassifications> list = iBlogClassificationsService.list(Wrappers.<BlogClassifications>lambdaQuery().eq(BlogClassifications::getBlogId, one.getBlogId()));
            if (CollUtil.isNotEmpty(list)) {
                list.forEach(blogClassifications -> {
                    Classifications byId = iClassificationsService.getById(blogClassifications.getClassId());
                    byId.setRelativeCount(byId.getRelativeCount() - 1);
                    iClassificationsService.updateById(byId);
                    iBlogClassificationsService.removeById(blogClassifications.getBlogClassId());
                });
            }
            String[] split = blogVo.getClassifications().substring(0, blogVo.getClassifications().length() - 1).split("/");
            int length = split.length;
            if (length >= 1) {
                Classifications root = iClassificationsService.getOne(Wrappers.<Classifications>lambdaQuery().eq(Classifications::getClassName, split[0]));
                root.setRelativeCount(root.getRelativeCount() + 1);
                iClassificationsService.updateById(root);
                iBlogClassificationsService.save(new BlogClassifications().setBlogId(one.getBlogId()).setClassId(root.getClassId()));
                if (length >= 2) {
                    Classifications second = iClassificationsService.getOne(Wrappers.<Classifications>lambdaQuery().eq(Classifications::getParentClassId, root.getClassId()));
                    second.setRelativeCount(second.getRelativeCount() + 1);
                    iClassificationsService.updateById(second);
                    iBlogClassificationsService.save(new BlogClassifications().setBlogId(one.getBlogId()).setClassId(second.getClassId()));
                    if (length == 3) {
                        Classifications last = iClassificationsService.getOne(Wrappers.<Classifications>lambdaQuery().eq(Classifications::getParentClassId, second.getClassId()));
                        last.setRelativeCount(last.getRelativeCount() + 1);
                        iClassificationsService.updateById(last);
                        iBlogClassificationsService.save(new BlogClassifications().setBlogId(one.getBlogId()).setClassId(last.getClassId()));
                    }
                }
            }


            // 标签
            String[] tags = blogVo.getTags().split(",");

            // 移除已存在的博客-标签
            List<BlogTags> list1 = iBlogTagsService.list(Wrappers.<BlogTags>lambdaQuery().eq(BlogTags::getBlogId, one.getBlogId()));
            if (CollUtil.isNotEmpty(list1)) {
                list1.forEach(blogTags -> {
                    Tags one1 = iTagsService.getOne(Wrappers.<Tags>lambdaQuery().eq(Tags::getTagId, blogTags.getTagId()));
                    iTagsService.updateById(one1.setRelativeCount(one1.getRelativeCount() - 1));
                    iBlogTagsService.removeById(blogTags);
                });
            }

            for (String tag : tags) {

                if (tag == null || "".equals(tag)) {
                    continue;
                }
                tag = tag.trim();
                // 查询是否存在
                Tags tag1 = iTagsService.getOne(Wrappers.<Tags>lambdaQuery().eq(Tags::getTagName, tag));

                if (ObjectUtil.isNotNull(tag1)) {
                    iTagsService.updateById(tag1.setRelativeCount(tag1.getRelativeCount() + 1));
                } else {
                    tag1 = new Tags().setTagName(tag).setRelativeCount(1);
                    iTagsService.save(tag1);
                }

                iBlogTagsService.save(new BlogTags().setBlogId(one.getBlogId()).setTagId(tag1.getTagId()));
            }
            // 同时成功 -> 提交事务
            dataSourceTransactionManager.commit(transactionStatus);

        } catch (Exception e) {
            assert transactionStatus != null;
            dataSourceTransactionManager.rollback(transactionStatus);
            e.printStackTrace();
            return Result.build(ResultCode.SERVER_ERROR);
        }

        return Result.build(ResultCode.BLOG_SAVE_OR_UPDATE_SUCCESS);


    }

    @GetMapping("/getBlogVo/{blogId}")
    public String getBlogVoById(@PathVariable String blogId) {
        if (StrUtil.isNotEmpty(blogId)) {
            Blog byId = iBlogService.getById(blogId);
            BlogVo blogVo = new BlogVo(blogId, byId.getCoverImg(),
                    byId.getTitle(), byId.getContent(), ArrayUtil.toString(getAllClassifications(blogId)), ArrayUtil.toString(getAllTags(blogId)),
                    byId.getClickCount(), byId.getPublishedDate().toString(), byId.getDraft());
            return Result.build(ResultCode.QUERY_SUCCESS, blogVo);
        }

        return Result.build(ResultCode.SERVER_ERROR);
    }

    @GetMapping("/byId/{blogId}")
    public String getById(@PathVariable String blogId) {
        Blog byId = iBlogService.getById(blogId);

        if (ObjectUtil.isNotNull(byId)) {
            iBlogService.updateById(byId.setClickCount(byId.getClickCount() + 1));
            return Result.build(ResultCode.QUERY_SUCCESS,
                    new DetailBlogVo().setBlogTitle(byId.getTitle())
                            .setPublishedDate(byId.getPublishedDate().toString())
                            .setClassifications(getAllClassifications(blogId))
                            .setContent(byId.getContent())
                            .setClickCount(byId.getClickCount())
                            .setTags(getAllTags(blogId)));
        }
        return Result.build(ResultCode.BLOG_NOT_EXISTED_ERROR);
    }

    @GetMapping("/check/{title}")
    public String checkTitle(@PathVariable String title) {
        if (ObjectUtil.isNotNull(iBlogService.getOne(Wrappers.<Blog>lambdaQuery().eq(Blog::getTitle, title)))) {
            return Result.build(ResultCode.TITLE_AVAILABLE_SUCCESS);
        } else {
            return Result.build(ResultCode.TITLE_ALREADY_EXISTED);
        }
    }

    @GetMapping("/total")
    public String getTotal() {
        return Result.build(ResultCode.QUERY_SUCCESS, "" + iBlogService.count(Wrappers.<Blog>lambdaQuery().eq(Blog::getDraft, false)));
    }

    @GetMapping("/top")
    public String getTop() {
        return Result.build(ResultCode.QUERY_SUCCESS, "" + iBlogService.count());
    }

    @GetMapping("/index/{page}")
    public String showIndexByPage(@PathVariable Integer page) {
        List<IndexBlogVo> vos = new ArrayList<>();
        iBlogService.page(new Page<>(page, 12), Wrappers.<Blog>lambdaQuery().eq(Blog::getDraft, false)).getRecords().forEach(blog -> {
            vos.add(new IndexBlogVo().setBlogId(blog.getBlogId())
                    .setPublishedDate(blog.getPublishedDate().toString())
                    .setCoverImg(blog.getCoverImg())
                    .setClassifications(getAllClassifications(blog.getBlogId()))
                    .setTags(getAllTags(blog.getBlogId()))
                    .setBlogTitle(blog.getTitle()));
        });

        if (CollUtil.isNotEmpty(vos)) {
            return Result.build(ResultCode.QUERY_SUCCESS, vos);
        }

        return Result.build(ResultCode.SERVER_ERROR);
    }

    @GetMapping("/admin/{page}")
    public String getByAdmin(@PathVariable Integer page) {
        List<Blog> records = iBlogService.page(new Page<>(page, 12)).getRecords();
        List<AdminBlogVo> vos = new ArrayList<>(records.size());
        records.forEach(blog -> {
            String[] classifications = getAllClassifications(blog.getBlogId());
            @SuppressWarnings("all")
            String classification = "";
            for (int i = 0; i < 3 && classifications[i] != null && !"".equals(classifications[i]); i++) {
                classification += classifications[i] + '/';
            }
            vos.add(new AdminBlogVo().setBlogId(blog.getBlogId())
                    .setPublishedDate(blog.getPublishedDate().toString())
                    .setClassifications(classification.substring(0, classification.length() - 1))
                    .setStatus(blog.getDraft() ? "未发布" : "已发布")
                    .setClickCount(blog.getClickCount())
                    .setBlogTitle(blog.getTitle()));

        });
        if (CollUtil.isNotEmpty(vos)) {
            return Result.build(ResultCode.QUERY_SUCCESS, vos);
        }
        return Result.build(ResultCode.SERVER_ERROR);
    }

    @GetMapping("/search/{type}/{name}")
    public String searchByTypeAndName(@PathVariable String type, @PathVariable String name) {
        List<IndexBlogVo> vos = new ArrayList<>();

        switch (type) {
            case "classifications":
                Classifications classId = iClassificationsService.getOne(Wrappers.<Classifications>lambdaQuery().eq(Classifications::getClassName, name));
                if (ObjectUtil.isNull(classId)) {
                    return Result.build(ResultCode.CLASSIFICATION_NOT_FOUND);
                }
                List<BlogClassifications> list = iBlogClassificationsService.list(Wrappers.<BlogClassifications>lambdaQuery().eq(BlogClassifications::getClassId, classId.getClassId()));

                list.forEach(blogClassifications -> {
                    Blog byId = iBlogService.getById(blogClassifications.getBlogId());
                    if (byId.getDraft()) {
                        return;
                    }
                    vos.add(new IndexBlogVo()
                            .setBlogTitle(byId.getTitle())
                            .setCoverImg(byId.getCoverImg())
                            .setPublishedDate(byId.getPublishedDate().toString())
                            .setBlogId(byId.getBlogId()));
                });
                break;

            case "tags":
                Tags one = iTagsService.getOne(Wrappers.<Tags>lambdaQuery().eq(Tags::getTagName, name));

                if (ObjectUtil.isNull(one)) {
                    return Result.build(ResultCode.TAG_NOT_FOUND);
                }

                List<BlogTags> tagsList = iBlogTagsService.list(Wrappers.<BlogTags>lambdaQuery().eq(BlogTags::getTagId, one.getTagId()));

                tagsList.forEach(blogTags -> {
                    Blog byId = iBlogService.getById(blogTags.getBlogId());

                    if (byId.getDraft()) {
                        return;
                    }
                    vos.add(new IndexBlogVo()
                            .setPublishedDate(byId.getPublishedDate().toString())
                            .setCoverImg(byId.getCoverImg())
                            .setBlogTitle(byId.getTitle())
                            .setBlogId(byId.getBlogId()));
                });
                break;
            default:
                break;
        }

        if (CollUtil.isNotEmpty(vos)) {
            return Result.build(ResultCode.QUERY_SUCCESS, vos);
        }

        return Result.build(ResultCode.SERVER_ERROR);
    }

    @GetMapping("/autoComplete/{name}")
    public String autoComplete(@PathVariable String name) {

        List<AutoCompleteSearchVo> vos = new ArrayList<>();

        List<Blog> list = iBlogService.list(Wrappers.<Blog>lambdaQuery().like(Blog::getTitle, name));

        list.forEach(blog -> {
            if (blog.getDraft()) {
                return;
            }

            vos.add(new AutoCompleteSearchVo().setValue(blog.getTitle()).setType("文章").setData(blog.getBlogId()));
        });

        List<Tags> list1 = iTagsService.list(Wrappers.<Tags>lambdaQuery().like(Tags::getTagName, name));

        list1.forEach(tags -> {
            vos.add(new AutoCompleteSearchVo().setValue(tags.getTagName()).setType("标签"));
        });

        List<Classifications> list2 = iClassificationsService.list(Wrappers.<Classifications>lambdaQuery().like(Classifications::getClassName, name));

        list2.forEach(classifications -> {
            vos.add(new AutoCompleteSearchVo().setValue(classifications.getClassName()).setType("分类"));
        });

        if (CollUtil.isNotEmpty(vos)) {
            return Result.build(ResultCode.QUERY_SUCCESS, vos);
        } else {
            return Result.build(ResultCode.SEARCH_NOT_FOUND);
        }
    }

    @GetMapping("/del/{blogId}")
    public String delByBlogId(@PathVariable String blogId) {

        TransactionStatus transactionStatus = null;
        try {
            // 开启事务
            transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);

            iBlogService.removeById(blogId);

            List<BlogClassifications> list = iBlogClassificationsService.list(Wrappers.<BlogClassifications>lambdaQuery().eq(BlogClassifications::getBlogId, blogId));

            list.forEach(blogClassifications -> {
                Classifications byId = iClassificationsService.getById(blogClassifications.getClassId());
                iClassificationsService.updateById(byId.setRelativeCount(byId.getRelativeCount() - 1));
                iBlogClassificationsService.removeById(blogClassifications);
            });

            List<BlogTags> list1 = iBlogTagsService.list(Wrappers.<BlogTags>lambdaQuery().eq(BlogTags::getBlogId, blogId));

            list1.forEach(blogTags -> {
                Tags byId = iTagsService.getById(blogTags.getTagId());
                iTagsService.updateById(byId.setRelativeCount(byId.getRelativeCount() - 1));
                iBlogTagsService.removeById(blogTags);

            });
            // 移除归档
            iBlogArchiveService.remove(Wrappers.<BlogArchive>lambdaQuery().eq(BlogArchive::getBlogId, blogId));

            // 同时成功 -> 提交事务
            dataSourceTransactionManager.commit(transactionStatus);

        } catch (Exception e) {
            assert transactionStatus != null;
            dataSourceTransactionManager.rollback(transactionStatus);
            e.printStackTrace();
            return Result.build(ResultCode.SERVER_ERROR);
        }

        return Result.build(ResultCode.DEL_SUCCESS);

    }

    private String[] getAllClassifications(String blogId) {
        String[] classifications = new String[3];
        // 查询分类
        List<BlogClassifications> list1 = iBlogClassificationsService.list(Wrappers.<BlogClassifications>lambdaQuery().eq(BlogClassifications::getBlogId, blogId));
        list1.forEach(temp -> {
            Classifications byId = iClassificationsService.getById(temp.getClassId());
            classifications[byId.getLevel()] = byId.getClassName();
        });
        return classifications;
    }

    private String[] getAllTags(String blogId) {
        List<String> tags = new ArrayList<>();
        List<BlogTags> list = iBlogTagsService.list(Wrappers.<BlogTags>lambdaQuery().eq(BlogTags::getBlogId, blogId));

        if (CollUtil.isNotEmpty(list)) {
            list.forEach(blogTags -> {
                tags.add(iTagsService.getById(blogTags.getTagId()).getTagName());
            });
        }
        return tags.toArray(String[]::new);
    }


}
