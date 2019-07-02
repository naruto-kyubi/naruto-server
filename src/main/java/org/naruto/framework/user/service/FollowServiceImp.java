package org.naruto.framework.user.service;

import org.naruto.framework.user.domain.Follow;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.repository.FollowRepository;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
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

        User followUser = follow.getFollowUser();
        Follow f = followRepository.queryFollowByUserIdAndFollowUserId(follow.getFollowUser().getId(),follow.getUser().getId());

        if(null==f){
            //only one follow；
            follow.setMutual("follow");
        }else{
            //both
            f.setMutual("both");
            followRepository.save(f);
            follow.setMutual("both");
        }
        return followRepository.save(follow);
    }

    public void delete(String userId,String followUserId){
        Follow f = followRepository.queryFollowByUserIdAndFollowUserId(followUserId,userId);
        if(null!=f){
            f.setMutual("follow");
            followRepository.save(f);
        }
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

    @Override
    public Page queryFollowByPage(Map map) {
        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
//        _map = PageUtils.clearPaginationArgs(_map);
        return followRepository.findAll(createQuery(_map),pageable);
    }

    @Override
    public Page queryFollowByUserId(Map map) {
        String userId = (String) map.get("userId");
        String currentUserId = (String) map.get("currentUserId");
        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        return followRepository.findAll(userId,currentUserId,pageable);
    }

    @Override
    public Page queryFans(Map map) {
        //被关注者
        String followUserId = (String) map.get("followUserId");
        String currentUserId = (String) map.get("currentUserId");
        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);

        return followRepository.queryFans(followUserId,currentUserId,pageable);
    }

    public static <T> Specification<T> createQuery(Map map){
        String currentUserId = (String) map.get("currentUser.id");
        String userId = (String) map.get("user.id");

        return new Specification<T>(){
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
//                root.get("user.id");

                CriteriaQuery<Follow> cq = cb.createQuery(Follow.class);

                Join<Follow,Follow> join = root.join("followUser", JoinType.LEFT);

                Path<String> user = join.get("user").get("id");
                user.equals(userId);


                Root<Follow> f1 = cq.from(Follow.class);
                Root<Follow> f2 = cq.from(Follow.class);

                Predicate p1 = cb.equal(f1.get("user").get("id"), userId);
                Predicate p2 = cb.equal(f2.get("user").get("id"), currentUserId);

                Predicate criteria = cb.conjunction();
                criteria = cb.and(criteria, p1);
                criteria = cb.and(criteria, p2);

                cq.where(criteria);
                cq.select(f1).distinct(true);

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
