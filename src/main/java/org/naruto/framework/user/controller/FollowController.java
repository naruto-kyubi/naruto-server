package org.naruto.framework.user.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.Follow;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.FollowService;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class FollowController {

    @Autowired
    private FollowService followService;

    @ResponseBody
    @RequestMapping(value = "/v1/follows/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> query(@PathVariable("id") String id) {

        Subject subject = SecurityUtils.getSubject();
        User sessionUser = (User) subject.getPrincipal();
        Follow follow = null;
        if(null!=sessionUser){
            follow = followService.query(sessionUser.getId(),id);
        }
        return ResponseEntity.ok(ResultEntity.ok(follow));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/follows/add", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> add(
            @Validated @RequestBody Follow follow,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        User sessionUser = (User) subject.getPrincipal();

        follow.setUser(sessionUser);

        return ResponseEntity.ok(ResultEntity.ok(followService.save(follow)));
    }

    // 删除记录；
    @ResponseBody
    @RequestMapping(value = "/v1/follows/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> delete(
            @PathVariable("id") String id,
            HttpServletRequest request,
            HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        User sessionUser = (User) subject.getPrincipal();

        followService.delete(sessionUser.getId(),id);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/follows/fans", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryFans(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        Page page = followService.queryUserByPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/follows/users", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryUsers(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        Page page = followService.queryUserByPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

//    @ResponseBody
//    @RequestMapping(value = "/v1/follows/users/{id}", method = RequestMethod.GET)
//    public ResponseEntity<ResultEntity> queryUsers(@PathVariable("id") String id) {
//
////        Subject subject = SecurityUtils.getSubject();
////        User sessionUser = (User) subject.getPrincipal();
////        List<Follow> list = null;
////        if(null!=sessionUser){
//        List<Follow> list = followService.queryByUserId(id);
////        }
//        return ResponseEntity.ok(ResultEntity.ok(list));
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/v1/follows/fans/{id}", method = RequestMethod.GET)
//    public ResponseEntity<ResultEntity> queryFollowUsers(@PathVariable("id") String id) {
//
//        Subject subject = SecurityUtils.getSubject();
//        User sessionUser = (User) subject.getPrincipal();
//        List<Follow> list = null;
//        if(null!=sessionUser){
//            list = followService.queryByFollowUserId(sessionUser.getId());
//        }
//        return ResponseEntity.ok(ResultEntity.ok(list));
//    }
}
