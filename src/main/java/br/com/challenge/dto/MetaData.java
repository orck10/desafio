package br.com.challenge.dto;

public class MetaData {
	
	private Integer page;
	private Integer size;
	private Integer lastPage;
	
	public MetaData() {
	}
	
	public MetaData(Integer page, Integer size, Integer lastPage) {
		super();
		this.page = page;
		this.size = size;
		this.lastPage = lastPage;
	}

	public Integer getPage() {
		return page;
	}

	public Integer getSize() {
		return size;
	}

	public Integer getLastPage() {
		return lastPage;
	}
}
