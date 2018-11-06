package com.example.demo.netty_socketio.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.demo.netty_socketio.entity.Message;

/**
 * 监听器
 * @author admin
 *
 */
public class MsgListener implements DataListener<Message>, ConnectListener, DisconnectListener {
	
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
		// test 是时间，页面需要定义test事件的js接收
		//this.server.getBroadcastOperations().sendEvent("test", msg);
		this.server.getRoomOperations(client.getHandshakeData().getSingleUrlParam("appId")).sendEvent("test", msg);
		//this.server.getNamespace("msg").getBroadcastOperations().sendEvent("test", msg);
	}

	@Override
	public void onConnect(SocketIOClient client) {
		String appId = client.getHandshakeData().getSingleUrlParam("appId");
		client.joinRoom(appId);
		System.err.println("客户端：" + client.getSessionId() + "连接");
	}

	@Override
	public void onDisconnect(SocketIOClient client) {
		//加入room
		System.err.println("客户端：" + client.getSessionId() + "断开连接");
	}

}
