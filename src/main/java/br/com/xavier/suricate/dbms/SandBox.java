package br.com.xavier.suricate.dbms;

import java.io.IOException;

import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlockContent;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;

public class SandBox {
	
	public static void main(String[] args) throws IOException {
		Byte tableId = new Byte("1");
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(2);
		Short headerSize = new Short("3");
		Integer nextFreeBlockId = new Integer(4);
		TableStatus tableStatus = TableStatus.VALID;
		
		ITableHeaderBlockContent thbc = new TableHeaderBlockContent(tableId, blockSize, headerSize, nextFreeBlockId, tableStatus);
		byte[] bytes = thbc.toByteArray();
		
		ITableHeaderBlockContent thbc2 = new TableHeaderBlockContent(bytes);
		System.out.println(thbc2);
	}

}
