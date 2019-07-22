package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.*;
import org.naruto.framework.article.repository.*;
import org.naruto.framework.article.vo.ArticleVo;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.elasticsearch.article.service.ArticleEsService;
import org.naruto.framework.user.service.UserService;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleEsService articleEsService;


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
            lastVersion.setTags(article.getTags());
            return articleRepository.save(lastVersion);
        }

    }

    public Page<Article> queryArticleByPage(Map map) {
        return articleRepository.queryPageByCondition(map);
    }

    public Article queryArticleById(String id){
        return  articleRepository.findById(id).get();
    }

    @Override
    public void deleteArticleById(String id) {
        articleRepository.deleteById(id);
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
        articleRepository.increateCount(articleId,"view_count",1L);
//        articleRepository.increaseViewCount(articleId);
    }

    @Override
    public void increaseLikeCount(String articleId,Long step) {
        articleRepository.increateCount(articleId,"like_count",step);
//        articleRepository.increaseLikeCount(articleId,step);
    }

    @Override
    public void increaseStarCount(String articleId,Long step) {
        articleRepository.increateCount(articleId,"star_count",step);
//        articleRepository.increaseStarCount(articleId,step);
    }

    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }

    @Override
    public List<Tag> queryTags() {
        return tagRepository.findAll();
    }

//    @Override
//    public Page<ArticleVo> search(Map map) {
//
//        Map _map = PageUtils.prepareQueryPageMap(map);
//        Pageable pageable = PageUtils.createPageable(_map);
//        String keyWord = (String) map.get("keyword");
//
//        Map searchMap = new HashMap();
//        searchMap.put("title",1F);
//        searchMap.put("contentHtml",1F);
//
//        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(keyWord, "title","content").fields(searchMap);
//        query.minimumShouldMatch("40%");
//        Page<EsArticle> page = articleEsRepository.search(query,pageable);
//        List<EsArticle> list = page.getContent();
//
//        List articleList = new ArrayList();
//
//        for (EsArticle article : list) {
//            String userId = article.getUserId();
//            User user = userService.findById(userId);
//            article.setOwner(user);
//            articleList.add(article);
//        }
//
//        List voList = ObjUtils.transformerClass(articleList, ArticleVo.class);
//        return new PageImpl(voList,page.getPageable(),page.getTotalElements());
//    }

    @Override
    public Page<ArticleVo> search(Map map) {

       return articleEsService.search(map);
    }

    @Override
    public Page<Article> queryHotList(Map map) {

        return articleRepository.queryPageByCondition(map);
    }

    @Override
    public Page<Article> queryFollowArticles(Map map) {
        String currentUserId = (String) map.get("currentUserId");
        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        return articleRepository.queryArticlesByFollows(currentUserId,pageable);
    }
}
