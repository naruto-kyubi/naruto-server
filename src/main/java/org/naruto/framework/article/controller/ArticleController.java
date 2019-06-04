package org.naruto.framework.article.controller;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.service.ArticleService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/v1/article/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

    public ResponseEntity<ResultEntity> add(@Validated @RequestBody Article article){

        return ResponseEntity.ok(ResultEntity.ok(articleService.save(article)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/article/query", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> query(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        Page page = articleService.queryPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryById(@PathVariable("id") String id){
        Article article = articleService.queryById(id);
        return ResponseEntity.ok(ResultEntity.ok(article));
    }
}
