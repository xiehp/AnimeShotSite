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

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReplyCommentId() {
		return replyCommentId;
	}

	public void setReplyCommentId(String replyCommentId) {
		this.replyCommentId = replyCommentId;
	}

	public String getClass1() {
		return class1;
	}

	public void setClass1(String class1) {
		this.class1 = class1;
	}

	public String getClass2() {
		return class2;
	}

	public void setClass2(String class2) {
		this.class2 = class2;
	}

	public String getClass3() {
		return class3;
	}

	public void setClass3(String class3) {
		this.class3 = class3;
	}

	public String getClass4() {
		return class4;
	}

	public void setClass4(String class4) {
		this.class4 = class4;
	}

}
