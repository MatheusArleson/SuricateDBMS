package br.com.xavier.suricate.dbms.impl;

import br.com.xavier.suricate.dbms.abstractions.table.AbstractThreeByteValue;

public class ThreeBytesValue 
		extends AbstractThreeByteValue {

	private static final long serialVersionUID = 5549909069846860140L;
	
	public ThreeBytesValue() {
		super();
	}
	
	public ThreeBytesValue(Integer value){
		super(value);
	}
	
	public static void main(String[] args) {
		ThreeBytesValue tbv = new ThreeBytesValue();
		tbv.setValue(511);
		System.out.println(tbv.getValue());
	}

}
