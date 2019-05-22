package org.naruto.framework.user.repository;

import org.naruto.framework.user.domain.Permission;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PermissionReponsitory extends CrudRepository<Permission,String>{

    List<Permission> getPermissionsByOrderBySeqAsc();
}
