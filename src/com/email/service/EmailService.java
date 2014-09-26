package com.email.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.email.sdo.EmailAuthenticator;
import com.email.utils.EmailProperties;

/**
 * @Description:邮件发送服务
 * @author lin
 * @date 2013-7-16 上午9:21:57
 */
public class EmailService {
	private static String username = EmailProperties.username;
	private static String password = EmailProperties.password;
	private static String smtp = EmailProperties.smtp;
	private static String defaultTitle = EmailProperties.defaultTitle;
	private static String emailName = EmailProperties.emailName;

	private static Vector<String> vfile = new Vector<String>();

	private static Session session = null;
	private static MimeMessage message = null;
	private static boolean isChecked;

	/**
	 * 向邮件中添加附件
	 * 
	 * @param attachPath
	 *            ：附件地址
	 */
	public static void addAttachemnt(String attachPath) {
		vfile.add(attachPath);
	}

	/**
	 * 邮件发送必须执行的内容：
	 * 
	 * @1.用户名密码验证
	 * @2.设置发送者的地址
	 * @3.增加了一个默认标题
	 */
	static {
		if (username != null && password != null && smtp != null) {
			isChecked = true;
			System.out.println("属性校验通过");
		} else {
			isChecked = false;
			System.out.println("属性校验失败");
		}
		if (isChecked) {
			Properties props = new Properties();// 获取系统环境
			Authenticator auth = new EmailAuthenticator(username, password);// 进行邮件服务用户认证
			props.put("mail.smtp.host", smtp);
			props.put("mail.smtp.auth", "true");
			// 设置session,和邮件服务器进行通讯
			session = Session.getDefaultInstance(props, auth);
			message = new MimeMessage(session);

			try {
				// 设置邮件发送者的地址
				InternetAddress from = new InternetAddress(username);
				if (emailName != null) {
					from.setPersonal(MimeUtility.encodeText(emailName));
				}
				message.setFrom(from);
			} catch (AddressException e) {
				System.err.println("设置发信地址失败！" + e);
			} catch (UnsupportedEncodingException e) {
				System.err.println("不支持编码异常" + e);
			} catch (MessagingException e) {
				System.err.println("设置发信地址失败！" + e);
			}

			try {
				// 设置默认邮件主题
				if (defaultTitle != null) {
					message.setSubject(defaultTitle);
				}
			} catch (MessagingException e) {
				System.err.println("设置默认主题失败！" + e);
			}
		}
	}

	/**
	 * 文本邮件发送工具类(默认标题)
	 * 
	 * @param emailTo
	 *            ：目标地址
	 * @param msg
	 *            ：邮件内容
	 */
	public static void send(String emailTo, String msg) {
		if (isChecked) {
			try {
				message.setText(msg);// 设置邮件内容
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(emailTo));// 设置邮件接收者的地址
				try {
					// 设置邮件发送时期
					message.setSentDate(new Date());
				} catch (MessagingException e) {
					System.err.println("设置邮件发送时期失败！" + e);
				}
				Transport.send(message);
			} catch (MessagingException e) {
				System.err.println("邮件发送失败！" + e);
			}
		}
	}


	/**
	 * 文本邮件发送工具类
	 * 
	 * @param emailTo
	 *            ：目标地址
	 * @param msg
	 *            ：邮件内容
	 * @param title
	 *            ：邮件标题
	 */
	public static void send(String emailTo, String msg, String title) {
		if (isChecked) {
			try {
				message.setSubject(title);// 设置邮件主题
				message.setText(msg);// 设置邮件内容
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(emailTo));
				try {
					// 设置邮件发送时期
					message.setSentDate(new Date());
				} catch (MessagingException e) {
					System.err.println("设置邮件发送时期失败！" + e);
				}
				Transport.send(message);
			} catch (MessagingException e) {
				System.err.println("邮件发送失败！" + e);
			}
		}
	}

	/**
	 * 可以发送 html 风格的邮件工具类
	 * 
	 * @param emailTo
	 *            ：目标地址
	 * @param msg
	 *            ：邮件内容
	 * @param title
	 *            ：邮件标题
	 * @param flag
	 *            ：true 为 html 风格，false 为 text 文本
	 */
	public static void send(String emailTo, String msg, String title,
			boolean flag) {
		if (isChecked) {
			if (flag) {
				try {
					message.setRecipient(Message.RecipientType.TO,
							new InternetAddress(emailTo));
					message.setSubject(MimeUtility.encodeText(title));
					// 构建邮件内容对象
					Multipart mm = new MimeMultipart();
					// 构建一个消息内容块
					BodyPart mbpFile = new MimeBodyPart();
					mbpFile.setContent(msg, "text/html;charset=UTF-8");
					mm.addBodyPart(mbpFile);
					message.setContent(mm);
					message.saveChanges();
					try {
						// 设置邮件发送时期
						message.setSentDate(new Date());
					} catch (MessagingException e) {
						System.err.println("设置邮件发送时期失败！" + e);
					}
					Transport.send(message);
				} catch (MessagingException e) {
					System.err.println("发送邮件失败！" + e);
				} catch (UnsupportedEncodingException e) {
					System.err.println("发送邮件失败！" + e);
				}
			} else {
				send(emailTo, msg, title);
			}
		}
	}

	/**
	 * 可以发送 html 风格的邮件工具类
	 * 
	 * @param emailTo
	 *            ：目标地址
	 * @param msg
	 *            ：邮件内容
	 * @param flag
	 *            ：true 为 html 风格，false 为 text 文本
	 */
	public static void send(String emailTo, String msg, boolean flag) {
		if (isChecked) {
			if (flag) {
				try {
					message.setRecipient(Message.RecipientType.TO,
							new InternetAddress(emailTo));
					// 构建邮件内容对象
					Multipart mm = new MimeMultipart();
					// 构建一个消息内容块
					BodyPart mbpFile = new MimeBodyPart();
					mbpFile.setContent(msg, "text/html;charset=UTF-8");
					mm.addBodyPart(mbpFile);
					message.setContent(mm);
					message.saveChanges();
					try {
						// 设置邮件发送时期
						message.setSentDate(new Date());
					} catch (MessagingException e) {
						System.err.println("设置邮件发送时期失败！" + e);
					}
					Transport.send(message);
				} catch (MessagingException e) {
					System.err.println("发送邮件失败！" + e);
				}
			} else {
				send(emailTo, msg);
			}
		}
	}


	/**
	 * 带有附件的邮件发送工具类
	 * 
	 * @param emailTo
	 *            ：目标地址
	 * @param msg
	 *            ：邮件内容
	 * @param file
	 *            ：要作为附件的文件地址，Vector<String>类型
	 */
	public static void send(String emailTo, String msg, Vector<String> file) {
		if (isChecked) {
			try {
				// 设置邮件接收的地址
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(emailTo));
				// 构造Multipart
				Multipart mp = new MimeMultipart();
				// 向Multipart添加正文
				MimeBodyPart content = new MimeBodyPart();
				content.setContent(msg, "text/html;charset=gb2312");
				mp.addBodyPart(content);
				// 向Multipart添加附件
				Enumeration<String> efile = file.elements();
				while (efile.hasMoreElements()) {
					MimeBodyPart fattach = new MimeBodyPart();
					String fName = efile.nextElement();
					FileDataSource fds = new FileDataSource(fName);
					fattach.setDataHandler(new DataHandler(fds));
					fattach.setFileName(MimeUtility.encodeWord(fds.getName(),
							"GB2312", null));
					mp.addBodyPart(fattach);
				}
				file.removeAllElements();
				message.setContent(mp);
				message.saveChanges();
				// 发送邮件
				try {
					// 设置邮件发送时期
					message.setSentDate(new Date());
				} catch (MessagingException e) {
					System.err.println("设置邮件发送时期失败！" + e);
				}
				Transport.send(message);
			} catch (MessagingException e) {
				System.err.println("发送邮件失败！" + e);
			} catch (UnsupportedEncodingException e) {
				System.err.println("发送邮件失败！" + e);
			}
		}
	}
}