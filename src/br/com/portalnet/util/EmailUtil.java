/**
 * @since 04/12/2008
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;


public class EmailUtil {
	
	private String smtpHost;
	
	private boolean smtpAuthentication;
	
	private String smtpUser;
	
	private String smtpPassword;
	
	private String assinaturaEmail;

	
	public EmailUtil(String smtpHost, boolean smtpAuthentication, String smtpUser, String smtpPassword, String assinaturaEmail) {
		this.smtpHost = smtpHost;
		this.smtpAuthentication = smtpAuthentication;
		this.smtpUser = smtpUser;
		this.smtpPassword = smtpPassword;
		this.assinaturaEmail = assinaturaEmail;
	}
	
	/**
	 * Send email to a single recipient.
	 * 
	 * @param senderAddress the sender email address
	 * @param senderName the sender name
	 * @param receiverAddress the recipient email address
	 * @param sub the subject of the email
	 * @param msg the message content of the email
	 */
	public void sendEmail(String receiverAddress, String sub, String msg) {
		List<String> recipients = new ArrayList<String>();
		recipients.add(receiverAddress);
		
		sendEmail(recipients, sub, msg);
	}

	/**
	 * Send email to a list of recipients.
	 * 
	 * @param senderAddress the sender email address
	 * @param senderName the sender name
	 * @param recipients a list of receipients email addresses
	 * @param sub the subject of the email
	 * @param msg the message content of the email
	 */
	public void sendEmail(List<String> recipients, String sub, String msg) {
		
		try {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

			if (smtpAuthentication) {
				Properties mailProps = new Properties();
				mailProps.put("mail.smtp.auth", "true");
				mailSender.setJavaMailProperties(mailProps);
				mailSender.setUsername(smtpUser);
				mailSender.setPassword(smtpPassword);
			}

			mailSender.setProtocol("smtp");
			mailSender.setPort(587);
			mailSender.setHost(smtpHost);
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			
			String [] emailsDestino = new String[recipients.size()];
			
			helper.setTo(recipients.toArray(emailsDestino));
			helper.setFrom(this.smtpUser);
			helper.setSubject(sub);
			helper.setText(msg+this.assinaturaEmail, false);
			helper.setValidateAddresses(false);

			mailSender.send(mimeMessage);
			
		 } catch (Exception e) {
			e.printStackTrace();
		 }
	}
	
}
