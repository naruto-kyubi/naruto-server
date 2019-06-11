package org.naruto.framework.security.service;

import org.naruto.framework.user.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ISessionService {

    void logonOK(User user , HttpServletRequest request, HttpServletResponse response);
    User getCurrentUser(HttpServletRequest request);
    void logout(User user);

}
