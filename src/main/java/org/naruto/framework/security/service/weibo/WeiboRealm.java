package org.naruto.framework.security.service.weibo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.naruto.framework.security.service.NarutoAuthorizingRealm;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.naruto.framework.utils.NetHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WeiboRealm extends NarutoAuthorizingRealm{

    @Autowired
    private UserService userService;

    @Autowired
    private WeiboConfig weiboConfig;

    /**
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        WeiboToken weiboToken = (WeiboToken) token;
        Map<String, String> map = new HashMap();

        map.put("code", weiboToken.getAuthCode());
        map.put("client_id", weiboConfig.getClientId());
        map.put("client_secret", weiboConfig.getClientSecret());
        map.put("grant_type", weiboConfig.getGrantType());
        map.put("redirect_uri", weiboConfig.getRedirectUri());

        String str = NetHttpClient.sendPost(weiboConfig.getTokenUrl(), map);

        JSONObject objToken = JSON.parseObject(str);
//			{    "access_token": "2.00b9a1uB3vRbZDba9f1f570brcTMGE",    "remind_in": "157679999",    "expires_in": 157679999,    "uid": "1748514365",    "isRealName": "true"}
        String access_token = objToken.getString("access_token");
        String uid = objToken.getString("uid");

        Map<String, String> userMap = new HashMap();
        userMap.put("access_token", access_token);
        userMap.put("uid", uid);

        String userStr = NetHttpClient.sendGet(weiboConfig.getUserUrl(), userMap);

        JSONObject objUser = JSON.parseObject(userStr);
        String name = objUser.getString("name");
//            String profile_image_url = objUser.getString("profile_image_url");

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
