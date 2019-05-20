package org.naruto.framework.user.repository;

import org.naruto.framework.user.domain.Perm;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PermReponsitory extends CrudRepository<Perm,String>{
    List<Perm> getPermsByOrderBySeqAsc();
}
