package cn.nilcode.server.controller;


import cn.nilcode.server.util.Result;
import cn.nilcode.server.util.ResultCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@RestController
@RequestMapping("/server/users")
public class UsersController {

    @GetMapping("/check")
    public String checkLogin() {
        return Result.build(ResultCode.USER_CHECK_SUCCESS);
    }
}
