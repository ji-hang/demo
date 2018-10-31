package com.example.demo.datax;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataxTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		File file = new File("D:/datax/datax/bin");
		Process pr = Runtime.getRuntime().exec("python D:/datax/datax/bin/datax.py D:/datax/datax/job/oracle.json", null,
				file);
		BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = null;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
		in.close();
		System.err.println(pr.waitFor());
	}
}
