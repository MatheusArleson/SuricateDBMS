package br.com.xavier.suricate.dbms.abstractions.services;

import java.util.LinkedHashSet;
import java.util.Set;

import br.com.xavier.suricate.dbms.interfaces.services.ITextSeparators;
import br.com.xavier.util.StringUtils;

public abstract class AbstractSeparators 
		implements ITextSeparators {
	
	//XXX PROPERTIES
	private String columnsSeparator;
	private String nameMetadataSeparator;
	private String typeSizeSeparator;
	private String endLineSeparator;
	
	//XXX CONSTRUCTORS
	public AbstractSeparators(
		String columnsSeparator, 
		String nameMetadataSeparator, 
		String typeSizeSeparator,
		String endLineSeparator
	) {
		super();
		
		validate(endLineSeparator, columnsSeparator, nameMetadataSeparator, typeSizeSeparator);
		
		setColumnsSeparator(columnsSeparator);
		setNameMetadataSeparator(nameMetadataSeparator);
		setTypeSizeSeparator(typeSizeSeparator);
		setEndLineSeparator(endLineSeparator);
	}

	private void validate(String endLineSeparator, String... strings) {
		if(!StringUtils.isPlatformLineSeparator(endLineSeparator)){
			throw new IllegalArgumentException("End line separator must be one of (\n, \r, \r\n).");
		}
		
		if(strings == null){
			throw new IllegalArgumentException("Strings array cannot be null.");
		}
		
		Set<String> stringsSet = new LinkedHashSet<>();
		for (String str : strings) {
			if(StringUtils.isNullOrEmpty(str)){
				throw new IllegalArgumentException("String separator must not be null or empty.");
			}
			
			if(stringsSet.contains(str)){
				throw new IllegalArgumentException("String separator must be unique.");
			}
			
			stringsSet.add(str);
		}
	}

	@Override
	public String getColumnsSeparator() {
		return new String(columnsSeparator);
	}

	@Override
	public void setColumnsSeparator(String separator) {
		if(StringUtils.isNullOrEmpty(separator)){
			throw new IllegalArgumentException("Columns Separator must be a non null or empty string");
		}
		
		this.columnsSeparator = separator;
	}

	@Override
	public String getNameMetadataSeparator() {
		return new String(nameMetadataSeparator);
	}

	@Override
	public void setNameMetadataSeparator(String separator) {
		if(StringUtils.isNullOrEmpty(separator)){
			throw new IllegalArgumentException("Name Metadata Separator must be a non null or empty string");
		}
		
		this.nameMetadataSeparator = separator;
	}

	@Override
	public String getTypeSizeSeparator() {
		return new String(typeSizeSeparator);
	}

	@Override
	public void setTypeSizeSeparator(String separator) {
		if(StringUtils.isNullOrEmpty(separator)){
			throw new IllegalArgumentException("Type Size Separator must be a non null or empty string");
		}
		
		this.typeSizeSeparator = separator;
	}

	@Override
	public String getEndLineSeparator() {
		return new String(endLineSeparator);
	}

	@Override
	public void setEndLineSeparator(String separator) {
		if(StringUtils.isNullOrEmpty(separator) && !StringUtils.isPlatformLineSeparator(separator)) {
			throw new IllegalArgumentException("End line Separator must be a non null or empty string");
		}
		
		this.endLineSeparator = separator;
	}

}
