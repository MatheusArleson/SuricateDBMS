package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.RandomAccessFile;
import java.io.Serializable;

public interface IFileSystemManager
		extends Serializable {
	
	Long getBlockSize();
	RandomAccessFile createFile(String fileAbsolutePath);
	RandomAccessFile createFile(String fileAbsolutePath, byte[] fileContent);
	byte[] readBlock(RandomAccessFile file, Long blockId);
	void writeBlock(RandomAccessFile file, Long blockId, byte[] content);
	void deleteBlock(RandomAccessFile file, Long blockId);

}
