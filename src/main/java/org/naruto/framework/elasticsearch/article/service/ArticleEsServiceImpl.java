package org.naruto.framework.elasticsearch.article.service;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.naruto.framework.article.vo.ArticleVo;
import org.naruto.framework.core.elasticsearch.HighLightResultMapper;
import org.naruto.framework.elasticsearch.article.domain.EsArticle;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.naruto.framework.utils.ObjUtils;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ArticleEsServiceImpl implements ArticleEsService{
    @Autowired
    private UserService userService;

    @Resource
    private HighLightResultMapper highLightResultMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Page<ArticleVo> search(Map map) {

        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        String keyWord = (String) map.get("keyword");

        String preTag = "<font color='#dd4b39'>";//google的色值
        String postTag = "</font>";

        SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withQuery(QueryBuilders.multiMatchQuery(keyWord, "title","contentHtml")).
        withHighlightFields(
            new HighlightBuilder.Field("title").preTags(preTag).postTags(postTag),
            new HighlightBuilder.Field("contentHtml").preTags(preTag).postTags(postTag)
        ).withMinScore(0.9F).
                        build();
        searchQuery.setPageable(pageable);
        Page<EsArticle> page = elasticsearchTemplate.queryForPage(searchQuery, EsArticle.class, highLightResultMapper);

//        Page page = articleEsRepository.search(searchQuery,);
// 高亮字段
//        AggregatedPage<EsArticle> page = elasticsearchTemplate.queryForPage(searchQuery, EsArticle.class, new SearchResultMapper() {
//
//            @Override
//            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
//                List<EsArticle> chunk = new ArrayList<>();
//                for (SearchHit searchHit : response.getHits()) {
//                    if (response.getHits().getHits().length <= 0) {
//                        return null;
//                    }
//                    EsArticle idea = new EsArticle();
//                    //name or memoe
//                    HighlightField ideaTitle = searchHit.getHighlightFields().get("title");
//                    if (ideaTitle != null) {
//                        idea.setTitle(ideaTitle.fragments()[0].toString());
//                    }
//                    HighlightField ideaContent = searchHit.getHighlightFields().get("contentHtml");
//                    if (ideaContent != null) {
//                        idea.setContentHtml(ideaContent.fragments()[0].toString());
//                    }
//
//                    chunk.add(idea);
//                }
//                if (chunk.size() > 0) {
//                    return new AggregatedPageImpl<>((List<T>) chunk);
//                }
//                return null;
//            }
//        });



//        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(keyWord, "title","content").fields(searchMap);
//        query.minimumShouldMatch("90%");
//        Page<EsArticle> page = articleEsRepository.search(query,pageable);
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
}
