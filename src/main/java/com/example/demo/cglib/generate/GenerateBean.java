package com.example.demo.cglib.generate;

import java.util.Map;

import net.sf.cglib.beans.BeanGenerator;

/**
 * 	动态生成 实体bean，使用cglib动态加入属性，和相应的get，set方法
 * @author admin
 *
 */
public class GenerateBean {
	
	private BeanGenerator generator;
	
	public GenerateBean() {
		super();
		this.generator = new BeanGenerator();
	}

	/**
	 * 	生成的实体bean
	 */
	private Object object;
	
	/**
 	 *	生成的实体bean的属性	 
	 */
	private Map<String, Class<?>> propertyMap;
	
	public void addProperty(String key, Class<?> clazz) {
		generator.addProperty(key, clazz);
	}
	
	public static void main(String[] args) {
		
	}
}
