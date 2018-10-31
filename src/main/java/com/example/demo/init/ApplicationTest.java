package com.example.demo.init;

import java.util.Arrays;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationTest implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.err.println("=================================");
		System.out.println(Arrays.asList(args.getSourceArgs()));
		System.err.println("=================================");
	}
	
	public static void main(String[] args) {
		
		System.out.println(51117340.167 - 12529213.948 - 35162 - 2990324.45 - 1195197.15 - 14629152.909);
	}

}
