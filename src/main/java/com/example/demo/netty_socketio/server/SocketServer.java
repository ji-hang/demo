package com.example.demo.netty_socketio.server;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.demo.netty_socketio.entity.Message;
import com.example.demo.netty_socketio.listener.MsgListener;
import com.example.demo.netty_socketio.listener.MyAuthorizationListener;

/**
 * 服务端启动代码
 * 
 * @author admin
 *
 */
@Component
public class SocketServer implements ApplicationRunner {
	
	public static void main(String[] args) throws Exception {
		new SocketServer().run(null);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Configuration config = new Configuration();
		//指定要主机ip地址，这个和页面请求ip地址一致
		config.setHostname("172.16.9.33");
		//指定端口号
		config.setPort(9090);
		//设置最大的WebSocket帧内容长度限制
		config.setMaxFramePayloadLength(1024 * 1024);
		//设置最大HTTP内容长度限制
		config.setMaxHttpContentLength(1024 * 1024);
		
		MyAuthorizationListener auth = new MyAuthorizationListener();
		// 认证的listener
		config.setAuthorizationListener(auth);
		SocketIOServer server = new SocketIOServer(config);
		
		MsgListener listener = new MsgListener(server);
		
		//这里的 chat 是js中的  socket.emit('chat', {username: username, msg: msg}); 一致 
		server.addEventListener("chat", Message.class, listener);
		
		// 连接成功的listener
		server.addConnectListener(listener);
		
		// 连接断开的listener
		server.addDisconnectListener(listener);
		
		server.start();
		while(true){
			
			Message msg = new Message();
			msg.setUsername("test");
			msg.setMsg("success");
			//回推消息
			server.getBroadcastOperations().sendEvent("test", msg);
			Thread.sleep(1000);
		}
	}
}
