package org.naruto.framework.user.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.naruto.framework.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends CustomRepository<User,String> {
//    public List<User> getUsersByMobile(String mobile);

    public User getUserByMobile(String mobile);

    public User getUserByWeiboUid(String weibo);

    public List<User> getUsersByNickname(String nickname);

}
