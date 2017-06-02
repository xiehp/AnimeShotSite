package xie.sys.auth.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.base.entity.BaseEntity;

@Entity
@Table(name = "sys_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Permission extends BaseEntity{

	private static final long serialVersionUID = 4314738151344311006L;
	private String name;
	private String permission;
	private String description;
	private Integer isShow;
	private String resourceId;
	
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
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

	@Override
	public String toString() {
		return "Permission [name=" + name + ", permission=" + permission
				+ ", description=" + description + ", isShow=" + isShow
				+ ", resourceId=" + resourceId + "]";
	}

}
