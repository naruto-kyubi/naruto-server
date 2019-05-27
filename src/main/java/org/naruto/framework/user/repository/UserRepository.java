package org.naruto.framework.user.repository;

import org.naruto.framework.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User,String>,JpaSpecificationExecutor<User> {
//    public List<User> getUsersByMobile(String mobile);

    public User getUserByMobile(String mobile);

    public User getUserByWeibo(String weibo);

    public List<User> getUsersByNickname(String nickname);

}
