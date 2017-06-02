package xie.sys.auth.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.base.entity.BaseEntity;
@Entity
@Table(name = "sys_resource")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends BaseEntity{

	private static final long serialVersionUID = 4586913208805261255L;
	private String name;
	private String identity;
	private String url;
	private String parentId;
	private String parentIds;
	private Integer weight;
	private Integer isShow;
	private String iconPath;
	private Integer resourceLevel;
	
	public Integer getResourceLevel() {
		return resourceLevel;
	}
	public void setResourceLevel(Integer resourceLevel) {
		this.resourceLevel = resourceLevel;
	}
	public String getIconPath() {
		return iconPath;
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getIsShow() {
		return isShow;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	@Override
	public String toString() {
		return "Resource [name=" + name + ", identity=" + identity + ", url="
				+ url + ", parentId=" + parentId + ", parentIds=" + parentIds
				+ ", weight=" + weight + ", isShow=" + isShow + ", iconPath="
				+ iconPath + ", resourceLevel=" + resourceLevel + "]";
	}
	
}
