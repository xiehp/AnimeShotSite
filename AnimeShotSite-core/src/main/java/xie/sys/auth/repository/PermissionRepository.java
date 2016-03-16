package xie.sys.auth.repository;

import java.util.List;

import xie.base.repository.BaseRepository;
import xie.sys.auth.entity.Permission;


public interface PermissionRepository extends BaseRepository<Permission, String> {

	Permission findByPermissionAndResourceId(String permissionString, String resourceId);

	List<Permission> findByResourceId(String resourceId);

}
