package xie.sys.auth.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import xie.base.entity.BaseEntity;

/**
 * 用户操作记录实体
 * @author zhiqiang
 *
 */
@Entity
@Table(name="sys_user_oper")
public class UserOper extends BaseEntity{

	private String className;
	private String methodName;
	private String url;
	private String params;
	
	private String ipAddress;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	@Override
	public String toString() {
		return "UserOper [className=" + className + ", methodName="
				+ methodName + ", url=" + url + ", params=" + params
				+ ", ipAddress=" + ipAddress + "]";
	}
	
}
