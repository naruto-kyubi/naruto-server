package org.naruto.framework.security.service;

import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionUtils {

    @Autowired
    private ISessionService sessionService;

    public void logonOK(User user, HttpServletRequest request, HttpServletResponse response){
        sessionService.logonOK(user,request,response);
    }

    public User getCurrentUser(HttpServletRequest request){
        return sessionService.getCurrentUser(request);
    }

    public void logout(User user){
        sessionService.logout(user);
    }
}
