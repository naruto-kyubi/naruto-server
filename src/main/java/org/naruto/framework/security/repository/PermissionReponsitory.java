package org.naruto.framework.security.repository;

import org.naruto.framework.security.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface PermissionReponsitory extends JpaRepository<Permission,String> {

    List<Permission> getPermissionsByOrderBySeqAsc();


    @Query(value="SELECT basic_menu.* FROM basic_menu ,basic_user,basic_user_role,basic_role_menu where basic_user.id=basic_user_role.user_id and basic_user_role.role_id=basic_role_menu.role_id and basic_role_menu.menu_id=basic_menu.id and  basic_user.id=?1",nativeQuery = true)
    public List<Map> queryMenuList(String userId);
}
