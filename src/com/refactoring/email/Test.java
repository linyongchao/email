package com.refactoring.email;

import com.refactoring.email.service.EmailService;

public class Test {
	public static void main(String[] args) {
		try {
			new EmailService().init("sendEmail", "pwd", "smtp", "port", true).addAlias("别名").addAttachs("附件 1 路径")
					.addAttachs("附件 2 路径").addContextImgs("正文附图 1 路径", "正文附图 2 路径").addContext("标题", "正文")
					.addTo("收件人 1 Email").addTo("收件人 2 Email").send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("send success");
	}
}
