package cn.nilcode.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author orez
 */
@SpringBootApplication
@MapperScan("cn.nilcode.server.mapper")
public class NilcodeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NilcodeServerApplication.class, args);
    }

}
