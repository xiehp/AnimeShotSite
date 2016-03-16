package xie.sys.role.vo;

import java.util.Set;

import xie.base.vo.BaseVo;

public class RoleVo extends BaseVo {

	private String name;
	private String role;
	private String description;
	private Integer isShow;
	private Integer status;
	private boolean isChecked = false;
	public boolean getIsChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	private Set<String> permissionIds;
	
	public Set<String> getPermissionIds() {
		return permissionIds;
	}
	public void setPermissionIds(Set<String> permissionIds) {
		this.permissionIds = permissionIds;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getIsShow() {
		return isShow;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
