package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.Comment;
import org.naruto.framework.article.domain.Like;
import org.naruto.framework.article.domain.Star;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ArticleService {

    public Article saveArticle(Article article);

    public Page<Article> queryArticleByPage(Map map);

    public Article queryArticleById(String id);

    public Comment saveComment(Comment comment);

    public Page<Comment> queryCommentByPage(Map map);

    //like,zhan;

    public Like queryLikeByUserIdAndTypeAndTargetId(String userId,String type,String targetId);

    public Like saveLike(Like like);

    public void deleteLike(String userId,String type,String targetId);

    public Star queryStarByUserIdAndArticleId(String userId, String articleId);

    public Star saveStar(Star star);

    public void deleteStar(String userId,String articleId);
}
