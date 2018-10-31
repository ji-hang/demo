package com.example.demo.filter;

import lombok.Data;

@Data
public class LogMessage {

	private String timestamp;
	private String body;
	private String threadName;
	private String className;
	private String level;
	private String exception;
	private String cause;

	public LogMessage(String body, String timestamp, String threadName, String className, String level,
			String exception, String cause) {
		super();
		this.body = body;
		this.timestamp = timestamp;
		this.threadName = threadName;
		this.className = className;
		this.level = level;
		this.exception = exception;
		this.cause = cause;
	}

}
