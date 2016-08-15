package br.com.xavier.suricate.dbms.abstractions.column.type;

public class AbstractColumnSizeFixed
		extends AbstractColumnType {

	private static final long serialVersionUID = -7969162868376318263L;

	//XXX CONSTRUCTOR
	public AbstractColumnSizeFixed(Short id, String name) {
		super(id, null, name);
	}

	@Override
	public void setSize(Short size) { }
}
