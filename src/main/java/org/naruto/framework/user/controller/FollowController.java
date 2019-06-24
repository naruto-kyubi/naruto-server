package org.naruto.framework.user.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.Follow;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    @RequestMapping(value = "/v1/follows/add", method = RequestMethod.POST)
    public ResponseEntity<ResultEntity> add(
            @Validated @RequestBody Follow follow,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        User sessionUser = (User) subject.getPrincipal();
        follow.setUserId(sessionUser.getId());

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
}
