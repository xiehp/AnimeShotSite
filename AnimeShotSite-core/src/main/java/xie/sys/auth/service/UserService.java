package xie.sys.auth.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import com.google.common.collect.Maps;

import xie.base.page.PageRequestUtil;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.Constants;
import xie.common.utils.Validator;
import xie.sys.auth.entity.Resource;
import xie.sys.auth.entity.Role;
import xie.sys.auth.entity.RoleResourcePermission;
import xie.sys.auth.entity.User;
import xie.sys.auth.entity.UserRole;
import xie.sys.auth.entity.UserSetting;
import xie.sys.auth.repository.ResourceRepository;
import xie.sys.auth.repository.RoleRepository;
import xie.sys.auth.repository.RoleResourcePermissionRepository;
import xie.sys.auth.repository.UserRepository;
import xie.sys.auth.repository.UserRoleRepository;
import xie.sys.auth.repository.UserSettingRepository;
import xie.sys.auth.service.realm.ShiroRDbRealm.ShiroUser;
import xie.sys.role.vo.RoleVo;
import xie.sys.user.vo.UserVo;


@Service
public class UserService extends BaseService<User, String> {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired 
	private ResourceRepository resourceRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private RoleResourcePermissionRepository roleResourcePermissionRepository;
	@Autowired
	private UserSettingRepository userSettingRepository;
	
	@Override
	public BaseRepository<User, String> getBaseRepository() {
		return userRepository;
	}

	public User addUser(UserVo userVo) throws Exception {
		
		User user = new User();
		user.setLoginName(userVo.getLoginName());
		user.setUserName(userVo.getUserName());
		user.setPlainPassword(userVo.getPlainPassword());
		user.setMobile(userVo.getMobile());
		user.setEmail(userVo.getEmail());
		user.setStatus(Constants.USER_STATUS_NORMAL);
		user.setUserType(Constants.USER_TYPE_BACKGROUND);
		
		// 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			entryptPassword(user);
		}
		save(user);
		//保存用户和角色关系
		if(userVo.getRoleList() == null || userVo.getRoleList().size() == 0) {
			throw new Exception();
		}
		for(String roleId : userVo.getRoleList()) {
			UserRole ur = new UserRole();
			ur.setUserId(user.getId());
			ur.setRoleId(roleId);
			userRoleRepository.save(ur);
		}
		//保存用户设置
		UserSetting setting = new UserSetting();
		setting.setShowSidebar(Constants.FLAG_INT_YES);
		setting.setSkinPath(Constants.SKIN_DEFAULT);
		setting.setUserId(user.getId());
		userSettingRepository.save(setting);
		return user;
	}

	public User addEnterpriceUser(UserVo userVo) throws Exception {

		User user = new User();
		user.setLoginName(userVo.getLoginName());
		user.setUserName(userVo.getUserName());
		user.setPlainPassword(userVo.getPlainPassword());
		user.setMobile(userVo.getMobile());
		user.setEmail(userVo.getEmail());
		user.setStatus(Constants.USER_STATUS_LOCK);
		user.setUserType(Constants.USER_TYPE_FOREGROUND);

		// 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			entryptPassword(user);
		}
		user = save(user);

		return user;
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	private void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}

	public User findUserByLoginName(String loginName) {
		User user = userRepository.findByLoginNameAndDeleteFlag(loginName, Constants.FLAG_INT_NO);
		return user;
	}

	public Page<UserVo> searchAllUsers(Map<String, Object> searchParams,
			int pageNumber, int defaultPageSize, String sortType) throws Exception {
		//创建分页对象
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, defaultPageSize, sortType);
		
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);
		Page<User> userList = userRepository.findAll(spec, pageRequest);
		
		List<UserVo> volist = wrapUserVoList(userList.getContent());
		
		Page<UserVo> voPage = new PageImpl<UserVo>(volist, new PageRequest(userList.getNumber(), userList.getSize(), userList.getSort()), userList.getTotalElements());
		
		return voPage;
	}
	
	public List<UserVo> wrapUserVoList(List<User> list){
		List<UserVo> volist = Lists.newArrayList();
		for(User user: list) {
			UserVo vo = new UserVo();
			vo.setId(user.getId());
			vo.setUserName(user.getUserName());
			vo.setLoginName(user.getLoginName());
			vo.setMobile(user.getMobile());
			vo.setEmail(user.getEmail());
			vo.setStatus(user.getStatus());
			volist.add(vo);
		}
		return volist;
	}

	public User findUserByLoginNameAndId(String loginName, String id) {
		User user = findUserByLoginName(loginName);
		if(user != null && !StringUtils.isEmpty(id) && id.equals(user.getId())){
			//验证id对应的用户和查询的用户是否为同一个用户
			return null;
		}
		return user;
	}

	public void updateUser(UserVo vo) throws Exception {
		if(StringUtils.isEmpty(vo.getId())) {
			throw new Exception();
		}
		//更新
		User user = findOne(vo.getId());
		user.setUserName(vo.getUserName());
		user.setMobile(vo.getMobile());
		user.setEmail(vo.getEmail());
		if(StringUtils.isNotBlank(vo.getPlainPassword())){
			//如果密码不为空,则更新密码
			user.setPlainPassword(vo.getPlainPassword());
			entryptPassword(user);
		}
		update(user);
		
		List<UserRole> userRoleList = userRoleRepository.findByUserId(vo.getId());
		userRoleRepository.delete(userRoleList);
		
		if(vo.getRoleList() != null && vo.getRoleList().size() > 0){
			for(String roleId : vo.getRoleList()){
				UserRole ur = new UserRole();
				ur.setUserId(user.getId());
				ur.setRoleId(roleId);
				userRoleRepository.save(ur);
			}
		}
	}

	public void changeUserStatus(String userId, int status) {
		User user = findOne(userId);
		user.setStatus(status);
		update(user);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> viewUser(String userId){
		Map<String, Object> resultMap = Maps.newHashMap();
		
		List<Role> roleList = IteratorUtils.toList(roleRepository.findAll().iterator());
		
		List<RoleVo> voList = BeanMapper.mapList(roleList, RoleVo.class);
		
		List<String> roleIdList = userRoleRepository.findRoleIdByUserId(userId);
		
		for(RoleVo vo : voList){
			for(String roleId : roleIdList){
				if(vo.getId().equals(roleId)){
					vo.setChecked(true);
				}
			}
		}
		
		resultMap.put("user", userRepository.findOne(userId));
		resultMap.put("roleList", voList);
		return resultMap;
	}

	public boolean validateUserResourcePermission(User user) {
		//查找用户当前的所有权限
		List<String> roleIdList = userRoleRepository.findRoleIdByUserId(user.getId());
		
		for (String roleId : roleIdList) {
			
			List<RoleResourcePermission> resourcePermissions = roleResourcePermissionRepository.findByRoleId(roleId);
			
			for (RoleResourcePermission roleResourcePermission : resourcePermissions) {
				Resource resource = resourceRepository.findOne(roleResourcePermission.getResourceId());
				if(resource.getResourceLevel() <= 2){
					continue;
				}
				if(roleResourcePermission.getPermissionString().contains(Constants.DEFAULT_BASIC_AVAILABLE_PERMISSION)){
					return true;
				}
			}
		}
		return false;
	}

	public void updateUserAndSetting(UserVo vo, String currUserId) throws Exception {
		if(Validator.isNull(vo.getId()) || !vo.getId().equals(currUserId)){
			throw new Exception();
		}
		
		User user = userRepository.findOne(vo.getId());
		user.setUserName(vo.getUserName());
		user.setEmail(vo.getEmail());
		user.setMobile(vo.getMobile());
		if(Validator.isNotNull(vo.getPlainPassword())){
			user.setPlainPassword(vo.getPlainPassword());
			entryptPassword(user);
		}
		update(user);
		
		UserSetting setting = userSettingRepository.findByUserId(currUserId);
		setting.setShowSidebar(vo.getShowSidebar());
		setting.setSkinPath(vo.getSkinPath());
		userSettingRepository.save(setting);
		
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		shiroUser.userName = vo.getUserName();
		shiroUser.showSidebar = vo.getShowSidebar();
		shiroUser.skinPath = vo.getSkinPath();
	}

	public List<String> getLoginNameList(String param) {
		return userRepository.getLoginNameList(param);
	}
	
}
