package org.naruto.framework.security.service;

import org.apache.shiro.authc.AuthenticationToken;
import org.naruto.framework.security.dto.LogonUser;
import org.naruto.framework.security.realm.CaptchaToken;
import org.naruto.framework.security.realm.NarutoUsernamePasswordToken;
import org.naruto.framework.security.realm.ValidateToken;
import org.naruto.framework.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public User login(LogonUser logonUser){

        ValidateToken token = (ValidateToken)CreateToken(logonUser);
        token.validate();
        return (User) token.getPrincipal();
    }

    public AuthenticationToken CreateToken(LogonUser logonUser){

        AuthenticationToken token = null;
        if("account".equals(logonUser.getType())){
            token = new NarutoUsernamePasswordToken(logonUser.getUserName(),logonUser.getPassword());
        }else if("mobile".equals(logonUser.getType())){
            token = new CaptchaToken(logonUser.getMobile(),logonUser.getCaptcha());
        }
        return token;
    }
}
