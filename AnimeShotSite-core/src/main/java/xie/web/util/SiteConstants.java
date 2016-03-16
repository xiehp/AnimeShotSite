package xie.web.util;

public class SiteConstants {

	/** 审核类型 0:待初审 */
	public static final int AUDIT_TYPE_COMMITED = 0;
	/** 审核类型 1:待终审 */
	public static final int AUDIT_TYPE_FIRST = 1;
	/** 审核类型 2:终审通过 */
	public static final int AUDIT_TYPE_LAST = 2;
	/** 审核类型 4:退回重填资料 */
	public static final int AUDIT_TYPE_NG = 4;

	/** 审核结果 0:退回 */
	public static final int AUDIT_RESULT_RETURN = 0;
	/** 审核结果 1:通过 */
	public static final int AUDIT_RESULT_PASS = 1;

	/** 邮件配置文件,配置位置: application.properties */
	public static final String MAIL_HOST = "mail.host";
	public static final String MAIL_USERNAME = "mail.username";
	public static final String MAIL_PASSWORD = "mail.password";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String MAIL_TIMEOUT = "mail.smtp.timeout";
	
	/** 配置编码*/
	public static final String RATING_ENCODING = "encoding";
	
	
	/** 邮件短信模板相关设置*/
	public static final String MAP_MS_SEND_SET = "MAP_MS_SEND_SET";
	
	
	/** 转贷状态*/
	public static final int RATING_MESSAGE_TYPE_END = 0;
	public static final int RATING_MESSAGE_TYPE_UNEND = 1;
	
	/** 缴费状态*/
	public static final int RATING_PAY_STATUS_PAST = 0;	//缴费过期
	public static final int RATING_PAY_STATUS_UNPAST = 1;	//缴费未过期
	
	/** 缴费审核状态*/
	public static final int RATING_PAY_STATUS_AUDIT = 1;	//已审核
	public static final int RATING_PAY_STATUS_UNAUDIT = 0;	//未审核
	public static final int RATING_PAY_STATUS_SAVE = 9;	//只保存
	
	public static final int RATING_PAY_SUBMIT_STATUS_1 = 1;  //提交
	public static final int RATING_PAY_SUBMIT_STATUS_0 = 0;  //保存
	public static final int RATING_PAY_SUBMIT_FAIL_FLAG = 1;  //保存
	
	/** 缴费记录*/
	public static final int RATING_HAVE_RECORD =1;	// 已经存在记录 ,未过期
	public static final int RATING_HAVE_RECORD_BUT_PAST =2;	// 已经存在记录,但是已过期 
	public static final int RATING_HAVE_NOT_RECORD =0;	// 不存在记录 
	
	
}
