package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.BlogClassifications;
import cn.nilcode.server.mapper.BlogClassificationsMapper;
import cn.nilcode.server.service.IBlogClassificationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客-分类表 服务实现类
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Service
public class BlogClassificationsServiceImpl extends ServiceImpl<BlogClassificationsMapper, BlogClassifications> implements IBlogClassificationsService {

}
