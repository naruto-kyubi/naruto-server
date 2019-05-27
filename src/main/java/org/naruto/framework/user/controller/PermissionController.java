package org.naruto.framework.user.controller;

import org.naruto.framework.user.service.PermissionService;
import org.naruto.framework.user.service.ResourceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PermissionController {
    @Autowired
    private ResourceRoleService resourceRoleService;

    @GetMapping(value = "/v1/perm/reloadFilterChains")
    public ResponseEntity<String> reloadFilterChains() {

        resourceRoleService.updatePermission();
        return ResponseEntity.ok("更新权限成功");
    }
}
