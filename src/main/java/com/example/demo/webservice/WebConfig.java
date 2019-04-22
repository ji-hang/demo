package com.example.demo.webservice;


import javax.xml.ws.Endpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
public class WebConfig {

	@Bean
	public Endpoint endpoint(){
		return Endpoint.publish("http://127.0.0.1:8081/user/login", new UserImpl());
	}
}
