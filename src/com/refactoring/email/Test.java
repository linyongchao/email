package com.refactoring.email;

import com.refactoring.email.service.EmailService;

public class Test {
	public static void main(String[] args) {
		String msg = "this is a test";
		try {
			new EmailService().init("email", "pwd", "smtp.163.com", "别名").addAttachs("/Users/lin/1.jpg")
					.addAttachs("/Users/lin/2.jpg").addContextImgs("/Users/lin/3.jpg", "/Users/lin/4.jpg")
					.addContext("test", msg).addTo("linyongchaohappy@163.com").addTo("lycwork@126.com").send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("send success");
	}
}
