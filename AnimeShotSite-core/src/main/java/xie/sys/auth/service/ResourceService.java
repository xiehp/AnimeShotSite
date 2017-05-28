package xie.sys.auth.service;

import java.util.*;

import org.apache.commons.collections.IteratorUtils;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import com.google.common.collect.Sets;

import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.Constants;
import xie.sys.auth.entity.*;
import xie.sys.auth.entity.tmp.Menu;
import xie.sys.auth.repository.*;
import xie.sys.role.vo.PermissionVo;
import xie.sys.role.vo.ResourceVo;

@Service
public class ResourceService extends BaseService<Resource, String> {

	@javax.annotation.Resource
	private ResourceRepository resourceRepository;
	@javax.annotation.Resource
	private UserRepository userRepository;
	@javax.annotation.Resource
	private PermissionRepository permissionRepository;
	@javax.annotation.Resource
	private RoleRepository roleRepository;
	@javax.annotation.Resource
	private RoleResourcePermissionRepository roleResourcePermissionRepository;
	@javax.annotation.Resource
	private UserRoleRepository userRoleRepository;

	public ResourceService() {
	}

	@Override
	public BaseRepository<Resource, String> getBaseRepository() {
		return resourceRepository;
	}
	

	@SuppressWarnings("unchecked")
	public List<Menu> findMenus(String userId) {
		
		User user = userRepository.findOne(userId);
		
		Iterable<Resource> resourceList = resourceRepository.findAll(new Sort(Sort.Direction.DESC, "weight"));

		Set<String> userPermissions = findStringPermissions(user);

		Iterator<Resource> iter = resourceList.iterator();
		while (iter.hasNext()) {
			Resource resource = iter.next();
			if (!hasPermission(resource, userPermissions)) {
				iter.remove();
			}
		}
		return convertToMenus(IteratorUtils.toList(resourceList.iterator()));
	}

	@SuppressWarnings("unchecked")
	public static List<Menu> convertToMenus(List<Resource> resources) {
		if (resources.size() == 0) {
			return Collections.EMPTY_LIST;
		}

		Menu root = convertToMenu(resources.remove(resources.size() - 1));

		recursiveMenu(root, resources);
		List<Menu> menus = root.getChildren();
		removeNoLeafMenu(menus);

		return menus;
	}

	private boolean hasPermission(Resource resource, Set<String> userPermissions) {
		String actualResourceIdentity = findActualResourceIdentity(resource);
		if (StringUtils.isEmpty(actualResourceIdentity)) {
			return true;
		}

		for (String permission : userPermissions) {
			if (hasPermission(permission, actualResourceIdentity)) {
				return true;
			}
		}

		return false;
	}

	private boolean hasPermission(String permission,
			String actualResourceIdentity) {

		// 得到权限字符串中的 资源部分，如a:b:create --->资源是a:b
		String permissionResourceIdentity = permission.substring(0,
				permission.lastIndexOf(":"));

		// 如果权限字符串中的资源 是 以资源为前缀 则有权限 如a:b 具有a:b的权限
		if (permissionResourceIdentity.startsWith(actualResourceIdentity)) {
			return true;
		}

		// 模式匹配
		WildcardPermission p1 = new WildcardPermission(
				permissionResourceIdentity);
		WildcardPermission p2 = new WildcardPermission(actualResourceIdentity);

		return p1.implies(p2) || p2.implies(p1);
	}
	
	public Set<String> findStringPermissions(User user) {
		Set<String> permissions = Sets.newHashSet();
		
		List<String> roleIds = userRoleRepository.findRoleIdByUserId(user.getId());
		
		for (String roleId : roleIds) {
			
			List<RoleResourcePermission> roleResourcePermissionList = roleResourcePermissionRepository.findByRoleId(roleId);
			
			for (RoleResourcePermission rrp : roleResourcePermissionList) {
				Resource resource = findOne(rrp.getResourceId());
				
				if(resource.getResourceLevel() <= Constants.DEFAULT_RESOURCE_AVAILABLE_LEVEL){
					//默认过滤掉1,级菜单
					continue;
				}
				if(rrp.getPermissionString().indexOf(Constants.DEFAULT_BASIC_AVAILABLE_PERMISSION) == -1){
					//如果对资源没有查看权限,则不显示菜单
					continue;
				}
				
				String actualResourceIdentity = findActualResourceIdentity(resource);
				
				//不可用 即没查到 或者标识字符串不存在
                if (resource == null || StringUtils.isEmpty(actualResourceIdentity) || Constants.FLAG_INT_NO == resource.getIsShow()) {
                    continue;
                }
                for (String permissionString : rrp.getPermissionString().split(";")) {
                    Permission permission = permissionRepository.findByPermissionAndResourceId(permissionString, resource.getId());

                    //不可用
                    if (permission == null || Constants.FLAG_INT_NO == permission.getIsShow()) {
                        continue;
                    }
                    permissions.add(actualResourceIdentity + ":" + permission.getPermission());
                }
			}
		}

		return permissions;
	}

	public void save(Collection<Resource> list) {
		for (Resource resource : list) {
			save(resource);
		}
	}

	public Resource findOne(String id) {
		if(StringUtils.isEmpty(id)) return null;
		return resourceRepository.findOne(id);
	}

	public String findActualResourceIdentity(Resource resource) {
		if (resource == null || StringUtils.isEmpty(resource.getIdentity())) {
			return null;
		}

		StringBuilder s = new StringBuilder(resource.getIdentity());

//		boolean hasResourceIdentity = !StringUtils.isEmpty(resource
//				.getIdentity());
//
//		Resource parent = findOne(resource.getParentId());
//
//		while (parent != null) {
//			if (!StringUtils.isEmpty(parent.getIdentity())) {
//				s.insert(0, parent.getIdentity() + ":");
//				hasResourceIdentity = true;
//			}
//			parent = findOne(parent.getParentId());
//		}
//
//		// 如果用户没有声明 资源标识 且父也没有，那么就为空
//		if (!hasResourceIdentity) {
//			return "";
//		}
//
//		// 如果最后一个字符是: 因为不需要，所以删除之
//		int length = s.length();
//		if (length > 0 && s.lastIndexOf(":") == length - 1) {
//			s.deleteCharAt(length - 1);
//		}
//
//		// 如果有儿子 最后拼一个*
//		boolean hasChildren = false;
//		for (Resource r : findAll()) {
//			if (resource.getId().equals(r.getParentId())) {
//				hasChildren = true;
//				break;
//			}
//		}
//		if (hasChildren) {
//			s.append(":*");
//		}

		return s.toString();
	}

	private static void removeNoLeafMenu(List<Menu> menus) {
		if (menus.size() == 0) {
			return;
		}
		for (int i = menus.size() - 1; i >= 0; i--) {
			Menu m = menus.get(i);
			removeNoLeafMenu(m.getChildren());
			if (!m.isHasChildren() && StringUtils.isEmpty(m.getUrl())) {
				menus.remove(i);
			}
		}
	}

	private static void recursiveMenu(Menu menu, List<Resource> resources) {
		for (int i = resources.size() - 1; i >= 0; i--) {
			Resource resource = resources.get(i);
			if (resource.getParentId().equals(menu.getId())) {
				menu.getChildren().add(convertToMenu(resource));
				resources.remove(i);
			}
		}

		for (Menu subMenu : menu.getChildren()) {
			recursiveMenu(subMenu, resources);
		}
	}

	private static Menu convertToMenu(Resource resource) {
		return new Menu(resource.getId(), resource.getName(), 
				resource.getUrl(), resource.getIconPath(), 
				resource.getIdentity(), resource.getResourceLevel());
	}

	@SuppressWarnings("unchecked")
	public List<Resource> findAll() {
		Iterable<Resource> resourceList = resourceRepository.findAll();
		return IteratorUtils.toList(resourceList.iterator());
	}

	public Map<String, Object> loadRolePermissions(String roleId) {
		//最终权限列表
		List<ResourceVo> finalResourceList = Lists.newArrayList();
		
		Role role = roleRepository.findOne(roleId);
		Set<String> permissionIds = convertPermissionIds(roleResourcePermissionRepository.findByRoleId(roleId));
		
		List<Resource> allVisiableResource = findAllVisiableResource();
		
		for(Resource resource : allVisiableResource){
			ResourceVo resourceVo = BeanMapper.map(resource, ResourceVo.class);
			
			List<Permission> permissionList = permissionRepository.findByResourceId(resource.getId());
			
			for(Permission permission : permissionList){
				PermissionVo permissionVo = BeanMapper.map(permission, PermissionVo.class);
				for(String permissionId : permissionIds){
					if(permissionId.equals(permissionVo.getId())){
						permissionVo.setPermitted(true);
					}
				}
				resourceVo.getPermissionList().add(permissionVo);
			}
			finalResourceList.add(resourceVo);
		}
		
		Map<String, Object> rolePermission = Maps.newHashMap();
		rolePermission.put("resourceList", finalResourceList);
		rolePermission.put("role", role);
		
		return rolePermission;
	}
	
	//过滤掉1,2级资源,不需要显示
	private List<Resource> findAllVisiableResource(){
		Map<String, Object> searchParams = Maps.newHashMap();
		
		searchParams.put(Operator.GT + "_resourceLevel", Constants.DEFAULT_RESOURCE_AVAILABLE_LEVEL+"");
		searchParams.put(Operator.EQ + "_isShow", Constants.FLAG_INT_YES+"");
		
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<Resource> spec = DynamicSpecifications.bySearchFilter(filters.values(), Resource.class);
		
		return resourceRepository.findAll(spec);
	}

	private Set<String> convertPermissionIds(List<RoleResourcePermission> resourcePermissions) {
		
		Set<String> permissionIds = Sets.newHashSet();
		
		for (RoleResourcePermission resourcePermission : resourcePermissions) {
			String[] permissions = resourcePermission.getPermissionString().split(";");
			for (String permission : permissions) {
				Permission p = permissionRepository.findByPermissionAndResourceId(permission, resourcePermission.getResourceId());
				if(p != null){
					permissionIds.add(p.getId());
				}
			}
		}
		return permissionIds;
	}

	
}
