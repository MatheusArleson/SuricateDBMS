package br.com.xavier.suricate.dbms.interfaces;

import java.io.Serializable;
import java.util.Collection;

public interface IDeserializableCollection<T>
		extends Serializable {
	
	Collection<T> fromByteArray(byte[] bytes);

}
