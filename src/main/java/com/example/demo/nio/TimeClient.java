package com.example.demo.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient {

	public static void main(String[] args) throws IOException {
		int port = 9090;
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		while (true) {
			socket = new Socket("127.0.0.1", port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("QUERY TIME ORDER"); // 输出给服务端
			System.out.println("Send order 2 server succeed.");
			String resp = in.readLine(); // 获取输入流
			System.out.println("NOW is: " + resp);
		}

	}
}
