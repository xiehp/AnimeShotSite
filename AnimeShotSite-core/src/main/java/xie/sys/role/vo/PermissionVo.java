package xie.sys.role.vo;

import xie.base.vo.BaseVo;

public class PermissionVo extends BaseVo {

	private String name;
	private boolean permitted = false;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getIsPermitted() {
		return permitted;
	}
	public void setPermitted(boolean permitted) {
		this.permitted = permitted;
	}
	@Override
	public String toString() {
		return "PermissionVo [name=" + name + ", permitted=" + permitted + "]";
	}
	
}
