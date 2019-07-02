package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.Comment;
import org.naruto.framework.article.domain.Like;
import org.naruto.framework.article.domain.Star;
import org.naruto.framework.article.repository.ArticleRepository;
import org.naruto.framework.article.repository.CommentRepository;
import org.naruto.framework.article.repository.LikeRepository;
import org.naruto.framework.article.repository.StarRepository;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private StarRepository starRepository;

    public Article saveArticle(Article article){

        if(article == null) {
                throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }
        String id = article.getId();
        if(id==null)
         return articleRepository.save(article);
        else {
            Article lastVersion = articleRepository.findById(id).get();
            lastVersion.setCatalogId(article.getCatalogId());
            lastVersion.setContent(article.getContent());
            lastVersion.setContentHtml(article.getContentHtml());
            lastVersion.setTitle(article.getTitle());
            return articleRepository.save(lastVersion);
        }

    }

    public Page<Article> queryArticleByPage(Map map) {
        return articleRepository.queryPageByCondition(map);
    }

    public Article queryArticleById(String id){
        return  articleRepository.findById(id).get();
    }

    public Comment saveComment(Comment comment){
        if(comment == null) {
            throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }

        return commentRepository.save(comment);
    }

    public Page<Comment> queryCommentByPage(Map map) {
        return commentRepository.queryPageByCondition(map);
    }

    //like,zhan;

    public Like queryLikeByUserIdAndTypeAndTargetId(String userId,String type,String targetId){

        return likeRepository.queryLikeByUserIdAndTypeAndTargetId(userId,type,targetId);
    }

    public Like saveLike(Like like){
        return likeRepository.save(like);
    }

    public void deleteLike(String userId,String type,String targetId){
        likeRepository.deleteLikeByUserIdAndTypeAndTargetId(userId,type,targetId);
    }

    public Page<Star> queryStarByPage(Map map) {
        return starRepository.queryPageByCondition(map);
    }

    @Override
    public Star queryStarByUserIdAndArticleId(String userId, String articleId) {

        return starRepository.queryStarByUserIdAndArticleId(userId,articleId);
    }

    @Override
    public Star saveStar(Star star) {

        return starRepository.save(star);
    }

    @Override
    public void deleteStar(String userId, String articleId) {

        starRepository.deleteByUserIdAndArticleId(userId,articleId);
    }

    @Override
    public void increaseViewCount(String articleId) {
        articleRepository.increaseViewCount(articleId);
    }

    @Override
    public void increaseLikeCount(String articleId,Integer step) {
        articleRepository.increaseLikeCount(articleId,step);
    }

    @Override
    public void increaseStarCount(String articleId,Integer step) {
        articleRepository.increaseStarCount(articleId,step);
    }
}
