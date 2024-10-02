package com.utils.myhub;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class AwsSesEmailUtils {

	private static Logger logger = Logger.getLogger(AwsSesEmailUtils.class);

	static final String FROM = WebappPropertyUtils.getWebAppProperty("awsSesFromEmail");
	static final String FROMNAME = WebappPropertyUtils.getWebAppProperty("awsSesFrom");
	static final String SMTP_USERNAME = WebappPropertyUtils.getWebAppProperty("awsSesUsername");
	static final String SMTP_PASSWORD = WebappPropertyUtils.getWebAppProperty("awsSesPassword");
	static final String HOST = WebappPropertyUtils.getWebAppProperty("awsSesHost");

	static final int PORT = 587;

	public static void sendTextMail(String toEmail, String subject, String message, String cc) {

		logger.info("sendTextMail called.\t" + toEmail);

		try {

			/*
			 * Create a Properties object to contain connection configuration information.
			 */

			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.port", PORT);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
			/*
			 * Create a Session object to represent a mail session with the specified
			 * properties.
			 */

			Session session = Session.getDefaultInstance(props);

			/*
			 * Create a message with the specified information.
			 */

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(FROM, FROMNAME));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			msg.setSubject(subject);
			msg.setContent(message, "text/html");

			if (cc != null) {
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
			}

			/*
			 * Create a transport.
			 */
			Transport transport = session.getTransport();

			/*
			 * Send the message.
			 */
			logger.info("Sending email");
			System.out.println("Sending email");
			send(msg, transport);

		} catch (Exception e) {
			logger.info("Error message: {}" + e.getMessage());
		}
	}

	private static void send(MimeMessage msg, Transport transport) throws MessagingException {

		try {

			logger.info("Sending...");

			/*
			 * Connect to Amazon SES using the SMTP username and password you specified
			 * above.
			 */
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

			/*
			 * Send the email.
			 */
			transport.sendMessage(msg, msg.getAllRecipients());

			System.out.println("Email sent!");
			logger.info("Email sent!");

		} catch (Exception ex) {
			System.out.println("The email was not sent." + ex.getStackTrace());
			logger.error("The email was not sent.");
			logger.error("Error message: " + ex);
			ex.printStackTrace();
		} finally {
			/*
			 * Close and terminate the connection.
			 */
			transport.close();
		}
	}
}