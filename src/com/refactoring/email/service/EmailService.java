package com.refactoring.email.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.refactoring.email.auth.EmailAuthenticator;

/**
 * 邮件发送工具类
 * 
 * @author lin
 * @date 2018年9月3日 下午3:51:10
 */
public class EmailService {
	// 基本配置
	private String username;
	private String password;
	private String smtp;
	private String emailName;
	// 正文
	private String title;
	private String msg;
	// 附件
	private List<String> attachs = new ArrayList<>();
	// 正文附图
	private List<String> contextImg = new ArrayList<>();
	// 收件人
	private List<String> to = new ArrayList<>();
	// 抄送
	private List<String> cc = new ArrayList<>();
	// 密送
	private List<String> bcc = new ArrayList<>();

	/**
	 * 初始化基本参数
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param smtp
	 *            smtp
	 * @return
	 * @author lin
	 * @date 2018年9月3日 下午3:55:40
	 */
	public EmailService init(String username, String password, String smtp) {
		this.username = username;
		this.password = password;
		this.smtp = smtp;
		return this;
	}

	/**
	 * 初始化基本参数
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param smtp
	 *            密码
	 * @param emailName
	 *            别名
	 * @return
	 * @author lin
	 * @date 2018年9月3日 下午3:56:01
	 */
	public EmailService init(String username, String password, String smtp, String emailName) {
		this.username = username;
		this.password = password;
		this.smtp = smtp;
		this.emailName = emailName;
		return this;
	}

	/**
	 * 添加正文
	 * 
	 * @param title
	 *            标题
	 * @param msg
	 *            内容
	 * @return
	 * @author lin
	 * @date 2018年9月3日 下午3:56:56
	 */
	public EmailService addContext(String title, String msg) {
		this.title = title;
		this.msg = msg;
		return this;
	}

	/**
	 * 添加正文附图
	 * 
	 * @param contextImgs
	 * @return
	 * @author lin
	 * @date 2018年9月3日 下午3:58:26
	 */
	public EmailService addContextImgs(String... contextImgs) {
		for (String string : contextImgs) {
			this.contextImg.add(string);
		}
		return this;
	}

	/**
	 * 添加附件
	 * 
	 * @param attachs
	 *            附件路径
	 * @return
	 * @author lin
	 * @date 2018年9月3日 下午3:57:41
	 */
	public EmailService addAttachs(String... attachs) {
		for (String string : attachs) {
			this.attachs.add(string);
		}
		return this;
	}

	/**
	 * 添加收件人
	 * 
	 * @param to
	 *            收件人
	 * @return
	 * @author lin
	 * @date 2018年9月3日 下午3:59:32
	 */
	public EmailService addTo(String... to) {
		for (String string : to) {
			this.to.add(string);
		}
		return this;
	}

	/**
	 * 添加抄送人
	 * 
	 * @param cc
	 *            抄送人
	 * @return
	 * @author lin
	 * @date 2018年9月3日 下午3:59:49
	 */
	public EmailService addCC(String... cc) {
		for (String string : cc) {
			this.cc.add(string);
		}
		return this;
	}

	/**
	 * 添加密送人
	 * 
	 * @param bcc
	 *            密送人
	 * @return
	 * @author lin
	 * @date 2018年9月3日 下午4:00:05
	 */
	public EmailService addBCC(String... bcc) {
		for (String string : bcc) {
			this.bcc.add(string);
		}
		return this;
	}

	/**
	 * 发送邮件
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @author lin
	 * @date 2018年9月3日 下午4:00:29
	 */
	public void send() throws UnsupportedEncodingException, MessagingException {
		if (username == null || password == null || smtp == null) {
			throw new NullPointerException("username,password,smtp均不能为空");
		}
		if (this.to.size() == 0) {
			throw new NullPointerException("收件人地址不能为空");
		}
		if (this.title == null || this.msg == null) {
			throw new NullPointerException("title,msg均不能为空");
		}
		// 获取系统环境
		Properties props = new Properties();
		props.put("mail.smtp.host", smtp);
		props.put("mail.smtp.auth", "true");
		// 进行邮件服务用户认证
		Authenticator auth = new EmailAuthenticator(username, password);
		// 设置session和邮件服务器进行通讯
		Session session = Session.getDefaultInstance(props, auth);
		MimeMessage message = new MimeMessage(session);
		// 设置邮件发送者的地址
		InternetAddress from = new InternetAddress(username);
		if (emailName != null) {
			from.setPersonal(MimeUtility.encodeText(emailName));
		}
		message.setFrom(from);
		// 设置邮件主题
		if (title != null) {
			message.setSubject(MimeUtility.encodeText(title));
		}

		// 开始设置邮件内容
		MimeMultipart multipart = new MimeMultipart();
		// 设置正文图片
		if (contextImg.size() > 0) {
			// 内嵌资源形式
			multipart.setSubType("related");
			// 添加附件
			for (int i = 0; i < contextImg.size(); i++) {
				MimeBodyPart attach = new MimeBodyPart();
				DataHandler dataHandler = new DataHandler(new FileDataSource(contextImg.get(i)));
				attach.setDataHandler(dataHandler);
				attach.setContentID("img" + i);
				multipart.addBodyPart(attach);
				msg += "<img src='cid:img" + i + "'>";
			}
		}
		// 设置附件
		if (attachs.size() > 0) {
			// 附件形式
			multipart.setSubType("mixed");
			// 添加附件
			for (String string : attachs) {
				MimeBodyPart attach = new MimeBodyPart();
				DataHandler dataHandler = new DataHandler(new FileDataSource(string));
				attach.setDataHandler(dataHandler);
				multipart.addBodyPart(attach);
			}
		}
		// 设置正文
		MimeBodyPart msgPart = new MimeBodyPart();
		msgPart.setContent(msg, "text/html; charset=utf-8");
		multipart.addBodyPart(msgPart);
		// 将邮件内容添加到message
		message.setContent(multipart);
		// 设置邮件接收者的地址
		InternetAddress[] toArray = getAddresses(this.to);
		message.addRecipients(Message.RecipientType.TO, toArray);
		// 设置抄送
		if (this.cc.size() > 0) {
			InternetAddress[] ccArray = getAddresses(this.cc);
			message.addRecipients(Message.RecipientType.CC, ccArray);
		}
		// 设置密送
		if (this.bcc.size() > 0) {
			InternetAddress[] bccArray = getAddresses(this.bcc);
			message.addRecipients(Message.RecipientType.BCC, bccArray);
		}
		// 设置邮件发送时期
		message.setSentDate(new Date());
		Transport.send(message);
	}

	/**
	 * 收件人地址转换
	 * 
	 * @param list
	 *            String类型的收件人
	 * @return InternetAddress类型的收件人
	 * @throws AddressException
	 * @author lin
	 * @date 2018年9月3日 下午4:16:17
	 */
	private InternetAddress[] getAddresses(List<String> list) throws AddressException {
		InternetAddress[] array = new InternetAddress[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = new InternetAddress(list.get(i));
		}
		return array;
	}

}