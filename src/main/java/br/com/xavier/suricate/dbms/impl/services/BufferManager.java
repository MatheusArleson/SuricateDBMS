package br.com.xavier.suricate.dbms.impl.services;

import br.com.xavier.suricate.dbms.abstractions.services.AbstractBufferManager;
import br.com.xavier.suricate.dbms.interfaces.services.IFileSystemManager;

public class BufferManager 
		extends AbstractBufferManager {

	public BufferManager(int bufferSlots) {
		super(bufferSlots);
	}
	
	public BufferManager(IFileSystemManager fileSystemManager, int bufferSlots) {
		super(fileSystemManager, bufferSlots);
	}

}
