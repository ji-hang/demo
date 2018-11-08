package com.example.demo.design.adapter.classAdapter;

import com.example.demo.design.adapter.Source;
import com.example.demo.design.adapter.Target;

/**
 * 类适配器,整合Source和Target中的方法
 * @author admin
 *
 */
public class ClassAdapter extends Source implements Target {

	@Override
	public void method2() {
		System.out.println("this is Class Target method!"); 
	}
	
	public static void main(String[] args) {
		ClassAdapter adapter = new ClassAdapter();
		adapter.method1();
		adapter.method2();
	}
	
}
