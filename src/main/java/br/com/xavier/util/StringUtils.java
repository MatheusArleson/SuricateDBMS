package br.com.xavier.util;

import java.util.Collection;

public final class StringUtils {
	
	//XXX CONSTRUCTOR
	private StringUtils() {	}
	
	//XXX SINGLE STRING METHODS
	public static boolean isNull(String str) {
		return str == null;
	}
	
	public static boolean isEmpty(String str) {
		if(str == null){
			return false;
		}
		
		return str.trim().isEmpty();
	}
	
	public static boolean isNullOrEmpty(String str){
		return isNull(str) || isEmpty(str);
	}
	
	public static String getTrimmedString(Object obj){
		if(obj == null){
			return null;
		}
		
		return getTrimmed(obj.toString());
	}
	
	public static String getTrimmed(String str){
		if(isNull(str)){
			return null;
		}
		
		return str.trim();
	}
	
	//XXX MULTIPLE STRINGS METHODS
	public static boolean isAnyNull(String... strings) {
		return doCheckAny(true, false, strings);
	}
	
	public static boolean isAnyEmpty(String... strings) {
		return doCheckAny(false, true, strings);
	}
	
	public static boolean isAnyNullOrEmpty(String... strings) {
		return doCheckAny(true, true, strings);
	}
	
	private static boolean doCheckAny(boolean checkNull, boolean checkEmpty, String... strings) {
		if(strings == null){
			return true;
		}
		
		for (String str : strings) {
			if(checkNull && isNull(str)){
				return true;
			}
			
			if(checkEmpty && isEmpty(str)){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isAllNull(String... strings) {
		return doCheckAll(true, false, strings);
	}
	
	public static boolean isAllEmpty(String... strings) {
		return doCheckAll(false, true, strings);
	}
	
	public static boolean isAllNullOrEmpty(String... strings) {
		return doCheckAll(true, true, strings);
	}
	
	public static boolean isAllNullOrEmpty(Collection<String> strings) {
		if(strings == null || strings.isEmpty()){
			return true;
		}
		
		String[] stringsArray = new String[strings.size()];
		strings.toArray(stringsArray);
		return doCheckAll(true, true, stringsArray);
	}
	
	private static boolean doCheckAll(boolean checkNull, boolean checkEmpty, String... strings) {
		if(strings == null){
			return true;
		}
		
		for (String str : strings) {
			boolean isNull = checkNull && isNull(str);
			boolean isEmpty = checkEmpty && isEmpty(str);
			boolean or = isNull || isEmpty;
			
			if(!or){
				return false;
			}
		}
		
		return true;
	}
}