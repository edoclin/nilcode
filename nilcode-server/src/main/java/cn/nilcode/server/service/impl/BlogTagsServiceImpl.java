package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.BlogTags;
import cn.nilcode.server.mapper.BlogTagsMapper;
import cn.nilcode.server.service.IBlogTagsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客-标签表 服务实现类
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Service
public class BlogTagsServiceImpl extends ServiceImpl<BlogTagsMapper, BlogTags> implements IBlogTagsService {

}
