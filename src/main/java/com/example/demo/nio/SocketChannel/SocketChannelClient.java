package com.example.demo.nio.SocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class SocketChannelClient {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		SocketChannel socketChannel = null;
		
		try{
			socketChannel = SocketChannel.open();
			//配置非阻塞模式
			socketChannel.configureBlocking(false);
			
			socketChannel.connect(new InetSocketAddress("127.0.0.1", 9090));
			
			if(socketChannel.finishConnect()){
				
				int i = 0;
				
				while(true){
					TimeUnit.SECONDS.sleep(1);
					String info = "I'm " + i++ + "-th information from client";
					buffer.clear();
					buffer.put(info.getBytes());
					buffer.flip();
					
					while(buffer.hasRemaining()){
						socketChannel.write(buffer);
					}
				}
			}
			
		}finally {
			if(socketChannel != null){
				socketChannel.close();
			}
		}
	}
}
