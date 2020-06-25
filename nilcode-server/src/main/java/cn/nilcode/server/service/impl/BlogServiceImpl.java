package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.Blog;
import cn.nilcode.server.mapper.BlogMapper;
import cn.nilcode.server.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

}
