package org.naruto.framework.user.repository;

import org.naruto.framework.user.domain.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User,String> {
}
