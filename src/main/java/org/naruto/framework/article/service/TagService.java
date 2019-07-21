package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Tag;
import org.naruto.framework.article.domain.UserTag;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TagService {

    UserTag save(UserTag userTag);

    Page<Tag> queryByPage(Map map);

    Page<Map> queryUserTags(String userId,String currentUserId,Map map);

    Page<Tag> queryUserTags(String userId, Map map);

    Page<Map> queryTags(String userId, Map map);

    void deleteUserTags(String userId,String tagId);

}
