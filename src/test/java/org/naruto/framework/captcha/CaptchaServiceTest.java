package org.naruto.framework.captcha;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.FrameworkApplication;
import org.naruto.framework.captcha.domain.Captcha;
import org.naruto.framework.captcha.repository.CaptchaRepository;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FrameworkApplication.class)
public class CaptchaServiceTest {

    private String mobile ;
    @Autowired
    CaptchaService captchaService;

    @Autowired
    private CaptchaRepository captchaRepository;


    private Captcha captcha;

    @Before
    public void before() throws Exception{
        captcha = captchaRepository.save(new Captcha(null,"18686876684",CaptchaType.SINGUP.toString(),"1234",new Date()));
    }
    @After
    public void after() throws Exception{

    }

    @Test
    @Transactional
    public void verfiyCaptchaSuccess() {
        captchaService.validateCaptcha("18686876684",CaptchaType.SINGUP,"1234");
    }

    @Test(expected = ServiceException.class)
    @Transactional
    public void verfiyCaptchaFailWithErrorCaptacha() {
        captchaService.validateCaptcha("18686876684",CaptchaType.SINGUP,"2345");

    }
}
