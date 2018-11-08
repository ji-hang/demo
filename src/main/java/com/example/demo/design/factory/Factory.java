package com.example.demo.design.factory;

import com.example.demo.netty_socketio.entity.Message;

public class Factory {

	public static <T> T getInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		Message msg = getInstance(Message.class);
		msg.setMsg("aaa");
	}
}
