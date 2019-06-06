package org.naruto.framework.user.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.security.service.LogonService;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.ThirdPartyUserService;
import org.naruto.framework.user.service.UserService;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Value("${img.location}")
    private String location;
    @Autowired
    private UserService userService;

    @Autowired
    LogonService logonService;

    @Autowired
    private ThirdPartyUserService thirdPartyUserService;

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
            @RequestBody User user,
            HttpServletRequest request,
            HttpServletResponse response) {

//        if (null == id || "".equals(id)) return null;
        if (null==user) return null;
        User dbUser = userService.findById(id);

        BeanUtils.copyProperties(user,dbUser,"mobile","password","passwordSalt","avatar","weibo","roles");
        return ResponseEntity.ok(ResultEntity.ok(userService.save(dbUser)));
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


    @ResponseBody
    @RequestMapping(value = "/v1/user/currentUser", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> getCurrentUser(
            HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        User sessionUser = (User) subject.getPrincipal();
        User localUser = userService.findById(sessionUser.getId());
        return ResponseEntity.ok(ResultEntity.ok(localUser));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/user/avatar", method = RequestMethod.POST)
    public ResponseEntity<ResultEntity> uploadFile(@RequestParam("file") MultipartFile file) throws Exception{
        //首先进行文件上传
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        uploadFile(file.getBytes(), location, fileName);

        Subject subject = SecurityUtils.getSubject();
        User sessionUser = (User) subject.getPrincipal();

        User user = userService.findById(sessionUser.getId());
        String imageUrl = "/naruto/api/images/" + fileName;
        user.setAvatar(imageUrl);
        userService.save(user);
        return ResponseEntity.ok(ResultEntity.ok(imageUrl));
    }

    /**
     * 上传文件
     * @param file  文件对应的byte数组流   使用file.getBytes()方法可以获取
     * @param filePath  上传文件路径，不包含文件名
     * @param fileName 上传文件名
     * @throws Exception
     */
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+"/"+fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    // bind with third party；
    @ResponseBody
    @RequestMapping(value = "/v1/user/bind", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> bind(
            @RequestParam("authType") String authType,
            @RequestParam("authCode") String authCode,
            HttpServletRequest request,
            HttpServletResponse response) {

        Subject subject = SecurityUtils.getSubject();
        User sessionUser = (User) subject.getPrincipal();
        ThirdPartyUser thirdPartyUser = logonService.bind(sessionUser,authType,authCode);
        return ResponseEntity.ok(ResultEntity.ok(thirdPartyUser));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/user/unbind", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> unbind(
            @RequestParam("authType") String authType,
            HttpServletRequest request,
            HttpServletResponse response) {

        Subject subject = SecurityUtils.getSubject();
        User sessionUser = (User) subject.getPrincipal();
        logonService.unbind(sessionUser,authType);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/user/queryBinds", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> queryBinds() {

        Subject subject = SecurityUtils.getSubject();
        User sessionUser = (User) subject.getPrincipal();
        List<ThirdPartyUser> thirdPartyUserList = thirdPartyUserService.findThirdPartyUsersByUser(sessionUser);
        return ResponseEntity.ok(ResultEntity.ok(thirdPartyUserList));
    }
}
