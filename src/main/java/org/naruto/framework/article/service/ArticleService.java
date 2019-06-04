package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.repository.ArticleRepository;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Article save(Article article){
        if(article == null) {
                throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }

        return articleRepository.save(article);
    }


    public Page<Article> queryPage(Map map) {
        return articleRepository.queryPageByCondition(map);
    }

    public Article queryById(String id){
        return  articleRepository.getOne(id);
    }
}
