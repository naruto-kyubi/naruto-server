package org.naruto.framework.security.service.captcha;

import org.apache.shiro.authc.*;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.domain.Captcha;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.security.service.BaseAuthorizingRealm;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaptchaRealm extends BaseAuthorizingRealm {

//    @Autowired
//    private UserService userService;

    @Autowired
    private CaptchaService captchaService;

    /**
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        CaptchaToken captchaToken = (CaptchaToken)token;
        User user = userService.getUserByMobile(captchaToken.getMobile());
        if(user == null)
            throw new AuthenticationException("Invalid userName or password");

        Captcha captcha = captchaService.getCaptcha(captchaToken.getMobile(),CaptchaType.LOGON);
        return new SimpleAuthenticationInfo(user, captcha.getCaptcha(), this.getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CaptchaToken;
    }

//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        return null;
//    }
}
