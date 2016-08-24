package br.com.xavier.suricate.dbms.interfaces;

import java.io.IOException;
import java.io.Serializable;

public interface IBinarizable
		extends Serializable {
	
	byte[] toByteArray() throws IOException;
	
}
