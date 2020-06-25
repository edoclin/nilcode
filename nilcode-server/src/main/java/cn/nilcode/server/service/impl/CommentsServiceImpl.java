package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.Comments;
import cn.nilcode.server.mapper.CommentsMapper;
import cn.nilcode.server.service.ICommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

}
