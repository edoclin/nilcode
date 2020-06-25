package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.Users;
import cn.nilcode.server.mapper.UsersMapper;
import cn.nilcode.server.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
