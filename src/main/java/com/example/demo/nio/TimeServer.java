package com.example.demo.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {

	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = null;

		try {

			serverSocket = new ServerSocket(9090);

			Socket socket = null;

			while (true) {
				// 当没有客户端连接时，程序会阻塞在accept这里，当有客户端访问时，就会创建新的线程去重新执行。
				socket = serverSocket.accept();
				new Thread(new TimeServerHandler(socket)).start();
			}
		} finally {
			if(serverSocket != null){
				serverSocket.close();
			}
		}

	}
}
