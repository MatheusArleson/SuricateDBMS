package br.com.xavier.suricate.dbms.interfaces;

import java.io.Serializable;

public interface IBinarizable<T> 
		extends Serializable {
	
	byte[] toByteArray();
	
}
