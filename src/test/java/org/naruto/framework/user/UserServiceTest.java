package org.naruto.framework.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.FrameworkApplication;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FrameworkApplication.class)
public class UserServiceTest {
    @Resource
    private WebApplicationContext webApplicationContext;

    @Autowired
    UserService userService;

    @Test
    public void testQueryCustomPage(){

        Map map = new HashMap();
        map.put("mobile","18686876");
        Page<User> page = userService.queryCustomPage(map);
        System.out.println("getTotalElements:" + page.getTotalElements());
        System.out.println("getTotalPages:" + page.getTotalPages());
        System.out.println("getNumber:" + page.getNumber());
        System.out.println("getSize:" + page.getSize());

        for (User user : page.getContent()) {
            System.out.println("user=" + user);
        }
    }
}
