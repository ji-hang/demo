package com.example.demo.netty_socketio.server;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.demo.netty_socketio.entity.Message;
import com.example.demo.netty_socketio.listener.MsgListener;

/**
 * 服务端启动代码
 * 
 * @author admin
 *
 */
@Component
public class SocketServer implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Configuration config = new Configuration();
		config.setHostname("118.24.160.133");
		config.setPort(9090);
		SocketIOServer server = new SocketIOServer(config);

		MsgListener listener = new MsgListener(server);

		server.addEventListener("chatevent", Message.class, listener);

		server.start();
	}
}
