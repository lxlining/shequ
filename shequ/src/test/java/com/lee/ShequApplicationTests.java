package com.lee;

import com.lee.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShequApplicationTests {
@Autowired
    UserService userService;
    @Test
    void contextLoads() {
        //System.out.println(userService.getAll(1,1));
    }

}
