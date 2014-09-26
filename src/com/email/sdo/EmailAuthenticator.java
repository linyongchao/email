package com.email.sdo;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @Description:邮箱身份验证
 * @author lin
 * @date 2013-7-16 上午9:06:50
 */
public class EmailAuthenticator extends Authenticator {
	private String username;
	private String password;

	public EmailAuthenticator() {
		super();
	}

	public EmailAuthenticator(String username, String pwd) {
		super();
		this.username = username;
		this.password = pwd;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}