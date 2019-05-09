package org.naruto.framework.captcha;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.FrameworkApplication;
import org.naruto.framework.captcha.domain.Captcha;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FrameworkApplication.class)
public class CaptchaServiceTest {

    private String mobile ;
    @Autowired
    CaptchaService captchaService;

    @Before
    public void before() throws Exception{
        mobile = "13704812516";
    }
    @After
    public void after() throws Exception{

    }

    @Test
    @Transactional
    public void getCaptcha() {
        Captcha captha = captchaService.getCaptcha(mobile);
        Assert.assertNotNull(captha);
    }
}
