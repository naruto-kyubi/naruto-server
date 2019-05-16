package org.naruto.framework.security.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.security.dto.LogonUser;
import org.naruto.framework.security.realm.CaptchaToken;
import org.naruto.framework.security.service.LoginService;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class LogonController {
    @Autowired
    LoginService loginService;

    @Autowired
    CaptchaService captchaService;

    @ResponseBody
    @RequestMapping(value = "/v1/logon/account", method = RequestMethod.POST ,produces ="application/json")
    public ResponseEntity<ResultEntity> login(@Validated @RequestBody LogonUser logonUser) {
        User user = loginService.login(logonUser);
        return ResponseEntity.ok(ResultEntity.ok(user));
    }

    @RequestMapping(value = "/v1/logon/captcha", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> getCaptcha(@Validated @RequestParam(name = "mobile") String mobile) {
        captchaService.getCaptcha(mobile, CaptchaType.LOGON);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }
}
