package org.naruto.framework.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.FrameworkApplication;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.domain.Captcha;
import org.naruto.framework.captcha.repository.CaptchaRepository;
import org.naruto.framework.user.domain.User;
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
public class UserControllerTest {
    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    private User user ;
    @Autowired
    private CaptchaRepository captchaRepository;

    Captcha captcha;

    @Before
    public void init(){

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        captcha = captchaRepository.save(new Captcha(null,"18686876684", CaptchaType.SINGUP.toString(),"1234",new Date()));
        user = new User();
        user.setEmail("nick@yahoo.com.cn");
        user.setCaptcha(null);
        user.setNickname("nickname");
        user.setMobile("18686876684");
        user.setPassword("123456");
    }
    @Test
    @Transactional
    public void registerFailWithIncorrectCaptcha() throws Exception {
        user.setCaptcha("2345");
        String str = mockMvc.perform(MockMvcRequestBuilders.post("/v1/register")
                .content(JSON.toJSONString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status",is("fail")))
                .andExpect(jsonPath("$.data.errCode",is("captcha.incorrect.exception")))
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    @Transactional
    public void registerSuccess() throws Exception {
        user.setCaptcha("1234");
        String str = mockMvc.perform(MockMvcRequestBuilders.post("/v1/register")
                .content(JSON.toJSONString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status",is("ok")))
                .andReturn().getResponse().getContentAsString();
    }
}
