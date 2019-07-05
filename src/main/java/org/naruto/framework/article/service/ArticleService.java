package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.Comment;
import org.naruto.framework.article.domain.Like;
import org.naruto.framework.article.domain.Star;
import org.naruto.framework.article.vo.ArticleVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ArticleService {

    Article saveArticle(Article article);

    Page<ArticleVo> queryArticleByPage(Map map);

    Article queryArticleById(String id);

    Comment saveComment(Comment comment);

    Page<Comment> queryCommentByPage(Map map);

    //like,zhan;

    Like queryLikeByUserIdAndTypeAndTargetId(String userId,String type,String targetId);

    Like saveLike(Like like);

    void deleteLike(String userId,String type,String targetId);

    Page<Star> queryStarByPage(Map map);

    Star queryStarByUserIdAndArticleId(String userId, String articleId);

    Star saveStar(Star star);

    public void deleteStar(String userId,String articleId);

    void increaseViewCount(String articleId);

    void increaseLikeCount(String articleId,Integer step);

    void increaseStarCount(String articleId,Integer step);

    Page<ArticleVo> search(Map map);

}
