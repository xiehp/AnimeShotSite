package xie.sys.auth.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import xie.base.page.PageRequestUtil;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.Constants;
import xie.common.utils.Validator;
import xie.sys.auth.entity.Permission;
import xie.sys.auth.entity.Resource;
import xie.sys.auth.entity.Role;
import xie.sys.auth.entity.RoleResourcePermission;
import xie.sys.auth.entity.User;
import xie.sys.auth.repository.PermissionRepository;
import xie.sys.auth.repository.ResourceRepository;
import xie.sys.auth.repository.RoleRepository;
import xie.sys.auth.repository.RoleResourcePermissionRepository;
import xie.sys.auth.repository.UserRoleRepository;
import xie.sys.role.vo.RoleVo;

@Service
public class RoleService extends BaseService<Role,String> {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private ResourceRepository resourceRepository;
	@Autowired
	private RoleResourcePermissionRepository roleResourcePermissionRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Override
	public BaseRepository<Role, String> getBaseRepository() {
		return roleRepository;
	}

	public Page<Role> searchAllRoles(Map<String, Object> searchParams,
			int pageNumber, int defaultPageSize, String sortType) {
		//创建分页对象
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, defaultPageSize, sortType);
		
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<Role> spec = DynamicSpecifications.bySearchFilter(filters.values(), Role.class);
		Page<Role> roleList = roleRepository.findAll(spec, pageRequest);
		
		return roleList;
	}
	
	public Set<String> findStringRoles(User user) {
		
		List<String> roleIdList = userRoleRepository.findRoleIdByUserId(user.getId());
		
		Set<String> roleNameSet = Sets.newHashSet();
		
		for(String roleId: roleIdList) {
			Role role = roleRepository.findOne(roleId);
			roleNameSet.add(role.getRole());
		}
		return roleNameSet;
	}

	public void updateRoleResourcePermission(RoleVo roleVo) throws RuntimeException {
		//删除原来的权限记录
		deleteOldRoleResourcePermissions(roleVo.getId());
		
		//封装资源
		saveRoleResourcePermission(roleVo);
	}

	public void saveRoleResourcePermission(RoleVo roleVo) throws RuntimeException {
		if(roleVo.getPermissionIds().size() == 0){
			return;
		}
		
		List<String> parentIdList = Lists.newArrayList();
		
		Map<String, String> resourcePermissionMap = Maps.newHashMap();
		for(String permissionId : roleVo.getPermissionIds()){
			Permission permission = permissionRepository.findOne(permissionId);
			//此处需要把父节点找出来
			
			Resource resource = resourceRepository.findOne(permission.getResourceId());
			
			if(!StringUtils.isEmpty(resource.getParentId())){
				Resource parentResource = resourceRepository.findOne(resource.getParentId());
				if(!parentIdList.contains(parentResource.getId()) && parentResource.getResourceLevel() == Constants.DEFAULT_RESOURCE_AVAILABLE_LEVEL){
					List<Permission> permissionList = permissionRepository.findByResourceId(parentResource.getId());
					resourcePermissionMap.put(parentResource.getId(), convertPermissionToStr(permissionList));
					parentIdList.add(parentResource.getId());
				}
			}
			String permissionStr = resourcePermissionMap.get(resource.getId());
			resourcePermissionMap.put(resource.getId(), StringUtils.isEmpty(permissionStr) ? permission.getPermission() : permissionStr + ";" + permission.getPermission());
		}
		
		Role role = roleRepository.findOne(roleVo.getId());
		for (Map.Entry<String, String> entry: resourcePermissionMap.entrySet()) {
			RoleResourcePermission rrp = new RoleResourcePermission();
			rrp.setRoleId(role.getId());
			rrp.setResourceId(entry.getKey());
			rrp.setPermissionString(entry.getValue());
			roleResourcePermissionRepository.save(rrp);
		}
	}
	
	private String convertPermissionToStr(List<Permission> permissionList) {
		String permissionStr = "";
		for (Permission permission : permissionList) {
			permissionStr += StringUtils.isEmpty(permissionStr) ? permission.getPermission() : permissionStr + ";" + permission.getPermission();
		}
		return permissionStr;
	}

	public void deleteOldRoleResourcePermissions(String roleId) throws RuntimeException {
		
		List<RoleResourcePermission> rrpList = roleResourcePermissionRepository.findByRoleId(roleId);
		
		roleResourcePermissionRepository.delete(rrpList);
	}
	
	public void deleteRole(String roleId) {
		roleRepository.delete(roleId);
	}

	public void addRole(RoleVo roleVo) throws Exception {
		Role role = roleRepository.findByName(roleVo.getName());
		if(role != null){
			throw new Exception("角色名称已经存在!");
		}
		Role newRole = new Role();
		newRole.setName(roleVo.getName());
		newRole.setRole(roleVo.getRole());
		newRole.setDescription(roleVo.getDescription());
		newRole.setIsShow(Constants.IS_SHOW);
		newRole.setStatus(Constants.FLAG_INT_YES);
		roleRepository.save(newRole);
	}

	@SuppressWarnings("unchecked")
	public List<Role> findALlRoles() {
		return IteratorUtils.toList(roleRepository.findAll().iterator());
	}

}
