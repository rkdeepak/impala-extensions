package org.impalaframework.extension.application;

import org.impalaframework.extension.root.MessageService;

public class MessageServiceImpl implements MessageService {

	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
