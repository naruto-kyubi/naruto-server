package org.naruto.framework.article.controller;

import org.naruto.framework.article.domain.UserTag;
import org.naruto.framework.article.service.TagService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.security.service.SessionUtils;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private SessionUtils sessionUtils;

    @ResponseBody
    @RequestMapping(value = "/v1/tags/query", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> query(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        Page page = tagService.queryByPage(map);

        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/users/{userId}/tags", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryUserTags(
                @PathVariable("userId") String userId,
                @RequestParam(required = false) Integer currentPage,
                @RequestParam(required = false) Integer pageSize,
                HttpServletRequest request,
                HttpServletResponse response
            ) {
        Map map = new HashMap();
        map.put("currentPage",currentPage);
        map.put("pageSize",pageSize);

        User user = sessionUtils.getCurrentUser(request);
        Page page = tagService.queryUserTags(userId,user.getId(),map);

        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/users/tags", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryTags(
            @RequestParam(required = false) Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request
    ) {
        Map map = new HashMap();
        map.put("currentPage",currentPage);
        map.put("pageSize",pageSize);

        User user = sessionUtils.getCurrentUser(request);
        Page page = tagService.queryTags(user.getId(),map);

        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/users/tags/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> addUserTag(@Validated @RequestBody UserTag userTag, HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        userTag.setUserId(user.getId());
        UserTag ut = tagService.save(userTag);

        return ResponseEntity.ok(ResultEntity.ok(ut));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/users/tags/{tagId}/delete", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> deleteUserTags(@PathVariable("tagId") String tagId,HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        tagService.deleteUserTags(user.getId(),tagId);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }
}