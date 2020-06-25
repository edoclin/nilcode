package cn.nilcode.server.configuration.security;

import cn.hutool.core.util.ObjectUtil;
import cn.nilcode.server.entity.Users;
import cn.nilcode.server.service.IUsersService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/1 16:03
 * @description
 */
@Component
@Slf4j
public class UsersDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IUsersService iUsersService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO : 根据用户名 查找到对应的密码与权限
        // 封装用户信息, 并返回, 参数分别是 : 用户名, 密码, 用户权限
        Users one = iUsersService.getOne(Wrappers.<Users>lambdaQuery().eq(Users::getUsername, username));
        User user = null;
        if (ObjectUtil.isNotNull(one)) {
            user = new User(username, one.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        }
        return user;
    }
}
