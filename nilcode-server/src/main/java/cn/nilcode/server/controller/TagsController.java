package cn.nilcode.server.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import cn.nilcode.server.entity.Blog;
import cn.nilcode.server.entity.BlogTags;
import cn.nilcode.server.entity.Tags;
import cn.nilcode.server.service.IBlogTagsService;
import cn.nilcode.server.service.ITagsService;
import cn.nilcode.server.util.Result;
import cn.nilcode.server.util.ResultCode;
import cn.nilcode.server.vo.IndexTagVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@RestController
@RequestMapping("/server/tags")
public class TagsController {

    @Autowired
    private ITagsService iTagsService;

    @Autowired
    private IBlogTagsService iBlogTagsService;

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    TransactionDefinition transactionDefinition;

    @GetMapping("/all")
    public String getAllTags() {
        List<Tags> list = iTagsService.list(Wrappers.<Tags>lambdaQuery());
        List<IndexTagVo> vos = new ArrayList<>(list.size());
        list.forEach(tags -> {
            vos.add(new IndexTagVo().setTagId(tags.getTagId()).setTagName(tags.getTagName()));
        });
        if (CollUtil.isNotEmpty(vos)) {
            return Result.build(ResultCode.QUERY_SUCCESS, vos);
        }
        return Result.build(ResultCode.NULL_TAGS_FOUND);
    }

    @GetMapping("/del/{tagId}")
    public String delByTagId(@PathVariable String tagId) {

        TransactionStatus transactionStatus = null;
        try {
            // 开启事务
            transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);

            iTagsService.removeById(tagId);

            iBlogTagsService.remove(Wrappers.<BlogTags>lambdaQuery().eq(BlogTags::getTagId, tagId));

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

}
