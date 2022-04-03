package br.com.challenge.dto;

public class DataPresenter {
	private Object data;
	private MetaData meta;
	
	public DataPresenter() {
		super();
	}
		
	public DataPresenter(Object data, MetaData meta) {
		super();
		this.data = data;
		this.meta = meta;
	}

	public Object getData() {
		return data;
	}

	public MetaData getMeta() {
		return meta;
	}
	
	
	
}
