package org.naruto.framework.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.security.service.LogonService;
import org.naruto.framework.security.service.jwt.JwtUtils;
import org.naruto.framework.security.vo.LogonUser;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.ResourceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class LogonController {
    @Autowired
    LogonService logonService;
    @Autowired
    CaptchaService captchaService;

    @Autowired
    ResourceRoleService resourceRoleService;

    @Value("${naruto.encrpyt.salt}")
    private String salt;

    @ResponseBody
    @RequestMapping(value = "/v1/logon/account", method = RequestMethod.POST ,produces ="application/json")
    public ResponseEntity<ResultEntity> logon(@Validated @RequestBody LogonUser logonUser, HttpServletResponse response) {


        User user = logonService.authenticate(logonUser);

        //if exists thrid party user info. bind it(weixin/weibo/taobo etc);
        if(StringUtils.isNotEmpty(logonUser.getBindType())
                && StringUtils.isNotEmpty(logonUser.getBindUid())
                && !logonUser.getAuthType().equals(logonUser.getBindType())
                ){
//            bind
            logonService.bind(user,logonUser.getBindType(),logonUser.getBindUid(),logonUser.getBindName());
        }
        String newToken = JwtUtils.sign(user.getId(),salt,3600);
        response.setHeader("x-auth-token", newToken);
        return ResponseEntity.ok(ResultEntity.ok(user));
    }



    @RequestMapping(value = "/v1/logon/logout", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> logout(HttpServletRequest request) {
        //在这里执行退出系统前需要清空的数据
        Subject subject= SecurityUtils.getSubject();
//        ServletContext context= request.getServletContext();
        try {
            subject.logout();
//            context.removeAttribute("error");
        }catch (SessionException e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

    @RequestMapping(value = "/v1/logon/captcha", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> getCaptcha(@Validated @RequestParam(name = "mobile") String mobile) {
        captchaService.createCaptcha(mobile, CaptchaType.LOGON);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

    @RequestMapping(value = "/v1/logon/function", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> getFunctions() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        List list = resourceRoleService.getUserFunctions(user.getId());

        list = buildMenuMap(list,"-1");

        return ResponseEntity.ok(ResultEntity.ok(list));
    }

    private List<Map> buildMenuMap(List<Map> menuList, String parentId){

        List<Map> _list = new ArrayList();
        for(Map _map : menuList){
            String _id = (String)_map.get("id");
            String _pId = (String)_map.get("parent_id");
            String _type = (String)_map.get("type");

//            String _hideInMenu = (String)_map.get("hide_in_menu");
//            if("true".equals(_hideInMenu))  continue;
            if (null != _pId && _pId.equals(parentId)) {
                Map _menuMap = new HashMap();
//                _menuMap.put("hideInMenu", _map.get("hideInMenu"));
                _menuMap.put("id", _map.get("id"));
                _menuMap.put("parent_id", _map.get("parent_id"));
                _menuMap.put("path", _map.get("path"));
                _menuMap.put("name", _map.get("name"));
                _menuMap.put("code", _map.get("code"));
                _menuMap.put("icon", _map.get("icon"));
                _menuMap.put("locale", _map.get("locale"));
                _menuMap.put("type", _map.get("type"));
                _list.add(_menuMap);
//                if ("MENU".equals(_type)) {
                    List<Map> _rtnList = buildMenuMap(menuList, _id);
                    if(null!=_rtnList && _rtnList.size()>0)
                        _menuMap.put("children", _rtnList);
//                }
            }
        }
        return _list;
    }
}
