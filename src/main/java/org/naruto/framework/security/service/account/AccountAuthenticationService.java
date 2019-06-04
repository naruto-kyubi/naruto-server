package org.naruto.framework.security.service.account;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.security.vo.LogonUser;
import org.naruto.framework.security.exception.SecurityError;
import org.naruto.framework.security.service.IAuthenticationService;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;
import org.springframework.stereotype.Component;

@Component("accountAuthenticationService")
@Slf4j
public class AccountAuthenticationService implements IAuthenticationService {
    @Override
    public User authenticate(LogonUser logonUser) {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(logonUser.getUserName(),logonUser.getPassword());
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
            throw new ServiceException(SecurityError.LOGON_PASSWORD_INCORRECT_ERROR);
        }
        return (User) subject.getPrincipal();
    }

    @Override
    public ThirdPartyUser bind(User user, String bindType, String bindUid, String bindName) {
        return null;
    }

    @Override
    public ThirdPartyUser bind(User user, String bindType, String authCode) {
        return null;
    }

    @Override
    public void unbind(User user, String authType) {

    }
}
