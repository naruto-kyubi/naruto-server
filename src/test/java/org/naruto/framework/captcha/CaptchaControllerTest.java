package org.naruto.framework.captcha;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.FrameworkApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FrameworkApplication.class)
public class CaptchaControllerTest {
    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    public void getCaptcha() throws Exception {
        String str = mockMvc.perform(MockMvcRequestBuilders.get("/v1/getCaptcha?mobile=13045196846")
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status",is("ok")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void getCaptchaWithNoMobile() throws Exception {
        String str = mockMvc.perform(MockMvcRequestBuilders.get("/v1/getCaptcha")
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status",is("fail")))
                .andExpect(jsonPath("$.data.errCode",is("sys.invalid-parameter.error")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void getCaptchaWithInvalidMobile() throws Exception {
        String str = mockMvc.perform(MockMvcRequestBuilders.get("/v1/getCaptcha?mobile=2345")
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status",is("fail")))
                .andExpect(jsonPath("$.data.errCode",is("captcha.unknown.error")))
                .andReturn().getResponse().getContentAsString();
    }
}
