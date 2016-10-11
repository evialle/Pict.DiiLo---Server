package com.diilo.streetartphone.server.core.utils.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.diilo.streetartphone.server.core.services.implementations.UserServiceImpl;
import com.diilo.streetartphone.server.core.utils.EmailManagerService;

@Service
public class EmailManagerImplService implements EmailManagerService {

	private static final Logger	LOG	= LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private MailSender			mailSender;

	@Autowired
	private SimpleMailMessage	templateMessage;

	public void sendEmail(final String name, final String email, final String subject, final String message)
			throws MailException {

		// Create a thread safe "copy" of the template message and customize it
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setSubject(subject);
		msg.setTo(email);
		msg.setText(message);
		this.mailSender.send(msg);
		LOG.info("Sent email: " + email);
	}
}