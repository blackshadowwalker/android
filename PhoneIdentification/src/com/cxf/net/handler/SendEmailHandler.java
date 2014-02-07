package com.cxf.net.handler;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.cxf.entity.PassAuthenticator;

public class SendEmailHandler {
	public static final int SENDEMAIL_SUCCESS = 500;
	public static final int SENDEMAIL_FAILURE = -500;

	@SuppressWarnings("static-access")
	public boolean sendEmail1(String emailString)
			throws UnsupportedEncodingException, MessagingException {
		boolean flag = false;
		Properties props = new Properties();
		props.put("mail.smtp.protocol", "smtp");
		props.put("mail.smtp.auth", "true"); // 设置要验证
		props.put("mail.smtp.host", "smtp.163.com"); // 设置host
		props.put("mail.smtp.port", "25"); // 设置端口

		PassAuthenticator pass = new PassAuthenticator(); // 获取帐号密码

		Session session = Session.getInstance(props, pass); // 获取验证会话

		// 配置发送及接收邮箱
		InternetAddress fromAddress, toAddress;
		fromAddress = new InternetAddress("public_feedback@163.com", "手机认证反馈");
		toAddress = new InternetAddress("896003640@qq.com", "手机认证反馈");

		// 配置发送信息
		MimeMessage message = new MimeMessage(session);
		message.setContent(emailString, "text/plain;charset=utf-8");
		message.setSubject("android问题反馈");
		message.setFrom(fromAddress);
		message.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
		message.saveChanges();

		// 连接邮箱并发送
		Transport transport = session.getTransport("smtp");
		transport.connect("smtp.163.com", "public_feedback@163.com",
				"123456789#k");
		transport.send(message);

		transport.close();
		flag = true;

		return flag;
	}

	/**
	 * 以HTML格式发送邮件
	 * @param url 
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public boolean sendHtmlMail(String emailString, String url)
			throws UnsupportedEncodingException, MessagingException {
		// 判断是否需要身份认证
		PassAuthenticator authenticator = new PassAuthenticator(); // 获取帐号密码
		Properties props = new Properties();
		props.put("mail.smtp.protocol", "smtp");
		props.put("mail.smtp.auth", "true"); // 设置要验证
		props.put("mail.smtp.host", "smtp.163.com"); // 设置host
		props.put("mail.smtp.port", "25"); // 设置端口
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(props,
				authenticator);

		// 根据session创建一个邮件消息
		Message mailMessage = new MimeMessage(sendMailSession);
		// 创建邮件发送者地址
		InternetAddress from = new InternetAddress("public_feedback@163.com",
				"手机认证反馈");
		// 设置邮件消息的发送者
		mailMessage.setFrom(from);
		// 创建邮件的接收者地址，并设置到邮件消息中
		InternetAddress to = new InternetAddress("896003640@qq.com", "手机认证反馈");
		// Message.RecipientType.TO属性表示接收者的类型为TO
		mailMessage.setRecipient(Message.RecipientType.TO, to);
		// 设置邮件消息的主题
		mailMessage.setSubject("android问题反馈");
		// 设置邮件消息发送的时间
		mailMessage.setSentDate(new Date());
		// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
		Multipart mainPart = new MimeMultipart();
		// 创建一个包含HTML内容的MimeBodyPart
		BodyPart html = new MimeBodyPart();
		// 设置HTML内容
		html.setContent(emailString+"\n发送人："+url, "text/html; charset=utf-8");
		mainPart.addBodyPart(html);
		// 将MiniMultipart对象设置为邮件内容
		mailMessage.setContent(mainPart);
		// 发送邮件
		Transport.send(mailMessage);
		return true;
	}
}
