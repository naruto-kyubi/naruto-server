package org.naruto.framework.article.controller;

import org.naruto.framework.article.domain.*;
import org.naruto.framework.article.service.ArticleService;
import org.naruto.framework.article.vo.LikeVo;
import org.naruto.framework.article.vo.StarVo;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.security.service.SessionUtils;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
public class ArticleController {

    @Autowired
    private SessionUtils sessionUtils;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/v1/articles/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> add(@Validated @RequestBody Article article,HttpServletRequest request){
        User user = sessionUtils.getCurrentUser(request);
        userService.increaseArticleCount(user.getId(),1L);

        return ResponseEntity.ok(ResultEntity.ok(articleService.saveArticle(article)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/query", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> query(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        Page page = articleService.queryArticleByPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryById(@PathVariable("id") String id){
        Article article = articleService.queryArticleById(id);
        return ResponseEntity.ok(ResultEntity.ok(article));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/comments", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryComments(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {
        Page page = articleService.queryCommentByPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/comment/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> addComment(@Validated @RequestBody Comment comment,HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        comment.setUserId(user);

        return ResponseEntity.ok(ResultEntity.ok(articleService.saveComment(comment)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/likes/{type}/{targetId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryLikeById(@PathVariable("type") String type,@PathVariable("targetId") String targetId,HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        Like like = null;
        if(null!=user){
            like = articleService.queryLikeByUserIdAndTypeAndTargetId(user.getId(),type,targetId);
        }
        Article article = articleService.queryArticleById(targetId);
        LikeVo vo = new LikeVo(like,article.getLikeCount());
        return ResponseEntity.ok(ResultEntity.ok(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/likes/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> addLike(@Validated @RequestBody Like like,HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);

        articleService.increaseLikeCount(like.getTargetId(),1);
        userService.increaseLikeCount(user.getId(),1L);

        like.setUserId(user.getId());
        articleService.saveLike(like);
        Article article = articleService.queryArticleById(like.getTargetId());
        LikeVo vo = new LikeVo(like,article.getLikeCount());

        return ResponseEntity.ok(ResultEntity.ok(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/likes/delete/{type}/{targetId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> deleteLike(@PathVariable("type") String type,@PathVariable("targetId") String targetId,HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        articleService.deleteLike(user.getId(),type,targetId);
        userService.increaseLikeCount(user.getId(),-1L);

        articleService.increaseLikeCount(targetId,-1);
        Article article = articleService.queryArticleById(targetId);
        LikeVo vo = new LikeVo(null,article.getLikeCount());

        return ResponseEntity.ok(ResultEntity.ok(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/stars/user/query", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryStar(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        Page page = articleService.queryStarByPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/stars/{articleId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryStarById(@PathVariable("articleId") String articleId,HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        Star star = null;
        if(null!=user) {
            star = articleService.queryStarByUserIdAndArticleId(user.getId(), articleId);
        }
        Article article = articleService.queryArticleById(articleId);
        StarVo vo = new StarVo(star,article.getStarCount());

        return ResponseEntity.ok(ResultEntity.ok(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/stars/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> addStar(@Validated @RequestBody Star star,HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        star.setUserId(user.getId());

        articleService.increaseStarCount(star.getArticle().getId(),1);
        userService.increaseStarCount(user.getId(),1L);

        articleService.saveStar(star);
        Article article = articleService.queryArticleById(star.getArticle().getId());
        StarVo vo = new StarVo(star,article.getStarCount());

        return ResponseEntity.ok(ResultEntity.ok(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/stars/delete/{articleId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> deleteStar(@PathVariable("articleId") String articleId,HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);

        articleService.increaseStarCount(articleId,-1);
        userService.increaseStarCount(user.getId(),-1L);

        articleService.deleteStar(user.getId(),articleId);
        Article article = articleService.queryArticleById(articleId);
        StarVo vo = new StarVo(null,article.getStarCount());

        return ResponseEntity.ok(ResultEntity.ok(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/tag/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> addComment(@Validated @RequestBody Tag tag, HttpServletRequest request){


        return ResponseEntity.ok(ResultEntity.ok(articleService.saveTag(tag)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/tags", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryTags(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        return ResponseEntity.ok(ResultEntity.ok(articleService.queryTags()));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/hot", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryHostList(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        c.add(Calendar.DATE, - 7);
        Date beforeDate = c.getTime();
        format.format(currentDate);
        map.put("sorter","viewCount_desc,updatedAt_desc");
        map.put("updatedAt_between",format.format(beforeDate) + "," + format.format(currentDate));

        Page page = articleService.queryArticleByPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/follows/articles", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryFollowArticles(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {
        User user = sessionUtils.getCurrentUser(request);
        map. put("currentUserId",user.getId());
        Page page = articleService.queryFollowArticles(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }
}
