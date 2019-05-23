package org.naruto.framework.user.controller;

import org.naruto.framework.user.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PermissionController {
    @Autowired
    private PermissionService permService;

    @GetMapping(value = "/v1/perm/reloadFilterChains")
    public ResponseEntity<String> reloadFilterChains() {

        permService.updatePermission();
        return ResponseEntity.ok("更新权限成功");
    }
}
