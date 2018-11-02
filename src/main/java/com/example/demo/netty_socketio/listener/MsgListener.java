package com.example.demo.netty_socketio.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.example.demo.netty_socketio.entity.Message;

/**
 * 监听器
 * @author admin
 *
 */
public class MsgListener implements DataListener<Message> {
	
	private SocketIOServer server;
	
	private static ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>(){
		
		public SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	
	public MsgListener(){
		super();
	}
	
	public MsgListener(SocketIOServer server){
		super();
		this.server = server;
	}

	@Override
	public void onData(SocketIOClient client, Message msg, AckRequest req) throws Exception {
		msg.setDate(local.get().format(new Date()));
		this.server.getBroadcastOperations().sendEvent("test", msg);
	}

}
