package com.example.demo.nio;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Test {

	public static void main(String[] args) {
		
		ByteBuffer buffer = ByteBuffer.allocate(10);
		for (int i = 0; i < buffer.capacity(); i++) {
			buffer.put((byte) i);
		}
		
		buffer.position(3);
		buffer.limit(6);
		ByteBuffer child = buffer.slice();
		for (int i = 0; i < child.capacity(); i++) {
			child.put(i, (byte) (child.get(i)*11));
		}
		
		buffer.position(0);
		buffer.limit(buffer.capacity());
		for (int i = 0; i < buffer.capacity(); i++) {
			System.out.println(buffer.get(i));
		}
		
		System.err.println("--------------------------------------------------------------");
		/*ByteBuffer byteBuffer = ByteBuffer.allocate(10);
		System.out.println(byteBuffer.capacity());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.position());

		byteBuffer.put("dacb".getBytes());

		System.out.println(byteBuffer.capacity());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.position());
		byteBuffer.flip();
		
		byte[] bytea = new byte[2];
		System.out.println(byteBuffer.get(bytea, 0, 2));
		
		System.out.println(Arrays.toString(bytea));
		
		System.out.println(byteBuffer.capacity());
		System.out.println(byteBuffer.limit());
		System.out.println("----" + byteBuffer.position());*/
		/*byteBuffer.flip();// 将存入数据模式变成取出数据模式 已经存入的数据posstion又变成0，从头继续读

		System.out.println(byteBuffer.capacity());
		System.out.println(byteBuffer.limit());
		System.out.println("----" + byteBuffer.position());

		System.out.println("**********get（）********");
		System.out.println((char) byteBuffer.get()); // 每get一次posstion+1
		System.out.println((char) byteBuffer.get());
		System.out.println(byteBuffer.capacity());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.position());*/
	}
}
