package com.example.demo.cglib.proxy;

import com.example.demo.entity.User;

/**
 * 	被代理对象
 * @author admin
 *
 */
public class Ship {

	public void travle(User user) {
		System.out.println(user.getUsername() + ": test success..." + user.getPassword());
	}
}
