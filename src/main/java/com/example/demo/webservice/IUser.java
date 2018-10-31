package com.example.demo.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface IUser {

	@WebMethod
	String login(String username, String password);
}
