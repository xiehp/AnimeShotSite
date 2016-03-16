package xie.sys.auth.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.base.entity.IdEntity;

@Entity
@Table(name = "sys_user_setting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserSetting extends IdEntity {
	
	private Integer showSidebar;
	private String skinPath;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getShowSidebar() {
		return showSidebar;
	}
	public void setShowSidebar(Integer showSidebar) {
		this.showSidebar = showSidebar;
	}
	public String getSkinPath() {
		return skinPath;
	}
	public void setSkinPath(String skinPath) {
		this.skinPath = skinPath;
	}
	
}
