package org.naruto.framework.security.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.security.service.ResourceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PermisionController {

    @Autowired
    private ResourceRoleService resourceRoleService;

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



    @GetMapping(value = "/v1/perm/reloadFilterChains")
    public ResponseEntity<String> reloadFilterChains() {

        resourceRoleService.updatePermission();
        return ResponseEntity.ok("更新权限成功");
    }
}