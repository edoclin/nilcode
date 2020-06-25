package cn.nilcode.server.configuration.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/16 18:21
 * @description
 */
@Configuration
@EnableTransactionManagement
public class MBConfiguration {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
         paginationInterceptor.setLimit(12);
        return paginationInterceptor;
    }
}
