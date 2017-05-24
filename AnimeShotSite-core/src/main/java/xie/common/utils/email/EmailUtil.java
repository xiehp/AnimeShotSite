package xie.common.utils.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import xie.common.utils.props.PropsUtil;
import xie.common.utils.string.StringUtil;
import xie.web.util.SiteConstants;

public class EmailUtil {

	private volatile static EmailUtil emailUtil;

	private static Logger _log = LoggerFactory.getLogger(EmailUtil.class);
	
	private final static JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

	private EmailUtil() {
		/**
		 * 初始化JavaMailSenderImpl
		 */
		mailSender.setHost(PropsUtil.getProperty(SiteConstants.MAIL_HOST));
		mailSender.setUsername(PropsUtil.getProperty(SiteConstants.MAIL_USERNAME));
		mailSender.setPassword(PropsUtil.getProperty(SiteConstants.MAIL_PASSWORD));
		mailSender.setDefaultEncoding(PropsUtil.getProperty(SiteConstants.RATING_ENCODING));
		
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", PropsUtil.getProperty(SiteConstants.MAIL_SMTP_AUTH));
		properties.put("mail.smtp.timeout", PropsUtil.getProperty(SiteConstants.MAIL_TIMEOUT));
		mailSender.setJavaMailProperties(properties);
		
	}
	
	static {
	}
	
	public static EmailUtil getEmailUtil() {
		if (emailUtil == null) {
			synchronized (EmailUtil.class) {
				if (emailUtil == null) {
					emailUtil = new EmailUtil();
				}
			}
		}
		return emailUtil;
	}




	public void sendHtmlEmail(final String fromMail, final String[] toMails, final String[] ccMails,
			final String msgSubject, final String msgBody)
					throws MessagingException {
		sendEmail(fromMail, toMails, ccMails, msgSubject, msgBody, null, null, true);
	}

	public void sendHtmlEmail(final String fromMail, final String toMail, final String ccMail,
			final String msgSubject, final String msgBody) throws MessagingException {
		sendEmail(fromMail, toMail, ccMail, msgSubject, msgBody, null, null, true);
	}


	private void sendEmail(final String fromMail, final String toMail, final String ccMail,
			final String msgSubject, final String msgBody, final String senderLoginId, final String senderPassword,
			final boolean htmlFlag) throws MessagingException {

		if (!StringUtil.isNull(senderLoginId)) {
			mailSender.setUsername(senderLoginId);
		}

		if (!StringUtil.isNull(senderPassword)) {
			mailSender.setPassword(senderPassword);
		}

		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false);

		final InternetAddress internetAddress = translateEmailAddress(fromMail);
		messageHelper.setFrom(internetAddress);
		messageHelper.setTo(toMail);
		if (!StringUtil.isNull(ccMail)) {
			messageHelper.setCc(ccMail);
		}
		messageHelper.setSubject(msgSubject);
		messageHelper.setText(msgBody, htmlFlag);
		mailSender.send(mimeMessage);
		_log.info("email send end!");
	}

	private static void sendEmail(final String fromMail, final String[] toMails, final String[] ccMails,
			final String msgSubject, final String msgBody, final String senderLoginId, final String senderPassword,
			final boolean htmlFlag) throws MessagingException {

		if (!StringUtil.isNull(senderLoginId)) {
			mailSender.setUsername(senderLoginId);
		}

		if (!StringUtil.isNull(senderPassword)) {
			mailSender.setPassword(senderPassword);
		}

		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false);

		final InternetAddress internetAddress = translateEmailAddress(fromMail);
		messageHelper.setFrom(internetAddress);
		messageHelper.setTo(toMails);
		if (ccMails.length > 0) {
			messageHelper.setCc(ccMails);
		}
		messageHelper.setSubject(msgSubject);
		messageHelper.setText(msgBody, htmlFlag);
		mailSender.send(mimeMessage);
		_log.info("email send end!");
	}

	/**
	 * translate EmailAddress to InternetAddress
	 * 
	 * @param emailAddress
	 *            : such as a@b.com or a@b.com(name)
	 * @return
	 */
	private static InternetAddress translateEmailAddress(final String emailAddress) {
		final InternetAddress internetAddress = new InternetAddress();
		try {
			final int pos = emailAddress.indexOf('(');
			if (pos < 0) {
				internetAddress.setAddress(emailAddress);
			} else {
				if (pos > 0) {
					internetAddress.setAddress(emailAddress.substring(0, pos));
					final String name = emailAddress.substring(pos + 1);
					if (name.lastIndexOf(')') == name.length() - 1 && name.length() > 1) {
						internetAddress.setPersonal(name.substring(0, name.length() - 1), "UTF-8");
					}
				} else {
					internetAddress.setAddress("");
				}
			}
		} catch (UnsupportedEncodingException e) {
			_log.error(e.toString());
		}
		return internetAddress;
	}
}