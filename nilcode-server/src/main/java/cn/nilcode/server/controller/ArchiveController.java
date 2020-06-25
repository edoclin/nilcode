package cn.nilcode.server.controller;


import cn.hutool.core.collection.CollUtil;
import cn.nilcode.server.entity.Archive;
import cn.nilcode.server.entity.Blog;
import cn.nilcode.server.entity.BlogArchive;
import cn.nilcode.server.service.IArchiveService;
import cn.nilcode.server.service.IBlogArchiveService;
import cn.nilcode.server.service.IBlogService;
import cn.nilcode.server.util.Result;
import cn.nilcode.server.util.ResultCode;
import cn.nilcode.server.vo.ArchiveVo;
import cn.nilcode.server.vo.IndexArchiveVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 归档表 前端控制器
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@RestController
@RequestMapping("/server/archive")
public class ArchiveController {

    @Autowired
    private IArchiveService iArchiveService;

    @Autowired
    private IBlogArchiveService iBlogArchiveService;

    @Autowired
    private IBlogService iBlogService;

    @GetMapping("/add")
    public String add() {
        iArchiveService.save(new Archive());
        return Result.build(ResultCode.SERVER_ERROR);
    }

    @GetMapping("/all")
    public String getAllArchive() {
        List<Archive> list = iArchiveService.list(Wrappers.<Archive>lambdaQuery().orderByDesc(Archive::getDate));

        List<IndexArchiveVo> vos = new ArrayList<>(list.size());

        list.forEach(archive -> {

            List<BlogArchive> list1 = iBlogArchiveService.list(Wrappers.<BlogArchive>lambdaQuery().eq(BlogArchive::getArchiveId, archive.getArchiveId()));
            List<ArchiveVo> vos1 = new ArrayList<>(list1.size());

            list1.forEach(blogArchive -> {
                Blog byId = iBlogService.getById(blogArchive.getBlogId());
                if (byId.getDraft()) {
                    return;
                }
                vos1.add(new ArchiveVo().setBlogId(blogArchive.getBlogId())
                .setBlogName(byId.getTitle()));
            });
            if (CollUtil.isEmpty(vos1)) {
                return;
            }
            vos.add(new IndexArchiveVo().setDate("" + archive.getDate()).setList(vos1));
        });
        if (CollUtil.isNotEmpty(vos)) {
            return Result.build(ResultCode.QUERY_SUCCESS, vos);
        }

        return Result.build(ResultCode.NULL_ARCHIVE_FOUND);
    }

}
