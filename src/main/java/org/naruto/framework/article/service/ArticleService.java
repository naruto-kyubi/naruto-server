package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.*;
import org.naruto.framework.article.vo.ArticleVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    Article saveArticle(Article article);

    Page<Article> queryArticleByPage(Map map);

    Article queryArticleById(String id);

    Article queryDraftById(String id);

    public void deleteArticleById(String id);

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

    public Tag saveTag(Tag tag);
    public void deleteTag(Tag tag);
    public List<Tag> queryTags();


    Page<Article> queryHotList(Map map);

    Page<Article> queryFollowArticles(Map map);

}
