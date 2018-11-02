package com.example.demo.netty_socketio.listener;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;

public class MyAuthorizationListener implements AuthorizationListener {

	@Override
	public boolean isAuthorized(HandshakeData data) {
		String token = data.getSingleUrlParam("token");
		if("123456".equals(token)){
			return true;
		}
		return false;
	}

}
