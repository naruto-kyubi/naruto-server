package org.naruto.framework.security.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.security.dto.LogonUser;
import org.naruto.framework.security.realm.CaptchaToken;
import org.naruto.framework.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public User login(LogonUser logonUser){
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
        return user;
    }

}
