package org.naruto.framework.user.service;

import org.naruto.framework.user.domain.Follow;
import org.naruto.framework.user.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    @Override
    public List<Follow> queryByUserId(String userId) {
        return followRepository.queryFollowsByUserId(userId);
    }

    @Override
    public List<Follow> queryByFollowUserId(String followUserId) {
        return followRepository.queryFollowsByFollowUserId(followUserId);
    }

    @Override
    public Page queryUserByPage(Map map) {
        return followRepository.queryPageByCondition(map);
    }
}
