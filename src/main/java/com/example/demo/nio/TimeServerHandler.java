package com.example.demo.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TimeServerHandler implements Runnable {

	private Socket socket;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public TimeServerHandler(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {

		System.out.println(Thread.currentThread().getName());
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			// 获取输入流
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			// 构造输出流
			out = new PrintWriter(this.socket.getOutputStream(), true);

			String body = null;
			Scanner sc = new Scanner(System.in);
			while (true) {
				while (sc.hasNext()) {
					body = in.readLine();
					if (body == null) {
						break;
					}
					System.out.println("server receive : " + body);
					String str = sc.nextLine();
					out.println(str);
				}
			}
		} catch (Exception e) {
			if (in != null) {
				try {
					in.close();
				} catch (IOException el) {
					el.printStackTrace();
				}
			}
			if (out != null) {
				out.close();
				out = null;
			}
			if (this.socket != null) {
				try {
					this.socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				this.socket = null;
			}
		}
	}

}
