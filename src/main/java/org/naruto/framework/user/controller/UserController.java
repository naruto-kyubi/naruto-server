package org.naruto.framework.user.controller;


import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/v1/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> register(@Valid @RequestBody User user){
        return ResponseEntity.ok(ResultEntity.ok(userService.register(user)));
    }
}
