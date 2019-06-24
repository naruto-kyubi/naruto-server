package org.naruto.framework.user.service;

import org.naruto.framework.user.domain.Follow;
import org.naruto.framework.user.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FollowServiceImp implements FollowService {

    @Autowired
    private FollowRepository followRepository;

    public Follow query(String userId,String followUserId){
       return followRepository.queryFollowByUserIdAndFollowUserId(userId,followUserId);
    }

    public Follow save(Follow follow){
        return followRepository.save(follow);
    }

    public void delete(String userId,String followUserId){
        followRepository.deleteByUserIdAndFollowUserId(userId,followUserId);
    }
}
