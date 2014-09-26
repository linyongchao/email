package com.email.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description:邮箱配置读取类
 * @author lin
 * @date 2014-9-26 下午1:49:33
 */
public final class EmailProperties {
	/**
	 * 用户名
	 */
	public static String username;
	/**
	 * 密码
	 */
	public static String password;
	/**
	 * smtp
	 */
	public static String smtp;
	/**
	 * 默认标题
	 */
	public static String defaultTitle;
	/**
	 * 邮箱名称
	 */
	public static String emailName;

	static {
		getPath();
	}

	/**
	 * 获取文件存放路径
	 * 
	 * @return
	 */
	private static void getPath() {
		Properties pro = new Properties();
		String path = EmailProperties.class.getClassLoader().getResource("")
				+ "email.properties";
		path = path.replace("file:", "");
		File ef = new File(path);

		if (ef.exists()) {
			try {
				// 获取jar包外的资源文件
				InputStream is = new FileInputStream(ef);
				pro.load(is);
			} catch (IOException e) {
				System.err.println("获取email.properties文件信息失败" + e);
			}
			username = pro.getProperty("username");
			password = pro.getProperty("password");
			smtp = pro.getProperty("smtp");
			defaultTitle = pro.getProperty("defaultTitle");
			emailName = pro.getProperty("emailName");
		} else {
			System.out.println("未找到email.properties配置文件");
		}
	}
}