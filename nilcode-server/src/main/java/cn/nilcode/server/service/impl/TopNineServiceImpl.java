package cn.nilcode.server.service.impl;

import cn.nilcode.server.entity.TopNine;
import cn.nilcode.server.mapper.TopNineMapper;
import cn.nilcode.server.service.ITopNineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 点击量前9 服务实现类
 * </p>
 *
 * @author zero
 * @since 2020-01-16
 */
@Service
public class TopNineServiceImpl extends ServiceImpl<TopNineMapper, TopNine> implements ITopNineService {

}
