package org.naruto.framework.article.listener;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.Star;
import org.naruto.framework.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;

@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
@Component
@Transactional
public class StarListener {

    @Autowired
    static private ArticleService articleService;

    @Autowired
    public void init(ArticleService articleService)
    {
        StarListener.articleService = articleService;
    }

    /**
     * 在保存之前调用
     */
    @PrePersist
    public void prePersist(Object source){
        System.out.println("@PrePersist：" + source);
    }

    /**
     * 在保在保存之后调用存之后调用
     */
    @PostPersist
    public void postPersist(Object source){
        Star star = (Star) source;
        Article article = articleService.queryArticleById(star.getArticleId());
        article.setStarCount(article.getStarCount()+1);
        articleService.saveArticle(article);
    }

    @PreRemove
    public void preRemove(Object source){
        System.out.println("@preRemove：" + source);
    }

    @PostRemove
    public void postRemove(Object source){
        Star star = (Star) source;
        String articleId= star.getArticleId();
        Article article = articleService.queryArticleById(articleId);
        article.setStarCount(article.getStarCount()-1);
        articleService.saveArticle(article);
    }
}
