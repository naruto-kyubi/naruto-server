package org.naruto.framework.security.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.security.error.SecurityError;

@Slf4j
public class NarutoUsernamePasswordToken extends UsernamePasswordToken implements ValidateToken{

    public NarutoUsernamePasswordToken(String userName, String password){
        super(userName,password);
    }

    @Override
    public void validate() {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(this);
        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
            throw new ServiceException(SecurityError.LOGON_PASSWORD_INCORRECT_ERROR);
        }
    }

    @Override
    public Object getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        return subject.getPrincipal();
    }
}
