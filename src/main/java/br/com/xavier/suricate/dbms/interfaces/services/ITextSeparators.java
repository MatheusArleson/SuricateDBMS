package br.com.xavier.suricate.dbms.interfaces.services;

public interface ITextSeparators {
	
	String getColumnsSeparator();
	void setColumnsSeparator(String separator);
	
	String getNameMetadataSeparator();
	void setNameMetadataSeparator(String separator);
	
	String getTypeSizeSeparator();
	void setTypeSizeSeparator(String separator);
	
	String getEndLineSeparator();
	void setEndLineSeparator(String separator);

}
