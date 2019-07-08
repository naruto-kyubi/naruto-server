package org.naruto.framework.elasticsearch.user.service;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.naruto.framework.core.elasticsearch.HighLightResultMapper;
import org.naruto.framework.elasticsearch.user.domain.EsUser;
import org.naruto.framework.user.vo.UserVo;
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
import java.util.List;
import java.util.Map;

@Service
public class UserEsServiceImpl implements UserEsService{

    @Resource
    private HighLightResultMapper highLightResultMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Page<UserVo> search(Map map) {

        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        String keyWord = (String) map.get("keyword");

        String preTag = "<font color='#dd4b39'>";//google的色值
        String postTag = "</font>";

        SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withQuery(QueryBuilders.multiMatchQuery(keyWord, "nickname","profile")).
                withHighlightFields(
                        new HighlightBuilder.Field("nickname").preTags(preTag).postTags(postTag),
                        new HighlightBuilder.Field("profile").preTags(preTag).postTags(postTag)
                ).withMinScore(0.4F).
                build();
        searchQuery.setPageable(pageable);
        Page<EsUser> page = elasticsearchTemplate.queryForPage(searchQuery, EsUser.class, highLightResultMapper);
        List<EsUser> userList = page.getContent();

        List voList = ObjUtils.transformerClass(userList, UserVo.class);
        return new PageImpl(voList,page.getPageable(),page.getTotalElements());
    }
}