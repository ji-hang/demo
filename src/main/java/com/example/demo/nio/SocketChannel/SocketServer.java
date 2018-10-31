package com.example.demo.nio.SocketChannel;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketServer {

	public static void main(String[] args) throws IOException {
		
		ServerSocket serverSocket = null;

		InputStream in = null;
		
		try{
			serverSocket = new ServerSocket(9090);
			
			byte[] buffer = new byte[1024];
			
			while(true){
				Socket socket = serverSocket.accept();
				SocketAddress address = socket.getRemoteSocketAddress();
				System.out.println("客户端：" + address.toString());
				in = socket.getInputStream();
				
				while(in.read(buffer) != -1){
					System.out.println("接收到客户端消息：" + new String(buffer));
				}
			}
		}finally {
			 if(serverSocket!=null){
                 serverSocket.close();
             }
             if(in!=null){
                 in.close();
             }
		}
	}
}
