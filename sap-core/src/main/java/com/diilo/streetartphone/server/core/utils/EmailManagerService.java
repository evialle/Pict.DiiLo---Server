package com.diilo.streetartphone.server.core.utils;

public interface EmailManagerService {
	public void sendEmail(String name, String email, String subject, String message);
}
