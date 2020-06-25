package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.Archive;
import cn.nilcode.server.mapper.ArchiveMapper;
import cn.nilcode.server.service.IArchiveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 归档表 服务实现类
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Service
public class ArchiveServiceImpl extends ServiceImpl<ArchiveMapper, Archive> implements IArchiveService {

}
