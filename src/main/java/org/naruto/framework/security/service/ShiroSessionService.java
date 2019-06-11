package org.naruto.framework.security.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.security.service.jwt.JwtUtils;
import org.naruto.framework.user.domain.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@ConditionalOnMissingBean(name="sessionService")
public class ShiroSessionService implements ISessionService{

    @Override
    public void logonOK(User user, HttpServletRequest request, HttpServletResponse response) {
        String newToken = JwtUtils.sign(user.getId(), user.getPasswordSalt(), 3600);
        Cookie cToken = new Cookie("x-auth-token", newToken);
        cToken.setMaxAge(259200);
        cToken.setPath(((HttpServletRequest) request).getContextPath());
        ((HttpServletResponse) response).addCookie(cToken);
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }

    @Override
    public void logout(User user) {
        Subject subject = SecurityUtils.getSubject();
        if(null!=subject) subject.logout();
    }

}
