package org.naruto.framework.article.repository;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ArticleRepository extends CustomRepository<Article,String> {

    @Modifying
    @Query(nativeQuery = true, value = "update articles set view_count = view_count + 1 where id=?1")
    void increaseViewCount(String articleId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "update articles set like_count = like_count + ?2 where id=?1")
    void increaseLikeCount(String articleId, Integer step);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update articles set star_count = star_count + ?2 where id=?1")
    void increaseStarCount(String articleId, Integer step);

}