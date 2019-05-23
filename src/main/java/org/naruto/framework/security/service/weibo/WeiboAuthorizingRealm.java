package org.naruto.framework.security.service.weibo;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.naruto.framework.security.service.BaseAuthorizingRealm;
import org.naruto.framework.user.domain.User;
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
    private UserService userService;

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

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("code", weiboToken.getAuthCode());
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
        //查询数据库汇总是否存在当前用户；
        User user = userService.getUserByWeibo(uid);
        if (null == user) {
            //如果不存在该微博用户，则插入新用户数据；
            user = new User();
            user.setWeibo(uid);
            user.setNickname(name);
//                user.setAvatar(profile_image_url);
            user = userService.save(user);
        }
        return new SimpleAuthenticationInfo(user, weiboToken.getAuthCode(), this.getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof WeiboToken;
    }

}
