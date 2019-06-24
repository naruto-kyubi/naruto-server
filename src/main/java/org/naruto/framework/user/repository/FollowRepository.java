package org.naruto.framework.user.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.naruto.framework.user.domain.Follow;


public interface FollowRepository extends CustomRepository<Follow,String> {

    public Follow queryFollowByUserIdAndFollowUserId(String userId,String followUserId);

    public void deleteByUserIdAndFollowUserId(String userId,String followUserId);
}
