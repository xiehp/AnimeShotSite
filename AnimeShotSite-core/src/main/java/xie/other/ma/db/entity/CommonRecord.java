package xie.other.ma.db.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.base.entity.BaseEntity;

/**
 * 通用记录
 */
@Entity
@Table(name = CommonRecord.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommonRecord extends BaseEntity {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "common_record";

	public static final String COLUMN_COOKIE_ID = "cookieId";
	public static final String COLUMN_TYPE = "type";

	public static final String TYPE_MA = "ma";

	/** 评论人ID 空则匿名 */
	private String userId;

	/** 评论人名字 空则匿名 */
	private String userName;

	/** 没有用户ID则记录cookieId */
	private String cookieId;

	/** 记录类型 */
	private String type;
	/** 记录名称 */
	private String name;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCookieId() {
		return cookieId;
	}

	public void setCookieId(String cookieId) {
		this.cookieId = cookieId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
