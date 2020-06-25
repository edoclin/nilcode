package cn.nilcode.server.controller;


import cn.hutool.core.collection.CollUtil;
import cn.nilcode.server.entity.TopNine;
import cn.nilcode.server.service.IBlogService;
import cn.nilcode.server.service.ITopNineService;
import cn.nilcode.server.util.Result;
import cn.nilcode.server.util.ResultCode;
import cn.nilcode.server.vo.TopVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 点击量前9 前端控制器
 *
 * done : 设置定时任务 定时更新 减轻服务器负载
 * </p>
 *
 * @author zero
 * @since 2020-01-16
 */
@RestController
@RequestMapping("/server/top-nine")
public class TopNineController {
    @Autowired
    private ITopNineService iTopNineService;

    @Autowired
    private IBlogService iBlogService;

    @GetMapping("/all")
    public String getAllTop() {
        List<TopNine> list = iTopNineService.list();

        List<TopVo> vos = new ArrayList<>(list.size());

        for (int i = list.size() - 1; i >= 0; i--) {
            vos.add(new TopVo().setBlogId(list.get(i).getBlogId())
            .setBlogName(iBlogService.getById(list.get(i).getBlogId()).getTitle()));
        }

        if (CollUtil.isNotEmpty(vos)) {
            return Result.build(ResultCode.QUERY_SUCCESS, vos);
        }
        return Result.build(ResultCode.NULL_TOP_NINE_FOUND);
    }
}
