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

	/**
	 * 获取 评论人ID 空则匿名.
	 *
	 * @return 评论人ID 空则匿名
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置 评论人ID 空则匿名.
	 *
	 * @param userId 评论人ID 空则匿名
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取 评论人名字 空则匿名.
	 *
	 * @return 评论人名字 空则匿名
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置 评论人名字 空则匿名.
	 *
	 * @param userName 评论人名字 空则匿名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取 没有用户ID则记录cookieId.
	 *
	 * @return 没有用户ID则记录cookieId
	 */
	public String getCookieId() {
		return cookieId;
	}

	/**
	 * 设置 没有用户ID则记录cookieId.
	 *
	 * @param cookieId 没有用户ID则记录cookieId
	 */
	public void setCookieId(String cookieId) {
		this.cookieId = cookieId;
	}

	/**
	 * 获取 记录类型.
	 *
	 * @return 记录类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置 记录类型.
	 *
	 * @param type 记录类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取 记录名称.
	 *
	 * @return 记录名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置 记录名称.
	 *
	 * @param name 记录名称
	 */
	public void setName(String name) {
		this.name = name;
	}

}
