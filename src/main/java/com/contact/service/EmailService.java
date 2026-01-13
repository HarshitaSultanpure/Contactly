package com.contact.service;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	public boolean sendEmail(String subject, String message, String to) 
	{
		boolean f=false;
		String from="sultanpureharshita@gmail.com"; 
		
		String host="smtp.gmail.com";
		
		Properties properties = System.getProperties();
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465"); 
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		
		//to get session object
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication("sultanpureharshita@gmail.com","aiqrgvscmslwejts");
			}
		});
		
		session.setDebug(true);
		
		//compose the message 
		MimeMessage m = new MimeMessage(session);
		try
		{
			m.setFrom(from);
			
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			m.setSubject(subject);
			
			//m.setText(message);
			m.setContent(message,"text/html");
			
			//send message using Transport class
			Transport.send(m);
			
			f=true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return f;
	}
}
