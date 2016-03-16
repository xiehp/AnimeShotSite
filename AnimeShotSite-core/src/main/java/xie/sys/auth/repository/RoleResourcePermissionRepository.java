package xie.sys.auth.repository;

import java.util.List;

import xie.base.repository.BaseRepository;
import xie.sys.auth.entity.RoleResourcePermission;

public interface RoleResourcePermissionRepository extends BaseRepository<RoleResourcePermission, String> {

	List<RoleResourcePermission> findByRoleId(String roleId);

}
