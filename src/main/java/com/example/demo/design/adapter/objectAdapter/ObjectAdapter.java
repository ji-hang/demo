package com.example.demo.design.adapter.objectAdapter;

import com.example.demo.design.adapter.Source;
import com.example.demo.design.adapter.Target;

import lombok.Data;

/**
 * 对象适配器
 * 
 * @author admin
 *
 */
@Data
public class ObjectAdapter implements Target {

	private Source source;

	public ObjectAdapter(Source source) {
		super();
		this.source = source;
	}

	@Override
	public void method2() {
		System.out.println("this is Class Target method!");
	}

	public void method1() {
		source.method1();
	}

	public static void main(String[] args) {
		Source source = new Source();
		ObjectAdapter adapter = new ObjectAdapter(source);
		adapter.method1();
		adapter.method2();
	}
}
