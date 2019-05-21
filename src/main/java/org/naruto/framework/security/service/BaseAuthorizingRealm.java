package org.naruto.framework.security.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.naruto.framework.user.domain.Role;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class BaseAuthorizingRealm extends AuthorizingRealm {
    @Autowired
    protected UserService userService;

    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
//        Set<Role> roles = userService.getUserRoles(user.getId());

        //获取集合某一对象属性集合；
        List<String> roleList = (List<String>) CollectionUtils.collect(user.getRoles(), new Transformer() {
            @Override
            public Object transform(Object o) {
                Role role = (Role) o;
                return role.getId();
            }
        });

        if (roleList != null) simpleAuthorizationInfo.addRoles(roleList);
        return simpleAuthorizationInfo;
    }
}
