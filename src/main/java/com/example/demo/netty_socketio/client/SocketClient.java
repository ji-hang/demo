package com.example.demo.netty_socketio.client;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.UUID;

import com.example.demo.netty_socketio.entity.Message;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import net.sf.json.JSONObject;

public class SocketClient {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	static {
		//对于为null的字段不进行序列化
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        //对于未知属性不进行反序列化
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        //无论对象中的值只有不为null的才进行序列化
        mapper.setSerializationInclusion(Include.NON_NULL);
	}

	public static void main(String[] args) throws URISyntaxException, InterruptedException {
		IO.Options options = new IO.Options();
		options.reconnection = true;
		//失败重连的次数
		options.reconnectionAttempts = 2;
		//失败重连的时间间隔
		options.reconnectionDelay = 5000;
		//连接超时时间
		options.timeout = 500;
		
		Socket socket = IO.socket("http://172.16.9.33:9090?token=123456", options);

		socket.on(Socket.EVENT_CONNECT, new Listener() {
			
			@Override
			public void call(Object... arg0) {
				System.out.println("client：连接成功" + Arrays.toString(arg0));
				Message msg = new Message(); 
				msg.setMsg("hello");
				msg.setUserId(UUID.randomUUID().toString());
				msg.setUsername("admin");
				socket.emit("chat", JSONObject.fromObject(msg));
			}
		});
		
		socket.on(Socket.EVENT_DISCONNECT, new Listener() {
			
			@Override
			public void call(Object... arg0) {
				System.out.println("client：断开连接" + Arrays.toString(arg0));
			}
		});
		
		socket.on("test", new Listener() {
            @Override
            public void call(Object... args) {
            	System.out.println("client：收到消息" + Arrays.toString(args));
            }
        });
		
		socket.connect();
		//Thread.sleep(20000);
		
		//socket.disconnect();
		
	}
}
