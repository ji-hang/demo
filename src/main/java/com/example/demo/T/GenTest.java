package com.example.demo.T;


public class GenTest<ID> {

	public <T> T load(ID id, String name){
		Entity obj = new Entity();
		obj.setName(name);
		return (T) obj;
	}
}
