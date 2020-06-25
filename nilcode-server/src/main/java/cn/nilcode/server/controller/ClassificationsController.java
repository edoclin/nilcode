package cn.nilcode.server.controller;

import java.util.*;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.nilcode.server.entity.Classifications;
import cn.nilcode.server.service.IClassificationsService;
import cn.nilcode.server.util.Result;
import cn.nilcode.server.util.ResultCode;
import cn.nilcode.server.vo.ClassificationVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 分类表 前端控制器
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@RestController
@RequestMapping("/server/classifications")
public class ClassificationsController {

    @Autowired
    private IClassificationsService iClassificationsService;

    @PostMapping("/saveOrRemove")
    public String addClassification(String className) {
        String[] split = className.split("//");
        int length = split.length;

        if (length >= 1) {
            // 根分类 没有则创建
            Classifications root = iClassificationsService.getOne(Wrappers.<Classifications>lambdaQuery().eq(Classifications::getClassName, split[0]));

            if (length == 1 && ObjectUtil.isNotNull(root)) {
                // done :移除 检查是否存在子分类

                if (CollUtil.isNotEmpty(iClassificationsService.list(Wrappers.<Classifications>lambdaQuery().eq(Classifications::getParentClassId, root.getClassId())))) {
                    // 存在子分类
                    return Result.build(ResultCode.CLASSIFICATION_CHILDREN_EXISTED);

                }
                iClassificationsService.removeById(root.getClassId());
                return Result.build(ResultCode.CLASSIFICATION_REMOVE_SUCCESS);
            }

            if (!ObjectUtil.isNotNull(root)) {
                // 该分类不存在
                root = new Classifications().setClassName(split[0]);
                if (iClassificationsService.save(root) && length == 1) {
                    // done : 添加成功
                    return Result.build(ResultCode.CLASSIFICATION_SAVE_SUCCESS);
                }
            }

            if (length >= 2) {
                Map<SFunction<Classifications, ?>, Object> filter = new HashMap<>(2);

                filter.put(Classifications::getClassName, split[1]);
                filter.put(Classifications::getParentClassId, root.getClassId());

                Classifications second = iClassificationsService.getOne(Wrappers.<Classifications>lambdaQuery().allEq(filter));


                if (length == 2 && ObjectUtil.isNotNull(second)) {
                    // done :移除 检查是否存在子分类

                    if (CollUtil.isNotEmpty(iClassificationsService.list(Wrappers.<Classifications>lambdaQuery().eq(Classifications::getParentClassId, second.getClassId())))) {
                        // 存在子分类
                        return Result.build(ResultCode.CLASSIFICATION_CHILDREN_EXISTED);

                    }
                    iClassificationsService.removeById(second.getClassId());
                    return Result.build(ResultCode.CLASSIFICATION_REMOVE_SUCCESS);
                }

                if (!ObjectUtil.isNotNull(second)) {
                    // 二级分类 没有则创建
                    second = new Classifications().setParentClassId(root.getClassId())
                            .setClassName(split[1]).setLevel(1);

                    if (iClassificationsService.save(second) && length == 2) {
                        // todo : 添加成功
                        return Result.build(ResultCode.CLASSIFICATION_SAVE_SUCCESS);
                    }
                }

                // 三级分类 存在则删除 没有则创建
                filter.clear();
                filter.put(Classifications::getClassName, split[2]);
                filter.put(Classifications::getParentClassId, second.getClassId());

                Classifications last = iClassificationsService.getOne(Wrappers.<Classifications>lambdaQuery().allEq(filter));

                if (ObjectUtil.isNotNull(last)) {
                    // 存在
                    iClassificationsService.removeById(last.getClassId());
                    return Result.build(ResultCode.CLASSIFICATION_REMOVE_SUCCESS);
                } else {
                    last = new Classifications().setClassName(split[2])
                            .setLevel(2)
                            .setParentClassId(second.getClassId());
                    if (iClassificationsService.save(last)) {
                        return Result.build(ResultCode.CLASSIFICATION_SAVE_SUCCESS);
                    }
                }
            }

        } else {
            return Result.build(ResultCode.INVALID_PARAM_ERROR);
        }

        return Result.build(ResultCode.SERVER_ERROR);
    }

    @GetMapping("/all")
    public String allClassifications() {
        // 一级分类
        List<Classifications> classificationsList = iClassificationsService.list(Wrappers.<Classifications>lambdaQuery().eq(Classifications::getLevel, 0));

        // vo 所有分类信息
        List<ClassificationVo> vos = new ArrayList<>(classificationsList.size());
        classificationsList.forEach(classifications -> {
            // 遍历子分类
            // 1. 构造条件
            Map<SFunction<Classifications, ?>, Object> map = new HashMap<>(2);
            map.put(Classifications::getLevel, 1);
            // 二级 parent_id = 一级 id
            map.put(Classifications::getParentClassId, classifications.getClassId());
            ClassificationVo classificationVo = new ClassificationVo().setClassId(classifications.getClassId())
                    .setValue(classifications.getClassName())
                    .setLabel(classifications.getClassName())
                    .setRelativeCount(classifications.getRelativeCount());
            // 二级分类
            List<Classifications> list = iClassificationsService.list(Wrappers.<Classifications>lambdaQuery().allEq(map));
            // 存在二级分类
            if (CollUtil.isNotEmpty(list)) {
                List<ClassificationVo> children = new ArrayList<>();
                // 遍历二级分类
                list.forEach(classifications1 -> {
                    map.clear();
                    map.put(Classifications::getLevel, 2);
                    // 三级 parent_id = 二级 id
                    map.put(Classifications::getParentClassId, classifications1.getClassId());
                    List<Classifications> lastList = iClassificationsService.list(Wrappers.<Classifications>lambdaQuery().allEq(map));
                    ClassificationVo second = new ClassificationVo().setClassId(classifications1.getClassId())
                            .setValue(classifications1.getClassName())
                            .setLabel(classifications1.getClassName());
                    // 存在三级分类
                    if (CollUtil.isNotEmpty(lastList)) {
                        List<ClassificationVo> secondChildren = new ArrayList<>(lastList.size());
                        // 遍历 添加到二级分类 children

                        lastList.forEach(classifications2 -> {
                            secondChildren.add(new ClassificationVo()
                                    .setRelativeCount(classifications2.getRelativeCount())
                                    .setValue(classifications2.getClassName())
                                    .setLabel(classifications2.getClassName())
                                    .setClassId(classifications2.getClassId()));
                        });
                        second.setChildren(secondChildren);
                    }
                    children.add(second);
                });
                // 添加子分类
                classificationVo.setChildren(children);
            }
            // 不存在二级分类 直接添加
            vos.add(classificationVo);
        });
        return Result.build(ResultCode.QUERY_SUCCESS, vos);
    }

    @GetMapping("/allRoot")
    public String getAllRoot() {
        List<Classifications> list = iClassificationsService.list(Wrappers.<Classifications>lambdaQuery().eq(Classifications::getLevel, 0).select(Classifications::getClassName, Classifications::getRelativeCount));
        if (CollUtil.isNotEmpty(list)) {
            return Result.build(ResultCode.QUERY_SUCCESS, list);
        }


        return Result.build(ResultCode.NULL_CLASSIFICATIONS_FOUND);
    }
}
