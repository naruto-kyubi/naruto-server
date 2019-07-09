package org.naruto.framework.user.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.elasticsearch.user.service.UserEsService;
import org.naruto.framework.security.service.SessionUtils;
import org.naruto.framework.user.domain.Follow;
import org.naruto.framework.user.domain.Mutual;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.FollowService;
import org.naruto.framework.user.service.UserService;
import org.naruto.framework.user.vo.FollowUserVo;
import org.naruto.framework.user.vo.UserVo;
import org.naruto.framework.utils.ObjUtils;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserEsService userEsService;


    @Autowired
    private SessionUtils sessionUtils;

//    用户之间关注（one - to -one）
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

//    新增关注
    @ResponseBody
    @RequestMapping(value = "/v1/follows/add", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> add(
            @Validated @RequestBody Follow follow,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {
        User user = sessionUtils.getCurrentUser(request);

        userService.increaseFollowCount(user.getId(),1L);
        userService.increaseFanCount(follow.getFollowUser().getId(),1L);

        follow.setUser(user);

        return ResponseEntity.ok(ResultEntity.ok(followService.save(follow)));
    }

    // 取消关注
    @ResponseBody
    @RequestMapping(value = "/v1/follows/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> delete(
            @PathVariable("id") String id,
            HttpServletRequest request,
            HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        User sessionUser = (User) subject.getPrincipal();

        userService.increaseFollowCount(sessionUser.getId(),-1L);
        userService.increaseFanCount(id,-1L);

        followService.delete(sessionUser.getId(),id);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

    //关注了
    @ResponseBody
    @RequestMapping(value = "/v1/follows/users", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryUsers(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        User user =sessionUtils.getCurrentUser(request);
        map.put("currentUserId",user.getId());
        Page page = followService.queryFollowUsers(map);

        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    //用户粉丝
    @ResponseBody
    @RequestMapping(value = "/v1/follows/fans", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryFans(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        User user =sessionUtils.getCurrentUser(request);
        map.put("currentUserId",user.getId());

        Page page = followService.queryFans(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/follows/search", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> search(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        //query elasticsearch users;
        Page<UserVo> page = userEsService.search(map);
        User user =sessionUtils.getCurrentUser(request);
        List<UserVo> list = page.getContent();

        List<FollowUserVo> followList = list.stream().map(item ->{

            Follow f = followService.queryFollowByUserIdAndFollowUserId(user.getId(),item.getId());
            Follow follow = null;
            if(null!=f) follow =(Follow) ObjUtils.convert(f,Follow.class);
            FollowUserVo followUserVo = new FollowUserVo();

            ObjUtils.copyProperties(item,followUserVo);
            if(null==follow){
                User u = userService.findById(item.getId());
                followUserVo.setMutual(Mutual.NONE.getValue());
//
//
//
//                User inner_user = (User) ObjUtils.convert(u,User.class);
//                follow = new Follow();
//                inner_user.setNickname(item.getNickname());
//                inner_user.setProfile(item.getProfile());
//                follow.setFollowUser(inner_user);
//                follow.setMutual(Mutual.NONE.getValue());
            }else{
                User u = follow.getFollowUser();
                followUserVo.setMutual(follow.getMutual());

//                follow.setFollowUser(fUser);
//                User fUser = (User) ObjUtils.convert(u,User.class);
//                fUser.setNickname(item.getNickname());
//                fUser.setProfile(item.getProfile());
            }
                return followUserVo;
            }).collect(Collectors.toList());

//        List followList = new ArrayList();
//        for (UserVo item : list) {
//            Follow f = followService.queryFollowByUserIdAndFollowUserId(user.getId(),item.getId());
//            Follow follow = null;
//            if(null!=f) follow =(Follow) ObjUtils.convert(f,Follow.class);
//            if(null==follow){
//                User u = userService.findById(item.getId());
//                User inner_user = (User) ObjUtils.convert(u,User.class);
//                follow = new Follow();
//                inner_user.setNickname(item.getNickname());
//                inner_user.setProfile(item.getProfile());
//                follow.setFollowUser(inner_user);
//                follow.setMutual(Mutual.NONE.getValue());
//            }else{
//                User u = follow.getFollowUser();
//                User fUser = (User) ObjUtils.convert(u,User.class);
//                fUser.setNickname(item.getNickname());
//                fUser.setProfile(item.getProfile());
//                follow.setFollowUser(fUser);
//            }
//            followList.add(follow);
//        }
        return ResponseEntity.ok(ResultEntity.ok(followList, PageUtils.wrapperPagination(page)));
    }
}
