package org.naruto.framework.user.service;

import org.naruto.framework.user.domain.Follow;


public interface FollowService {


    public Follow query(String userId,String followUserId);

    public Follow save(Follow follow);

    public void delete(String userId,String followUserId);
}
