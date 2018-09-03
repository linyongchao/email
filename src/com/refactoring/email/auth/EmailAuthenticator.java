package com.refactoring.email.auth;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 发件人认证
 * 
 * @author lin
 * @date 2018年9月3日 下午3:50:47
 */
public class EmailAuthenticator extends Authenticator {
	private String username;
	private String password;

	public EmailAuthenticator(String username, String pwd) {
		this.username = username;
		this.password = pwd;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}
