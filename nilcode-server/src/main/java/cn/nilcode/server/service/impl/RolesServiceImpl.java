package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.Roles;
import cn.nilcode.server.mapper.RolesMapper;
import cn.nilcode.server.service.IRolesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles> implements IRolesService {

}
