package com.example.demo.webservice;

import javax.jws.WebService;

import org.springframework.core.annotation.Order;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;

@WebService
public class UserImpl implements IUser {

	@Override
	public String login(String username, String password) {
		if("123".equals(password)){
			return String.format("user %s login success", username);
		}else{
			return "login failure";
		}
	}
	
	public static void main(String[] args) {
	}
	
	
}
