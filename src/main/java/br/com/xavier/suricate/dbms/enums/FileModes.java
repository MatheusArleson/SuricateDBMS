package br.com.xavier.suricate.dbms.enums;

public enum FileModes {
	
	//XXX ENUM MEMBERS
	READ_ONLY("r"),
	READ_WRITE("rw"),
	READ_WRITE_CONTENT_SYNC("rwd"),
	READ_WRITE_CONTENT_METADATA_SYNC("rws");
	
	//XXX PROPERTIES
	private final String mode;
	
	//XXX CONSTRUCTOR
	private FileModes(String mode) {
		this.mode = mode;
	}
	
	//XXX GETTERS
	public String getMode() {
		return mode;
	}

}
