package xie.sys.auth.service.realm;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springside.modules.utils.Encodes;

import com.google.common.base.Objects;

import xie.common.Constants;
import xie.common.exception.NoPermissionException;
import xie.sys.auth.entity.User;
import xie.sys.auth.entity.UserSetting;
import xie.sys.auth.service.ResourceService;
import xie.sys.auth.service.RoleService;
import xie.sys.auth.service.UserService;
import xie.sys.auth.service.UserSettingService;

public class ShiroRDbRealm extends AuthorizingRealm {
	
	protected UserService userService;
	
	protected UserSettingService userSettingService;
	
	protected ResourceService resourceService;

	protected RoleService roleService;

	public void setUserSettingService(UserSettingService userSettingService) {
		this.userSettingService = userSettingService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		User user = userService.findUserByLoginName(shiroUser.loginName);
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setRoles(roleService.findStringRoles(user));
		authorizationInfo.setStringPermissions(resourceService.findStringPermissions(user));
		
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username = token.getUsername().trim();
        User user = userService.findUserByLoginName(username);
		
        if(user != null){
        	if(user.getStatus() != Constants.USER_STATUS_NORMAL){
        		throw new DisabledAccountException();
        	}
        	//判断用户是否有可查看的权限
        	boolean permissionFlag = userService.validateUserResourcePermission(user);
        	if(!permissionFlag){
        		throw new NoPermissionException();
        	}
        	
        	UserSetting setting = userSettingService.findByUserId(user.getId());
        	
        	byte[] salt = Encodes.decodeHex(user.getSalt());
        	return new SimpleAuthenticationInfo(new ShiroUser(user.getId(), user.getLoginName(), user.getUserName(), user.getEmail(), user.getMobile(), setting.getShowSidebar(), setting.getSkinPath()), user.getPassword(),
					ByteSource.Util.bytes(salt), getName());
        }else {
        	throw new UnknownAccountException();
        }
	}
	
	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(UserService.HASH_ALGORITHM);
		matcher.setHashIterations(UserService.HASH_INTERATIONS);

		setCredentialsMatcher(matcher);
	}
	
	private static final String OR_OPERATOR = " or ";
    private static final String AND_OPERATOR = " and ";
    private static final String NOT_OPERATOR = "not ";
    
    /**
     * 支持or and not 关键词  不支持and or混用
     *
     * @param principals
     * @param permission
     * @return
     */
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        if (permission.contains(OR_OPERATOR)) {
            String[] permissions = permission.split(OR_OPERATOR);
            for (String orPermission : permissions) {
                if (isPermittedWithNotOperator(principals, orPermission)) {
                    return true;
                }
            }
            return false;
        } else if (permission.contains(AND_OPERATOR)) {
            String[] permissions = permission.split(AND_OPERATOR);
            for (String orPermission : permissions) {
                if (!isPermittedWithNotOperator(principals, orPermission)) {
                    return false;
                }
            }
            return true;
        } else {
            return isPermittedWithNotOperator(principals, permission);
        }
    }

    private boolean isPermittedWithNotOperator(PrincipalCollection principals, String permission) {
        if (permission.startsWith(NOT_OPERATOR)) {
            return !super.isPermitted(principals, permission.substring(NOT_OPERATOR.length()));
        } else {
            return super.isPermitted(principals, permission);
        }
    }
	
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -1373760761780840081L;
		public String id;
		public String loginName;
		public String userName;
		public String email;
		public String mobile;
		public Integer showSidebar;
		public String skinPath;

		public ShiroUser(String id, String loginName, String userName, String email, String mobile, Integer showSidebar, String skinPath){
			this.id = id;
			this.loginName = loginName;
			this.userName = userName;
			this.email = email;
			this.mobile = mobile;
			this.showSidebar = showSidebar;
			this.skinPath = skinPath;
		}
		
		public String getShowSidebar() {
			if(showSidebar == 0){
				//隐藏
				return "page-sidebar-closed";
			}
			return "";
		}

		public String getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getUserName() {
			return userName;
		}

		public String getEmail() {
			return email;
		}

		public String getMobile() {
			return mobile;
		}

		public String getSkinPath() {
			return skinPath;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return "loginName:" + loginName + ", userName:" + userName + ", id:" + id;
		}

		/**
		 * 重载hashCode,只计算loginName;
		 */
		@Override
		public int hashCode() {
			return Objects.hashCode(loginName);
		}

		/**
		 * 重载equals,只计算loginName;
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ShiroUser other = (ShiroUser) obj;
			if (loginName == null) {
				if (other.loginName != null) {
					return false;
				}
			} else if (!loginName.equals(other.loginName)) {
				return false;
			}
			return true;
		}
	}

}
