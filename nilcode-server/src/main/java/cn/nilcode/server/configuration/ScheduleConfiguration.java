package cn.nilcode.server.configuration;

import cn.nilcode.server.entity.Blog;
import cn.nilcode.server.entity.TopNine;
import cn.nilcode.server.service.IBlogService;
import cn.nilcode.server.service.ITopNineService;
import cn.nilcode.server.vo.UpdateLinkVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/21 12:28
 * @description
 */
@EnableScheduling
@Configuration
public class ScheduleConfiguration {

    @Autowired
    private ITopNineService iTopNineService;

    @Autowired
    private IBlogService iBlogService;
    /**
     * 每天凌晨四点
     */
    @Scheduled(cron="0 00 04 * * ?")
    public void updateTopByEveryDay() {
        List<TopNine> list = iTopNineService.list(Wrappers.<TopNine>lambdaQuery().select(TopNine::getTNId));

        list.forEach(topNine -> {
            iTopNineService.removeById(topNine);
        });

        List<Blog> list1 = iBlogService.list(Wrappers.<Blog>lambdaQuery().orderByDesc(Blog::getClickCount).select(Blog::getBlogId, Blog::getClickCount));

        for (int i = 0; i < list1.size() && i < 9; i++) {
            System.out.println(list1.get(i));
            iTopNineService.save(new TopNine().setBlogId(list1.get(i).getBlogId()));
        }
    }
}
