package com.example.demo.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		
		GenericityTest<String> g1 = new GenericityTest<String>();
		g1.setObj("asd");
		
		GenericityTest<String> g2 = new GenericityTest<String>();
		g2.setObj("ascd");
		
		GenericityTest<List<String>> genericityTest1 = new GenericityTest<List<String>>();
		genericityTest1.setObj(new ArrayList<String>());
		
		GenericityTest<Map<String, String>> map = new GenericityTest<Map<String, String>>();
		map.setObj(new HashMap<>());
		
		boolean flag = GenericityTest.Compare(g1, g2);
		System.out.println(flag);
	}
}
