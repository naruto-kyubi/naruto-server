package org.naruto.framework.security.service.account;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.naruto.framework.security.service.NarutoAuthorizingRealm;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserPasswordRealm extends NarutoAuthorizingRealm{

    @Value("${naruto.encrpyt.salt}")
    private String salt;


    @Autowired
    public UserPasswordRealm(PasswordCredentialsMatcher passwordCredentialsMatcher){
//        this.userService = userService;
        this.setCredentialsMatcher(passwordCredentialsMatcher);
    }
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
    /**
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userpasswordToken = (UsernamePasswordToken)token;
        String username = userpasswordToken.getUsername();
        User user = userService.getUserByMobile(username);
        if(user == null)
            throw new AuthenticationException("Invalid userName or password");

        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(salt), this.getName());
    }

//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        User user = (User) principals.getPrimaryPrincipal();
//        List<String> roles = new ArrayList();
////        if(CollectionUtils.isEmpty(roles)) {
//        roles = userService.getUserRoles(user.getId());
////            user.setRoles(roles);
////        }
//        if (roles != null)
//            simpleAuthorizationInfo.addRoles(roles);
//        return simpleAuthorizationInfo;
//    }
}
