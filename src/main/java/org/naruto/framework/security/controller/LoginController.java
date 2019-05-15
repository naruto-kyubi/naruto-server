package org.naruto.framework.security.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.security.realm.CaptchaToken;
import org.naruto.framework.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @ResponseBody
    @RequestMapping(value = "/v1/login/account", method = RequestMethod.POST ,produces ="application/json")
    public ResponseEntity<ResultEntity> login(@Validated @RequestBody LogonUser logonUser) {
        User user = null;
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = null;
        if("account".equals(logonUser.getType())){
            token = new UsernamePasswordToken(logonUser.getUserName(),logonUser.getPassword());
        }else if("mobile".equals(logonUser.getType())){
            token = new CaptchaToken(logonUser.getMobile(),logonUser.getCaptcha());
        }
        subject.login(token);
        user = (User) subject.getPrincipal();

        return ResponseEntity.ok(ResultEntity.ok(user));
    }

    @Data
    @NoArgsConstructor
    static class LogonUser {
        private String type;
        private String userName;
        private String password;
        private String mobile;
        private String captcha;
    }
}
