package org.naruto.framework.elasticsearch.article.service;

import org.naruto.framework.article.vo.ArticleVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ArticleEsService {
    public Page<ArticleVo> search(Map map);
}
