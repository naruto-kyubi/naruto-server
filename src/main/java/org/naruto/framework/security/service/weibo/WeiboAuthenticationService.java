package org.naruto.framework.security.service.weibo;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.security.vo.LogonUser;
import org.naruto.framework.security.service.IAuthenticationService;
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

@Component("weiboAuthenticationService")
@Slf4j
public class WeiboAuthenticationService implements IAuthenticationService {
    @Autowired
    private WeiboConfig weiboConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ThirdPartyUserService thirdPartyUserService;

    @Override
    public User authenticate(LogonUser logonUser) {

        Map<String,String> authInfo = null;
        Subject subject = SecurityUtils.getSubject();
        try {
            authInfo = getAuthInfo(logonUser.getAuthCode());
            WeiboToken token = new WeiboToken(logonUser.getAuthType(),authInfo.get("uid"));
            subject.login(token);
        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
            WeiboError error = WeiboError.WEIBO_NOTBINDED_ERROR;
            error.setData(authInfo);
            throw new ServiceException(error);
        }
        return (User) subject.getPrincipal();
    }

    @Override
    public void bind(User user,String bindType, String bindUid, String bindName) {

        ThirdPartyUser thirdPartyUser = new ThirdPartyUser(null,bindType,bindUid,bindName,user);
        thirdPartyUserService.save(thirdPartyUser);
    }

    private Map<String,String> getAuthInfo(String authCode) {

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("code", authCode);
        map.add("client_id", weiboConfig.getClientId());
        map.add("client_secret", weiboConfig.getClientSecret());
        map.add("grant_type", weiboConfig.getGrantType());
        map.add("redirect_uri", weiboConfig.getRedirectUri());

        JSONObject objToken = restTemplate.postForObject(weiboConfig.getTokenUrl(), map, JSONObject.class);
        String access_token = objToken.getString("access_token");
        String uid = objToken.getString("uid");

        Map<String, String> userMap = new HashMap();
        userMap.put("access_token", access_token);
        userMap.put("uid", uid);
        String userUrl = weiboConfig.getUserUrl().concat("?access_token={access_token}&uid={uid}");
        JSONObject objUser = restTemplate.getForObject(userUrl,JSONObject.class,userMap);
        String name = objUser.getString("name");

        Map mapInfo =new HashMap();
        mapInfo.put("uid",uid);
        mapInfo.put("name",name);

        return mapInfo;
    }
}
