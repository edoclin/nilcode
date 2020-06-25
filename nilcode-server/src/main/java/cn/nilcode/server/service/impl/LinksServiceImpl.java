package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.Links;
import cn.nilcode.server.mapper.LinksMapper;
import cn.nilcode.server.service.ILinksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 链接表 服务实现类
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Service
public class LinksServiceImpl extends ServiceImpl<LinksMapper, Links> implements ILinksService {

}
