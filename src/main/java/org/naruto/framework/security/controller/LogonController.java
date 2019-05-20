package org.naruto.framework.security.controller;

import org.apache.coyote.Response;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.security.dto.LogonUser;
import org.naruto.framework.security.service.LogonService;
import org.naruto.framework.security.service.jwt.JwtUtils;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class LogonController {
    @Autowired
    LogonService logonService;
    @Autowired
    CaptchaService captchaService;

    @Value("${naruto.encrpyt.salt}")
    private String salt;

    @ResponseBody
    @RequestMapping(value = "/v1/logon/account", method = RequestMethod.POST ,produces ="application/json")
    public ResponseEntity<ResultEntity> logon(@Validated @RequestBody LogonUser logonUser, HttpServletResponse response) {

        User user = logonService.authenticate(logonUser);
        String newToken = JwtUtils.sign(user.getId(),salt,3600);
        response.setHeader("x-auth-token", newToken);
        return ResponseEntity.ok(ResultEntity.ok(user));
    }

    @RequestMapping(value = "/v1/logon/captcha", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> getCaptcha(@Validated @RequestParam(name = "mobile") String mobile) {
        captchaService.createCaptcha(mobile, CaptchaType.LOGON);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }
}
