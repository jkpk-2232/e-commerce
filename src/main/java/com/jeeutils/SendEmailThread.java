package com.jeeutils;

import com.utils.myhub.AwsSesEmailUtils;

public class SendEmailThread implements Runnable {

	private String message;

	private String subject;

	private String emailId;

	Thread newMail;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public SendEmailThread(String emailId, String subject, String messasge) {

		this.message = messasge;

		this.subject = subject;

		this.emailId = emailId;

		newMail = new Thread(this);

		newMail.start();

	}

	@Override
	public void run() {

		AwsSesEmailUtils.sendTextMail(emailId, subject, message, null);
//		MailUtils.sendTextMail(emailId, subject, message);
	}
}
