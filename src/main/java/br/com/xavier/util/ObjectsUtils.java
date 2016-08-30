package br.com.xavier.util;

public final class ObjectsUtils {
	
	//XXX CONSTRUCTOR
	private ObjectsUtils() {}
	
	//XXX STATIC METHODS
	public static boolean anyNull(Object... objects){
		if(objects == null){
			return true;
		}
		
		for (Object object : objects) {
			if(object == null){
				return true;
			}
		}
		
		return false;
	}
	
}
