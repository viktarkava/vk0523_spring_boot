package com.toolrental.response;

import java.util.Date;

//vk0523

public class ErrorMessage {
	private Date timestamp;
	private String message;

	public ErrorMessage() {
	}

	public ErrorMessage(Date timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
