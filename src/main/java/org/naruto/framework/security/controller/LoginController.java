package org.naruto.framework.security.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class LoginController {

    @ResponseBody
    @RequestMapping(value = "/v1/login/account", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> login(@Validated @RequestParam("userName") String userName,@Validated @RequestParam("password") String password) {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        subject.login(token);
        User user = (User) subject.getPrincipal();

        return ResponseEntity.ok(ResultEntity.ok(user));

    }
}
