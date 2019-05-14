package org.naruto.framework.security.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.naruto.framework.security.encrpyt.IEncrpyt;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;

    @Autowired
    public UserPasswordRealm(PasswordCredentialsMatcher passwordCredentialsMatcher){
        this.userService = userService;
        this.setCredentialsMatcher(passwordCredentialsMatcher);
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

        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(""), this.getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
}
