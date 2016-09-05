package br.com.xavier.suricate.dbms.abstractions.table.data;

import java.io.IOException;
import java.util.Arrays;

import br.com.xavier.suricate.dbms.interfaces.table.data.IColumnEntry;

public class AbstractColumnEntry
		implements IColumnEntry {

	private static final long serialVersionUID = -5978555259748724952L;
	
	//XXX PROPERTIES
	private Short contentSize;
	private byte[] content;
	
	//XXX CONSTRUCTORS
	public AbstractColumnEntry() {
		super();
	}
	
	public AbstractColumnEntry(byte[] bytes) throws IOException {
		fromByteArray(bytes);
	}

	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(content);
		result = prime * result + ((contentSize == null) ? 0 : contentSize.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractColumnEntry other = (AbstractColumnEntry) obj;
		if (contentSize == null) {
			if (other.contentSize != null)
				return false;
		} else if (!contentSize.equals(other.contentSize))
			return false;
		if (!Arrays.equals(content, other.content))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractColumnEntry [" 
			+ "contentSize=" + contentSize 
		+ "]";
	}

	//XXX GETTERS/SETTERS
	@Override
	public byte[] getContent() {
		if(content == null){
			return null;
		}
		
		return Arrays.copyOf(content, content.length);
	}
	
	@Override
	public void setContent(byte[] content) {
		if(content == null){
			throw new IllegalArgumentException("Content must not be null.");
		}
		
		Integer contentLength = content.length;
		if(contentLength < IColumnEntry.CONTENT_MIN_LENGTH){
			throw new IllegalArgumentException("Minimum size for content is : " + IColumnEntry.CONTENT_MIN_LENGTH);
		}
		
		if(contentLength > IColumnEntry.CONTENT_MAX_LENGTH){
			throw new IllegalArgumentException("Maximum size for content is : " + IColumnEntry.CONTENT_MAX_LENGTH);
		}
		
		this.content = content;
		this.contentSize = contentLength.shortValue();
	}
	
	@Override
	public Short getContentSize() {
		if(contentSize == null){
			return null;
		}
		
		return new Short(contentSize);
	}
	
	@Override
	public Integer getEntrySize() {
		Short contentSize = getContentSize();
		
		if(contentSize == null){
			return null;
		}
		
		return contentSize + Short.BYTES;
	}
}
