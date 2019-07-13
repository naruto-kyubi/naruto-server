package org.naruto.framework.elasticsearch.search.service;

import org.naruto.framework.elasticsearch.article.domain.EsArticle;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface SearchEsService {
    Page<Map> searchMutiIndices(Map map);

    Page<EsArticle> searchLikeThis(Map map);
}
