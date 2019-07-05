package org.naruto.framework.article.service;

import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.Comment;
import org.naruto.framework.article.domain.Like;
import org.naruto.framework.article.domain.Star;
import org.naruto.framework.article.repository.ArticleRepository;
import org.naruto.framework.article.repository.CommentRepository;
import org.naruto.framework.article.repository.LikeRepository;
import org.naruto.framework.article.repository.StarRepository;
import org.naruto.framework.article.vo.ArticleVo;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.elasticsearch.article.domain.EsArticle;
import org.naruto.framework.elasticsearch.article.repository.ArticleEsRepository;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.naruto.framework.utils.ObjUtils;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
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
    private ArticleEsRepository articleEsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

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

    public Page<ArticleVo> queryArticleByPage(Map map) {
        Page page =  articleRepository.queryPageByCondition(map);
        return PageUtils.wrapperVoPage(page,ArticleVo.class);
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
        articleRepository.increateCount(articleId,"view_count",1L);
    }

    @Override
    public void increaseLikeCount(String articleId,Integer step) {
        articleRepository.increateCount(articleId,"like_count",1L);
    }

    @Override
    public void increaseStarCount(String articleId,Integer step) {
        articleRepository.increateCount(articleId,"star_count",1L);
    }

    @Override
    public Page<ArticleVo> search(Map map) {

        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        String keyWord = (String) map.get("keyword");

        Map searchMap = new HashMap();
        searchMap.put("title",1F);
        searchMap.put("content",1F);

        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(keyWord, "title","content").fields(searchMap);
        query.minimumShouldMatch("90%");
        Page<EsArticle> page = articleEsRepository.search(query,pageable);
        List<EsArticle> list = page.getContent();

        List articleList = new ArrayList();

        for (EsArticle article : list) {
            String userId = article.getUserId();
            User user = userService.findById(userId);
            article.setOwner(user);
            articleList.add(article);
        }

        List voList = ObjUtils.transformerClass(articleList, ArticleVo.class);
        return new PageImpl(voList,page.getPageable(),page.getTotalElements());
    }

//    @Override
//    public Page<ArticleVo> search(Map map) {
//
//        Map _map = PageUtils.prepareQueryPageMap(map);
//        Pageable pageable = PageUtils.createPageable(_map);
//        String keyWord = (String) map.get("keyword");
//
////        Map searchMap = new HashMap();
////        searchMap.put("title",1F);
////        searchMap.put("content",1F);
//
//
//        String preTag = "<font color='#dd4b39'>";//google的色值
//        String postTag = "</font>";
//
//        SearchQuery searchQuery = new NativeSearchQueryBuilder().
//                withQuery(QueryBuilders.matchQuery("title", keyWord)).
//                withQuery(QueryBuilders.matchQuery("contentHtml", keyWord)).
//                withHighlightFields(new HighlightBuilder.Field("title").preTags(preTag).postTags(postTag),
//                        new HighlightBuilder.Field("contentHtml").preTags(preTag).postTags(postTag)).build();
//        searchQuery.setPageable(pageable);
//        Page page = articleEsRepository.search(searchQuery);
//// 高亮字段
////        AggregatedPage<EsArticle> page = elasticsearchTemplate.queryForPage(searchQuery, EsArticle.class, new SearchResultMapper() {
////
////            @Override
////            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
////                List<EsArticle> chunk = new ArrayList<>();
////                for (SearchHit searchHit : response.getHits()) {
////                    if (response.getHits().getHits().length <= 0) {
////                        return null;
////                    }
////                    EsArticle idea = new EsArticle();
////                    //name or memoe
////                    HighlightField ideaTitle = searchHit.getHighlightFields().get("title");
////                    if (ideaTitle != null) {
////                        idea.setTitle(ideaTitle.fragments()[0].toString());
////                    }
////                    HighlightField ideaContent = searchHit.getHighlightFields().get("contentHtml");
////                    if (ideaContent != null) {
////                        idea.setContentHtml(ideaContent.fragments()[0].toString());
////                    }
////
////                    chunk.add(idea);
////                }
////                if (chunk.size() > 0) {
////                    return new AggregatedPageImpl<>((List<T>) chunk);
////                }
////                return null;
////            }
////        });
//
//
//
////        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(keyWord, "title","content").fields(searchMap);
////        query.minimumShouldMatch("90%");
////        Page<EsArticle> page = articleEsRepository.search(query,pageable);
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
}
