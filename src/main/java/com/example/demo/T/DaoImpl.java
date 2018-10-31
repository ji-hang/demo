package com.example.demo.T;

import java.io.Serializable;

public class DaoImpl<ID extends Serializable> implements IDao<ID> {

	@Override
	public int delete(ID id) {
		return 0;
	}
	
	public <T extends Entity> Integer insert(T id){
		return Integer.valueOf(id.getId());
	}

}
