package com.example.demo.netty_socketio.entity;

import lombok.Data;

/**
 * 消息主体
 * 
 * @author admin
 *
 */
@Data
public class Message {

	private String userId;
	private String username;
	private Object msg;
	private String date;

	public Message() {
		super();
	}

	public Message(String userId, String username, Object msg) {
		super();
		this.userId = userId;
		this.username = username;
		this.msg = msg;
	}

}
