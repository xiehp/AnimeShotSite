package xie.other.ma.db.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.base.entity.BaseEntity;

/**
 * 评论记录
 */
@Entity
@Table(name = CommentRecord.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommentRecord extends BaseEntity {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "comment_record";

	public static final String COLUMN_CLASS1 = "class1";
	public static final String COLUMN_CLASS2 = "class2";
	public static final String COLUMN_TARGET_ID = "targetId";

	public static final String CLASS1_MA = "ma";
	public static final String CLASS2_MA_MAIN = "ma_main";
	public static final String CLASS2_MA_SINGLE = "ma_single";

	/** 评论人ID 空则匿名 */
	private String userId;

	/** 评论人名字 空则匿名 */
	private String userName;

	/** 没有用户ID则记录cookieId */
	private String cookieId;

	/** 评论所属对象ID */
	private String targetId;

	/** 评论所属对象其他信息1 */
	private String targetAttr1;

	/** 评论所属对象其他信息2 */
	private String targetAttr2;

	/** 评论所属对象其他信息3 */
	private String targetAttr3;

	/** 评论时间 */
	private Date commentDate;

	/** 评论内容 */
	private String content;

	/** 回复评论ID */
	private String replyCommentId;

	/** 分类1 */
	private String class1;

	/** 分类2 */
	private String class2;

	/** 分类3 */
	private String class3;

	/** 分类5 */
	private String class4;

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
	 * 获取 评论所属对象ID.
	 *
	 * @return 评论所属对象ID
	 */
	public String getTargetId() {
		return targetId;
	}

	/**
	 * 设置 评论所属对象ID.
	 *
	 * @param targetId 评论所属对象ID
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTargetAttr1() {
		return targetAttr1;
	}

	public void setTargetAttr1(String targetAttr1) {
		this.targetAttr1 = targetAttr1;
	}

	public String getTargetAttr2() {
		return targetAttr2;
	}

	public void setTargetAttr2(String targetAttr2) {
		this.targetAttr2 = targetAttr2;
	}

	public String getTargetAttr3() {
		return targetAttr3;
	}

	public void setTargetAttr3(String targetAttr3) {
		this.targetAttr3 = targetAttr3;
	}

	/**
	 * 获取 评论时间.
	 *
	 * @return 评论时间
	 */
	public Date getCommentDate() {
		return commentDate;
	}

	/**
	 * 设置 评论时间.
	 *
	 * @param commentDate 评论时间
	 */
	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	/**
	 * 获取 评论内容.
	 *
	 * @return 评论内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置 评论内容.
	 *
	 * @param content 评论内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取 回复评论ID.
	 *
	 * @return 回复评论ID
	 */
	public String getReplyCommentId() {
		return replyCommentId;
	}

	/**
	 * 设置 回复评论ID.
	 *
	 * @param replyCommentId 回复评论ID
	 */
	public void setReplyCommentId(String replyCommentId) {
		this.replyCommentId = replyCommentId;
	}

	/**
	 * 获取 分类1.
	 *
	 * @return 分类1
	 */
	public String getClass1() {
		return class1;
	}

	/**
	 * 设置 分类1.
	 *
	 * @param class1 分类1
	 */
	public void setClass1(String class1) {
		this.class1 = class1;
	}

	/**
	 * 获取 分类2.
	 *
	 * @return 分类2
	 */
	public String getClass2() {
		return class2;
	}

	/**
	 * 设置 分类2.
	 *
	 * @param class2 分类2
	 */
	public void setClass2(String class2) {
		this.class2 = class2;
	}

	/**
	 * 获取 分类3.
	 *
	 * @return 分类3
	 */
	public String getClass3() {
		return class3;
	}

	/**
	 * 设置 分类3.
	 *
	 * @param class3 分类3
	 */
	public void setClass3(String class3) {
		this.class3 = class3;
	}

	/**
	 * 获取 分类5.
	 *
	 * @return 分类5
	 */
	public String getClass4() {
		return class4;
	}

	/**
	 * 设置 分类5.
	 *
	 * @param class4 分类5
	 */
	public void setClass4(String class4) {
		this.class4 = class4;
	}

}
