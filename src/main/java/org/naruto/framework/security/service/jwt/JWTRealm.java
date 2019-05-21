package org.naruto.framework.security.service.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.naruto.framework.security.service.BaseAuthorizingRealm;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JWTRealm extends BaseAuthorizingRealm {

    @Value("${naruto.encrpyt.salt}")
    private String salt;

    public JWTRealm(){
        this.setCredentialsMatcher(new JWTCredentialsMatcher());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        JWTToken jwtToken = (JWTToken) authcToken;
        String token = jwtToken.getToken();

        User user =userService.getUserById(JwtUtils.getUsername(token));

        if(user == null)
            throw new AuthenticationException("token expired，please relogin.");

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,salt,this.getName());
        return authenticationInfo;
    }
}
