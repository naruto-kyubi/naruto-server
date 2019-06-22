package org.naruto.framework.article.listener;

import org.naruto.framework.article.domain.Like;
import org.naruto.framework.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;

@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
@Component
public class LikeListener {

    @Autowired
    static private ArticleService articleService;

    @Autowired
    public void init(ArticleService articleService)
    {
        LikeListener.articleService = articleService;
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
        Like like = (Like) source;
//        Article article = articleService.queryArticleById(like.getTargetId());
//        article.setLikeCount(article.getLikeCount()+1);
//        articleService.saveArticle(article);
        articleService.increaseLikeCount(like.getTargetId(),1);
    }


    @PreRemove
    public void preRemove(Object source){
        System.out.println("@preRemove：" + source);
    }

    @PostRemove
    public void postRemove(Object source){
        Like like = (Like) source;
//        String articleId= like.getTargetId();
//        Article article = articleService.queryArticleById(articleId);
//        article.setLikeCount(article.getLikeCount()-1);
//        articleService.saveArticle(article);
        articleService.increaseLikeCount(like.getTargetId(),-1);
    }
}
