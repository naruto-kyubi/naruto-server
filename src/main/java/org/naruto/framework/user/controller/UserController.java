package org.naruto.framework.user.controller;


import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.naruto.framework.utils.ObjUtils;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    //多条件组合查询查询
    @ResponseBody
    @RequestMapping(value = "/v1/user/query", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> query(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        //查询条件参数验证。
        Page page = userService.queryPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    // 新增记录；
    @ResponseBody
    @RequestMapping(value = "/v1/user/create", method = RequestMethod.POST)
    public ResponseEntity<ResultEntity> create(
            @Validated @RequestBody User user,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(ResultEntity.ok(userService.save(user)));
    }

    // 修改记录；
    @ResponseBody
    @RequestMapping(value = "/v1/user/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResultEntity> update(
            @PathVariable("id") String id,
            @RequestBody Map map,
            HttpServletRequest request,
            HttpServletResponse response) {

//        if (null == id || "".equals(id)) return null;
        if (CollectionUtils.isEmpty(map)) return null;
        User user = userService.findById(id);

//        BeanUtils.copyProperties(user,_user);

        ObjUtils.copyMap2Obj(map, user);
        return ResponseEntity.ok(ResultEntity.ok(userService.save(user)));
    }

    // 删除记录；
    @ResponseBody
    @RequestMapping(value = "/v1/user/delete/{ids}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultEntity> delete(
            @PathVariable("ids") String ids,
            HttpServletRequest request,
            HttpServletResponse response) {

        String[] _ids = ids.split(",");
        List idList = Arrays.asList(_ids);
        if (!CollectionUtils.isEmpty(idList)) userService.delete(idList);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }
}
