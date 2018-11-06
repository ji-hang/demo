package com.example.demo.netty_socketio.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 消息主体
 * 
 * @author admin
 *
 */
@Data
public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String username;
	private Object msg;
	private String date;

	
	/*public Message(String userId, String username, Object msg) {
		super();
		this.userId = userId;
		this.username = username;
		this.msg = msg;
	}

	public Message(String userId, String username, Object msg, String date) {
		super();
		this.userId = userId;
		this.username = username;
		this.msg = msg;
		this.date = date;
	}*/

}
