package com.jeeutils.models;

import java.util.List;

public class Message {

	private String type;

	private List<String> messages;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

}