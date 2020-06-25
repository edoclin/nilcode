package cn.nilcode.server.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/2 13:41
 * @description
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomizeAuthenticationEntryPoint customizeAuthenticationEntryPoint;
    @Autowired
    private CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;
    @Autowired
    private CustomizeAuthenticationFailureHandler customizeAuthenticationFailureHandler;
    @Autowired
    private CustomizeLogoutSuccessHandler customizeLogoutSuccessHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        // 反斜杠
        firewall.setAllowBackSlash(true);
        firewall.setUnsafeAllowAnyHttpMethod(true);
        // 分号
        firewall.setAllowSemicolon(true);
        // 百分号
        firewall.setAllowUrlEncodedPercent(true);
        //双斜杠
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置认证方式等
        auth
                .userDetailsService(userDetailsService());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http相关的配置 包括登入登出 异常处理 会话管理等
        http.cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                // 公共 URL
                .antMatchers("/server/archive/all",
                        "/server/blog/byId/**",
                        "/server/blog/total",
                        "/server/blog/index/**",
                        "/server/blog/search/**",
                        "/server/blog/autoComplete/**",
                        "/server/classifications/all",
                        "/server/classifications/allRoot",
                        "/server/classifications/allRoot",
                        "/server/links/linkType/**",
                        "/server/tags/all",
                        "/server/top-nine/all",
                        "/server/files/**",
                        // 文件路径
                        "/files/**"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customizeAuthenticationEntryPoint)
                .and()
                .formLogin()
                .permitAll()
                .successHandler(customizeAuthenticationSuccessHandler)
                .failureHandler(customizeAuthenticationFailureHandler)
                .and()
                .logout()
                .permitAll()
                .logoutSuccessHandler(customizeLogoutSuccessHandler)
                .deleteCookies("JSESSIONID");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        //获取用户账号密码及权限信息
        return new UsersDetailsServiceImpl();
    }
}
