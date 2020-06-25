package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.Classifications;
import cn.nilcode.server.mapper.ClassificationsMapper;
import cn.nilcode.server.service.IClassificationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Service
public class ClassificationsServiceImpl extends ServiceImpl<ClassificationsMapper, Classifications> implements IClassificationsService {

}
