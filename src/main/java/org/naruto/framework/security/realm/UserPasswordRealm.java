package org.naruto.framework.security.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserPasswordRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    public UserPasswordRealm(){
        this.userService = userService;
        this.setCredentialsMatcher(new PasswordCredentialsMatcher());
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

//        User user = userService.getUserInfoByLoginName(username);
//        user.setEncryptPwd(new Sha256Hash(user.getPassword(),encryptSalt ).toHex());

        User user = userService.getUserByMobile(username);
        if(user == null)
            throw new AuthenticationException("Invalid userName or password");

        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(""), this.getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
//        List<String> roles = user.getRoles();

//        List<String> roles = new ArrayList();
////        if(CollectionUtils.isEmpty(roles)) {
//        roles = userService.getUserRoles(user.getId());
////            user.setRoles(roles);
////        }
//        if (roles != null)
//            simpleAuthorizationInfo.addRoles(roles);

        return simpleAuthorizationInfo;
    }
}
