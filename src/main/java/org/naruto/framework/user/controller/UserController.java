package org.naruto.framework.user.controller;


import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    CaptchaService captchaService;

    @RequestMapping(value = "/v1/user/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> register(@Validated @RequestBody User user){

        return ResponseEntity.ok(ResultEntity.ok(userService.register(user)));
    }

    @RequestMapping(value = "/v1/user/registerCaptcha", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> getRegisterCaptcha(@Validated @RequestParam(name = "mobile") String mobile) {
        captchaService.createCaptcha(mobile, CaptchaType.SINGUP);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

    @RequestMapping(value = "/v1/user/resetPassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> resetPassword(@Validated @RequestBody User user){

        return ResponseEntity.ok(ResultEntity.ok(userService.resetPassword(user)));
    }

    @RequestMapping(value = "/v1/user/forgotPasswordCaptcha", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> getForgotPasswordCaptcha(@Validated @RequestParam(name = "mobile") String mobile) {
        captchaService.createCaptcha(mobile, CaptchaType.FORGOTPASSWORD);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }
}
