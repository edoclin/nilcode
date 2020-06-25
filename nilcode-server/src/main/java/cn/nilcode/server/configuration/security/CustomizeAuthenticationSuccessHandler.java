package cn.nilcode.server.configuration.security;

import cn.nilcode.server.util.Result;
import cn.nilcode.server.util.ResultCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/2 13:55
 * @description
 */
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //todo : 具体根据自己的业务需求进行扩展
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(Result.build(ResultCode.LOGIN_SUCCESS, "登录成功"));
    }
}
