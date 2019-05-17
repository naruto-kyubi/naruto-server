package org.naruto.framework.security.service.captcha;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaptchaRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaService captchaService;

    public CaptchaRealm(){
        this.userService = userService;
    }
    /**
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        CaptchaToken captchaToken = (CaptchaToken)token;
//        String username = userpasswordToken.getUsername();
        captchaService.validateCaptcha(captchaToken.getMobile(), CaptchaType.LOGON,captchaToken.getToken());

        User user = userService.getUserByMobile(captchaToken.getMobile());

        if(user == null)
            throw new AuthenticationException("Invalid userName or password");

        return new SimpleAuthenticationInfo(user, captchaToken.getToken(), this.getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CaptchaToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
}
