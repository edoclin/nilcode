package cn.nilcode.server.configuration.security;

import cn.hutool.json.JSONUtil;
import cn.nilcode.server.util.Result;
import cn.nilcode.server.util.ResultCode;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/2 13:58
 * @description
 */
@Component
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //返回json数据
        String result = null;
        if (e instanceof AccountExpiredException) {
            //账号过期
        } else if (e instanceof BadCredentialsException) {
            //密码错误
            result = Result.build(ResultCode.USER_OR_PASSWORD_ERROR, "用户名或密码错误");
        } else if (e instanceof CredentialsExpiredException) {
            //密码过期
        } else if (e instanceof DisabledException) {
            //账号不可用
        } else if (e instanceof LockedException) {
            //账号锁定
        } else if (e instanceof InternalAuthenticationServiceException) {
            result = Result.build(ResultCode.USER_OR_PASSWORD_ERROR, "用户名或密码错误");
        }else{
            result = Result.build(ResultCode.ACCOUNT_OTHER_ERROR, "其他错误");
        }
        //处理编码方式，防止中文乱码的情况
        httpServletResponse.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        httpServletResponse.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
