package com.example.demo.T;

public class GenericityTest<T> {

	private Integer id;
	private T obj;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}
	
	public static <T> boolean Compare(GenericityTest<T> g1, GenericityTest<T> g2){
		return g1.getObj().equals(g2.getObj());
	}
	
	
}
