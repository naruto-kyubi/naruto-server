package org.naruto.framework.elasticsearch.article.repository;

import org.naruto.framework.elasticsearch.article.domain.EsArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArticleEsRepository extends ElasticsearchRepository<EsArticle,String> {
    List<EsArticle> findArticlesByTitleLike(String title);
}
