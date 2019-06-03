package org.naruto.framework.security.service.weibo;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.security.service.BaseAuthorizingRealm;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.ThirdPartyUserService;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class WeiboAuthorizingRealm extends BaseAuthorizingRealm {

    @Autowired
    private ThirdPartyUserService thirdPartyUserService;

    @Autowired
    private WeiboConfig weiboConfig;

    @Autowired
    private RestTemplate restTemplate;
    /**
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        WeiboToken weiboToken = (WeiboToken) token;

        ThirdPartyUser thirdPartyUser = thirdPartyUserService.findThirdPartyUserByTypeAndUid(weiboToken.getType(),weiboToken.getUid());
        if (null == thirdPartyUser) {
            throw new AuthenticationException(" weibo not binding with a main account");
        }
        return new SimpleAuthenticationInfo(thirdPartyUser.getUser(), thirdPartyUser.getUid(), this.getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof WeiboToken;
    }
}
