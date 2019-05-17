package org.naruto.framework.core;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.FrameworkApplication;
import org.naruto.framework.core.encrpyt.IEncrpyt;
import org.naruto.framework.security.service.IAuthenticationService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FrameworkApplication.class)
public class SpringUtilsTest {
    @Test
    public void getBeanTest(){
        IAuthenticationService service = (IAuthenticationService)SpringUtils.getBean("accountAuthenticationService");
        System.out.println(service.getClass());
    }
}
