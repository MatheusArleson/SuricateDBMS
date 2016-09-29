package br.com.xavier.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

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
	
	public static boolean isPlatformLineSeparator(String str){
		if(isNull(str)){
			return false;
		}
		
		if(str.equals("\n") || str.equals("\r") || str.equals("\r\n")){
			return true;
		}
		
		return false;
	}
	
	public static boolean containsLineSeparator(String str){
		if(isNullOrEmpty(str)){
			return false;
		}
		
		if(str.contains("\n") || str.contains("\r") || str.contains("\r\n")){
			return true;
		}
		
		return false;
	}
	
	public static String getRegexPattern(String str){
		if(isNullOrEmpty(str)){
			return null;
		}
		
		String quote = Pattern.quote(str);
		Pattern compiled = Pattern.compile(quote);
		return compiled.pattern();
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

	//XXX FETCH METHODS
	public static String readLine(File file, Charset charset, String endLineDelimiter, Integer lineNumber) throws IOException {
		if(lineNumber < 1){
			throw new IOException("Invalid line number.");
		}
		
		if(!StringUtils.isPlatformLineSeparator(endLineDelimiter) && StringUtils.isNullOrEmpty(endLineDelimiter)){
			throw new IOException("Invalid end line separator.");
		}
		
		InputStream is = null;
		InputStreamReader isReader = null;
		BufferedReader buffReader = null;
		Scanner scan = null;
		try{
			
			is = new FileInputStream(file);
			isReader = new InputStreamReader(is, charset);
			buffReader = new BufferedReader(isReader);
			
			scan = new Scanner(buffReader);
			scan.useDelimiter(Pattern.compile(endLineDelimiter).pattern());
			
			String line = null;
			Integer lineCounter = new Integer(0);
			while (scan.hasNext()) {
				line = scan.next();
				lineCounter++;
				
				if(lineNumber.equals(lineCounter)){
					break;
				}
			}
			
			if(!lineNumber.equals(lineCounter)){
				throw new IOException("Invalid line number : EOF reached.");
			}
			
			if(isNullOrEmpty(line)){
				throw new IOException("Invalid line.");
			}
			
			if(containsLineSeparator(line)){
				throw new IOException("Invalid line : check end line separator");
			}
			
			return line;
			
		} catch(Exception e){
			throw new IOException(e);
		} finally {
			if(scan != null){
				IOUtils.closeQuietly(scan);
			}
			
			if(buffReader != null){
				IOUtils.closeQuietly(buffReader);
			}
			
			if(isReader != null){
				IOUtils.closeQuietly(isReader);
			}
			
			if(is != null){
				IOUtils.closeQuietly(is);
			}
		}
	}

	public static LineIterator getLineIterator(File file, Charset charset) throws IOException {
		return FileUtils.lineIterator(file, charset.name());
	}

}