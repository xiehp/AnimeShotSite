package xie.common.utils;


import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xie.common.utils.email.EmailUtil;
import xie.common.utils.props.PropsUtil;
import xie.web.util.SiteConstants;

public class MsSendUtil {
	
	private static Logger _log = LoggerFactory.getLogger(MsSendUtil.class);
	
	/**
	 * 发送邮件(支持html格式)
	 * @param toMail	收件人
	 * @param ccMail	抄送方
	 * @param msgSubject	邮件标题
	 * @param msgBody	邮件内容
	 */
	public static void sendMail(final String toMail, String ccMail,final String msgSubject, String msgBody) {
		String fromMail = PropsUtil.getProperty(SiteConstants.MAIL_USERNAME);
		try {
			EmailUtil.getEmailUtil().sendHtmlEmail(fromMail, toMail, ccMail, msgSubject, msgBody);
		} catch (MessagingException e) {
			_log.error("Send Email to "+toMail+" Error: {}",e.getCause());
		}
	}
	
	/**
	 * 邮件群发(支持html格式)
	 * @param toMail	String[] 收件人
	 * @param ccMail	String[] 抄送方
	 * @param msgSubject	邮件标题
	 * @param msgBody	邮件内容
	 */
	public static void sendMail(final String[] toMail, String[] ccMail,final String msgSubject, String msgBody) {
		String fromMail = PropsUtil.getProperty(SiteConstants.MAIL_USERNAME);
		try {
			EmailUtil.getEmailUtil().sendHtmlEmail(fromMail, toMail, ccMail, msgSubject, msgBody);
		} catch (MessagingException e) {
			_log.error("Send Email to "+toMail+" Error: {}"+e.getCause());
		}
	}
	
}
