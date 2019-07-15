package org.naruto.framework.article.repository;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends CustomRepository<Article,String> {

//    @Query(value = "select ar.id," +
//            "ar.title," +
//            "ar.cover," +
//            "ar.content," +
//            "ar.content_html as contentHtml," +
//            "ar.updatedAt," +
//            "ar.owner " +
//            "from articles ar,follows fw where ar.owner = fw.follow_user_id and fw.user_id=?1",
//            countQuery="select count(*) from articles ar,follows fw where ar.owner = fw.follow_user_id and fw.user_id=?1",
//            nativeQuery = true)


    @Query(value="select ar from Article ar,Follow fw where ar.owner=fw.followUser and fw.user.id=?1",
            countQuery="select count(ar) from Article ar,Follow fw where ar.owner=fw.followUser and fw.user.id=?1")
    Page<Article> queryArticlesByFollows(String userId, Pageable pageable);
}