package com.example.demo.init;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class InitTest {

	@PostConstruct
	public void test(){
		System.out.println(111);
	}
}
