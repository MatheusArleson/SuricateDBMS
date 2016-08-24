package br.com.xavier.suricate.dbms.interfaces;

import java.io.Serializable;

public interface ISerializable 
		extends Serializable {
	
	byte[] toByteArray();
	
}
