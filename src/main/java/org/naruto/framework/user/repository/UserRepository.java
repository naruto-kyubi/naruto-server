package org.naruto.framework.user.repository;

import org.naruto.framework.user.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends CrudRepository<User,String> {
//    public List<User> getUsersByMobile(String mobile);

    public User getUserByMobile(String mobile);
    public List<User> getUsersByNickname(String nickname);

}
