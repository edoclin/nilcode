package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.Tags;
import cn.nilcode.server.mapper.TagsMapper;
import cn.nilcode.server.service.ITagsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements ITagsService {

}
