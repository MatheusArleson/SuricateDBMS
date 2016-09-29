package br.com.xavier.suricate.dbms.impl.services;

import br.com.xavier.suricate.dbms.abstractions.services.AbstractSeparators;

public class TextSeparators 
		extends AbstractSeparators {

	public TextSeparators(
		String columnsSeparator, String nameMetadataSeparator, 
		String typeSizeSeparator, String endLineSeparator
	) {
		super(columnsSeparator, nameMetadataSeparator, typeSizeSeparator, endLineSeparator);
	}

}
