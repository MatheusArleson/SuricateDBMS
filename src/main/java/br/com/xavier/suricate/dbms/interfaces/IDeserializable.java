package br.com.xavier.suricate.dbms.interfaces;

import java.io.Serializable;

public interface IDeserializable<T>
		extends Serializable{
	
	T fromByteArray(byte[] bytes);
	
}
