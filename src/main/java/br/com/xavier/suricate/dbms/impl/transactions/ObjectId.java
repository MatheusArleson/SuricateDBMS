package br.com.xavier.suricate.dbms.impl.transactions;

import br.com.xavier.suricate.dbms.abstractions.transactions.AbstractObjectId;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public class ObjectId 
		extends AbstractObjectId {

	private static final long serialVersionUID = 3929401511859038740L;

	public ObjectId(Byte tableId, IThreeByteValue blockId, Long byteOffset) {
		super(tableId, blockId, byteOffset);
	}

}
