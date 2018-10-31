package com.example.demo.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.lang3.ArrayUtils;


public class CopyFile {
	
	public static void close(Object... objs) throws Exception {
		if(!ArrayUtils.isEmpty(objs)){
			for (Object obj : objs) {
				if(obj != null){
					Method close = obj.getClass().getMethod("close");
					close.invoke(obj);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		String resource = "E:/书籍/headfirst设计模式/head first 设计模式.pdf";
		String target = "E:/项目/test.pdf";

		FileInputStream in = null;

		FileOutputStream out = null;

		FileChannel inChannel = null;

		FileChannel outChannel = null;

		try {
			in = new FileInputStream(resource);
			out = new FileOutputStream(target);

			// 获取读通道
			inChannel = in.getChannel();

			// 写通道
			outChannel = out.getChannel();

			// 定义缓冲区，并制定大小
			ByteBuffer buffer = ByteBuffer.allocate(1024);

			while (inChannel.read(buffer) != -1) {

				// 将buffer指针指向头部
				buffer.flip();

				// 把缓冲区的数据写入通道
				outChannel.write(buffer);

				// 清空缓冲区
				buffer.clear();
			}
		} finally {
			close(inChannel, outChannel, in, out);
		}

	}
}
