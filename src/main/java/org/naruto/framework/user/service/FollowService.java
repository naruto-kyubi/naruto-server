package org.naruto.framework.user.service;

import org.naruto.framework.user.domain.Follow;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface FollowService {


    Follow query(String userId,String followUserId);

    Follow save(Follow follow);

    void delete(String userId,String followUserId);

    List<Follow> queryByUserId(String userId);

    List<Follow> queryByFollowUserId(String followUserId);

    Page queryUserByPage(Map map);
}
