package org.naruto.framework.security.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.user.domain.User;

public class UserSesstionUtils {

    public static User getUser(){
        Subject subject = SecurityUtils.getSubject();
        return (User)subject.getPrincipal();
    }
}
