package xie.sys.auth.service;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.sys.auth.entity.Permission;
import xie.sys.auth.repository.PermissionRepository;

@Service
public class PermissionService extends BaseService<Permission, String>{

	@Autowired
	private PermissionRepository permissionRepository;
	
	@Override
	public BaseRepository<Permission, String> getBaseRepository() {
		return permissionRepository;
	}
	
	public Permission update(Permission permission){
		permission.setUpdateDate(Calendar.getInstance().getTime());
		return permissionRepository.save(permission);
	}
	
	public void save(Collection<Permission> list){
		for (Permission permission : list) {
			save(permission);
		}
	}

	public Permission findByPermission(String permissionString, String resourceId) {
		return permissionRepository.findByPermissionAndResourceId(permissionString, resourceId);
	}

	
	
}
