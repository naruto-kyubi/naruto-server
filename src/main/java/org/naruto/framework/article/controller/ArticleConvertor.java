package org.naruto.framework.article.controller;

import org.dozer.CustomConverter;
import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.vo.ArticleVo;

public class ArticleConvertor implements CustomConverter {
    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
        if (sourceFieldValue == null)
            return null;
        return (sourceFieldValue.toString() + "aaa");
    }
}
