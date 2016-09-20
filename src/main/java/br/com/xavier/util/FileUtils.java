package br.com.xavier.util;

import java.io.File;
import java.io.IOException;

public class FileUtils {
	
	public static void validateInstance(File file, boolean directoryExpected, boolean writeMode) throws IOException {
		if(file == null){
			throw new IOException("Null file");
		}
		
		String absolutePath = file.getAbsolutePath();
		
		boolean isDirectory = file.isDirectory();
		if(isDirectory && !directoryExpected){
			throw new IOException("Path is a directory : " + absolutePath);
		}
		
		if(!isDirectory && directoryExpected){
			throw new IOException("Path is not a directory : " + absolutePath);
		}
		
		if(writeMode && file.exists() && !file.canWrite()){
			throw new IOException("Cannot write in : " + absolutePath);
		}
		
		if(!writeMode && !file.canRead()){
			throw new IOException("Cannot read in : " + absolutePath);
		}
	}
	
}
