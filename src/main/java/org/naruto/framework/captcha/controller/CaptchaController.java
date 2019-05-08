package org.naruto.framework.captcha.controller;

import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CaptchaController {

    @Autowired
    CaptchaService captchaService;

    @RequestMapping(value = "/v1/getCaptcha", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> register(@Valid @RequestParam(name = "mobile") String mobile){
        return ResponseEntity.ok(ResultEntity.ok(captchaService.getCaptcha(mobile)));
    }
}
