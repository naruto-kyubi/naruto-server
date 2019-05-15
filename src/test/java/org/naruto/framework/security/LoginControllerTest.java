package org.naruto.framework.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.FrameworkApplication;
import org.naruto.framework.captcha.domain.Captcha;
import org.naruto.framework.captcha.repository.CaptchaRepository;
import org.naruto.framework.core.encrpyt.IEncrpyt;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FrameworkApplication.class)
public class LoginControllerTest {
    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    private User user ;
    @Autowired
    private CaptchaRepository captchaRepository;

    @Autowired
    private IEncrpyt encrpytService;

    @Autowired
            private UserRepository userRepository;
    Captcha captcha;

    private String mobile = null;

    private String password = null;
    @Before
    public void init() {

        mobile = "13011112222";
        password = "123456";

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        captcha = captchaRepository.save(new Captcha(null,mobile,"1234",new Date()));
        user = new User();
        user.setMail("nick@yahoo.com.cn");
        user.setCaptcha(null);
        user.setNickname("nickname");
        user.setMobile(mobile);
        user.setPassword(encrpytService.encrpyt(password,""));
        userRepository.save(user);

    }
    @Test
    @Transactional
    public void loginOK() throws Exception {
        String str = mockMvc.perform(MockMvcRequestBuilders.get("/v1/login/account?userName=" + mobile + "&password=" + password)
//                .content("")
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status",is("ok")))
//                .andExpect(jsonPath("$.data.errCode",is("captcha.incorrect.error")))
                .andReturn().getResponse().getContentAsString();
    }
}
