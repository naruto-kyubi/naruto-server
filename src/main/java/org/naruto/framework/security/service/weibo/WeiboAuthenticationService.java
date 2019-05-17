package org.naruto.framework.security.service.weibo;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.security.dto.LogonUser;
import org.naruto.framework.security.exception.SecurityError;
import org.naruto.framework.security.service.IAuthenticationService;
import org.naruto.framework.user.domain.User;
import org.springframework.stereotype.Component;

@Component("weiboAuthenticationService")
@Slf4j
public class WeiboAuthenticationService implements IAuthenticationService {
    @Override
    public User authenticate(LogonUser logonUser) {

        Subject subject = SecurityUtils.getSubject();
        WeiboToken token = new WeiboToken(logonUser.getAuthCode());
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
            throw new ServiceException(SecurityError.LOGON_MOBILE_INCORRECT_ERROR);
        }
        return (User) subject.getPrincipal();
    }
}
