package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.Files;
import cn.nilcode.server.mapper.FilesMapper;
import cn.nilcode.server.service.IFilesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件表 服务实现类
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements IFilesService {

}
