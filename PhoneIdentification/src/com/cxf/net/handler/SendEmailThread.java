package com.cxf.net.handler;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

public class SendEmailThread implements Runnable {

	SendEmailHandler emailHandler;
	Context context;
	Callback handCallback;
	String content;
	String url;

	public SendEmailThread(Context context, Callback haCallback,
			String content, String url) {
		this.context = context;
		this.handCallback = haCallback;
		this.emailHandler = new SendEmailHandler();
		this.content = content;
		this.url = url;
	}

	@Override
	public void run() {
		Looper.prepare();//
		try {
			emailHandler.sendHtmlMail(content, url);
			Message m = new Message();
			m.what = SendEmailHandler.SENDEMAIL_SUCCESS;
			handCallback.handleMessage(m);
		} catch (UnsupportedEncodingException e) {
			Message m = new Message();
			m.what = SendEmailHandler.SENDEMAIL_FAILURE;
			handCallback.handleMessage(m);
			e.printStackTrace();
		} catch (MessagingException e) {
			Message m = new Message();
			m.what = SendEmailHandler.SENDEMAIL_FAILURE;
			handCallback.handleMessage(m);
			e.printStackTrace();
		} catch (Exception e) {
			Message m = new Message();
			m.what = SendEmailHandler.SENDEMAIL_FAILURE;
			handCallback.handleMessage(m);
			e.printStackTrace();
		}
		Looper.loop();
	}
}
