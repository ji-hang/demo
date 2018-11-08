package com.example.demo.design.singleton;

/**
 * 单列模式
 * @author admin
 *
 */
public class SingletonTest {
	
	private static SingletonTest instance = null;

	public static SingletonTest getInstance() {
		if(instance == null){
			init();
		}
		return instance;
	}

	/**
	 * 防止线程不安全
	 */
	private static synchronized void init() {
		if(instance == null){
			instance = new SingletonTest();
		}
	}
}
