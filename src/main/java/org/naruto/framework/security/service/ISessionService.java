package org.naruto.framework.security.service;

import org.naruto.framework.user.domain.User;

import javax.servlet.http.HttpServletRequest;

public interface ISessionService {

    User getCurrentUser(HttpServletRequest request);
    void logout(User user);
}
