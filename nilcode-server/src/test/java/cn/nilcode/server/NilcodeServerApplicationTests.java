package cn.nilcode.server;

import cn.hutool.core.date.DateUtil;
import cn.nilcode.server.configuration.ScheduleConfiguration;
import cn.nilcode.server.entity.Users;
import cn.nilcode.server.service.IUsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class NilcodeServerApplicationTests {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private IUsersService iUsersService;

    @Test
    void contextLoads() {
        iUsersService.save(new Users());
        System.out.println(bCryptPasswordEncoder.encode("myzero0713"));
    }

}
