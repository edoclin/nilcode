package cn.nilcode.server.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.nilcode.server.entity.Links;
import cn.nilcode.server.service.ILinksService;
import cn.nilcode.server.util.Result;
import cn.nilcode.server.util.ResultCode;
import cn.nilcode.server.vo.IndexLinkVo;
import cn.nilcode.server.vo.LinkVo;
import cn.nilcode.server.vo.UpdateLinkVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 链接表 前端控制器
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@RestController
@RequestMapping("/server/links")
public class LinksController {

    @Autowired
    private ILinksService iLinksService;

    @GetMapping("/all/{page}")
    public String allByPage(@PathVariable Integer page) {

        List<Links> records = iLinksService.page(new Page<>(page, 10)).getRecords();
        List<LinkVo> vos = new ArrayList<>(records.size());

        records.forEach(links -> {
            vos.add(new LinkVo()
            .setLinkName(links.getLinkName())
            .setLinkType(links.getLinkType())
            .setLinkUrl(links.getLinkUrl()));
        });



        if (CollUtil.isNotEmpty(vos)) {
            return Result.build(ResultCode.QUERY_SUCCESS, vos);
        } else {
            return Result.build(ResultCode.NULL_LINKS_FOUND);
        }
    }

    @PostMapping("/saveOrUpdate")
    public String addLink(UpdateLinkVo linkVo) {

        Links one = iLinksService.getOne(Wrappers.<Links>lambdaQuery().eq(Links::getLinkUrl, linkVo.getOldUrl()));
        System.out.println(one);
        // 更新
        if (ObjectUtil.isNotNull(one)) {
            one.setLinkName(linkVo.getLinkName())
                    .setLinkType(linkVo.getLinkType())
                    .setLinkUrl(linkVo.getLinkUrl());
        } else {
            one = new Links().setLinkName(linkVo.getLinkName())
                    .setLinkType(linkVo.getLinkType())
                    .setLinkUrl(linkVo.getLinkUrl());
        }
        if (iLinksService.saveOrUpdate(one)) {
            return Result.build(ResultCode.SAVE_SUCCESS);
        }

        return Result.build(ResultCode.SERVER_ERROR);
    }

    @GetMapping("/total")
    public String total() {
        return Result.build(ResultCode.QUERY_SUCCESS, iLinksService.count());
    }

    @PostMapping("/del")
    public String delByUrl(LinkVo linkVo) {
        if (iLinksService.remove(Wrappers.<Links>lambdaQuery().eq(Links::getLinkUrl, linkVo.getLinkUrl()))) {
            return Result.build(ResultCode.DEL_SUCCESS);
        }
        return Result.build(ResultCode.SERVER_ERROR);
    }

    @GetMapping("/linkType/{type}")
    public String getLinkByType(@PathVariable String type) {

        System.out.println(type);
        List<Links> list = iLinksService.list(Wrappers.<Links>lambdaQuery().eq(Links::getLinkType, type));
        List<IndexLinkVo> vos = new ArrayList<>(list.size());

        list.forEach(links -> {

            vos.add(new IndexLinkVo().setLinkName(links.getLinkName()).setLinkUrl(links.getLinkUrl()));
        });

        if (CollUtil.isNotEmpty(list)) {
            return Result.build(ResultCode.QUERY_SUCCESS, vos);
        }

        return Result.build(ResultCode.NULL_LINKS_FOUND);
    }
}
