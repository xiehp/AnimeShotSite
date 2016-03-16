package xie.sys.user.vo;

import java.util.Map;
import java.util.Set;

import org.assertj.core.util.Sets;

import xie.base.vo.BaseVo;
import xie.common.Constants;
import xie.sys.dictionary.common.DictionaryConstants;
import xie.sys.dictionary.utils.PublicDictionaryLoader;

public class UserVo extends BaseVo{
	private String loginName;
	private String plainPassword;
	private String email;
	private String mobile;
	private String userName;
	private Integer status;
	private Set<String> roleList = Sets.newHashSet();
	private Integer showSidebar;
	private String skinPath;
	public String getSkinPath() {
		return skinPath;
	}

	public void setSkinPath(String skinPath) {
		this.skinPath = skinPath;
	}

	public Integer getShowSidebar() {
		return showSidebar;
	}

	public void setShowSidebar(Integer showSidebar) {
		this.showSidebar = showSidebar;
	}

	public String getUserStatus(){
		Map<Integer, String> intMap = PublicDictionaryLoader.getIntMap(DictionaryConstants.TYPE_ID_USER_STATUS);
		return intMap.get(status);
	}
	
	public Set<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(Set<String> roleList) {
		this.roleList = roleList;
	}

	public boolean getAdminFlag(){
		return Constants.USER_MANAGE_LOGIN_ID.equals(this.loginName);
	}
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPlainPassword() {
		return plainPassword;
	}
	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "UserVo [loginName=" + loginName + ", plainPassword="
				+ plainPassword + ", email=" + email + ", mobile=" + mobile
				+ ", userName=" + userName + "]";
	}
}
