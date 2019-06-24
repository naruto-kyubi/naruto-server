package org.naruto.framework.user.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.naruto.framework.user.domain.Follow;

import java.util.List;


public interface FollowRepository extends CustomRepository<Follow,String> {

    Follow queryFollowByUserIdAndFollowUserId(String userId,String followUserId);

    void deleteByUserIdAndFollowUserId(String userId,String followUserId);

    List<Follow> queryFollowsByUserId(String userId);

    List<Follow> queryFollowsByFollowUserId(String followUserId);
}
