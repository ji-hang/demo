package com.example.demo.cglib.proxy;

import java.lang.reflect.Method;

import com.example.demo.entity.User;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 	动态代理类
 * @author admin
 *
 */
public class ShipProxy implements MethodInterceptor {
	
	//通过Enhancer 创建代理对象
	private Enhancer enhancer = new Enhancer();
	
	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> clazz) {
		//设置创建子类的类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return (T) enhancer.create();
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		System.out.println("日志开始...");
        //代理类调用父类的方法
        proxy.invokeSuper(obj, args);
        System.out.println("日志结束...");
        return null;
	}
	
	public static void main(String[] args) {
		//创建我们的代理类
        ShipProxy proxy = new ShipProxy();
        Ship ship = proxy.getProxy(Ship.class);
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123");
        ship.travle(user);
	}
	
}
