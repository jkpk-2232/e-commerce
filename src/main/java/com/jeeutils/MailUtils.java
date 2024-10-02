//package com.jeeutils;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.UnknownHostException;
//import java.util.Date;
//import java.util.Properties;
//
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//import org.apache.log4j.Logger;
//
//public class MailUtils {
//
//	private static Logger logger = Logger.getLogger(MailUtils.class);
//
//	private static Properties mailProperties = new Properties();
//
//	private static boolean loadedProperties = false;
//
//	private synchronized static void loadMailProperties() throws IOException {
//
//		if (loadedProperties) {
//			return;
//		}
//
//		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("mail.properties");
//
//		mailProperties.load(in);
//
//		loadedProperties = true;
//	}
//
//	private synchronized static String getMailProperty(String key) {
//		return mailProperties.getProperty(key);
//	}
//
//	public static boolean sendTextMail(String to, String subject, String message) {
//
//		try {
//
//			loadMailProperties();
//
//			String mailHost = getMailProperty("mail.server");
//
//			int mailServerPort = Integer.parseInt(getMailProperty("mail.serverPort"));
//
//			String defaultUsername = getMailProperty("mail.defaultUsername");
//			String defaultPassword = getMailProperty("mail.defaultPassword");
//			String defaultFrom = getMailProperty("mail.defaultFrom");
//
//			if ("true".equalsIgnoreCase(getMailProperty("mail.usingGmail"))) {
//
//				Properties props = new Properties();
//
//				props.put("mail.transport.protocol", "smtps");
//				props.put("mail.smtps.host", mailHost);
//				props.put("mail.smtps.auth", "true");
//				props.put("mail.smtps.quitwait", "false");
//
//				Session mailSession = Session.getDefaultInstance(props);
//				mailSession.setDebug(true);
//				Transport transport = mailSession.getTransport();
//				transport.connect(mailHost, mailServerPort, defaultUsername, defaultPassword);
//
//				MimeMessage mimeMessage = new MimeMessage(mailSession);
//				mimeMessage.setSubject(subject);
//				mimeMessage.setContent(message, "text/html");
//
//				mimeMessage.setFrom(new InternetAddress(defaultFrom));
//				mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//				transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
//				transport.close();
//
//				return true;
//			} else {
//				Properties props = new Properties();
//
//				props.put("mail.smtp.host", mailHost);
//				props.put("mail.smtp.port", mailServerPort);
//				props.put("mail.smtp.user", defaultUsername);
//				props.put("mail.debug", "true");
//
//				// Get a session
//				Session session = Session.getInstance(props);
//
//				// Instantiatee a message
//				Message msg = new MimeMessage(session);
//
//				// Set message attributes
//				msg.setFrom(new InternetAddress(defaultFrom));
//				InternetAddress[] address = { new InternetAddress(to) };
//				msg.setRecipients(Message.RecipientType.TO, address);
//				msg.setSubject(subject);
//				msg.setSentDate(new Date());
//				// Set message content
//				msg.setContent(message, "text/html");
//				//msg.setText(message);
//				msg.saveChanges();
//
//				Transport bus = session.getTransport("smtp");
//				bus.connect(mailHost, mailServerPort, defaultUsername, defaultPassword);
//				bus.sendMessage(msg, address);
//
//				bus.close();
//
//				return true;
//			}
//		}
//
//		catch (MessagingException m) {
//			logger.debug("Sending mail failed due to MessagingException");
//			m.printStackTrace();
//			return false;
//		} catch (UnknownHostException h) {
//			logger.debug("Sending mail failed due to UnknownHostException");
//			h.printStackTrace();
//			return false;
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.debug("Sending mail failed", e);
//			return false;
//		}
//	}
//}