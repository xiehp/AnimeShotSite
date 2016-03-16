package xie.sys.role.vo;

import java.util.ArrayList;
import java.util.List;

import xie.base.vo.BaseVo;

public class ResourceVo extends BaseVo {

	private String name;
	
	private List<PermissionVo> permissionList = new ArrayList<PermissionVo>();

	public List<PermissionVo> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<PermissionVo> permissionList) {
		this.permissionList = permissionList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ResourceVo [name=" + name + ", permissionList="
				+ permissionList + "]";
	}
}
