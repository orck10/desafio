package br.com.challenge.enums;

public enum SQLEnums {
	SIMPLE_FIND_WITH_WHERE("SELECT {val} FROM {tab} WHERE {cond}"),
	SIMPLE_SELECT_COUNT("SELECT count({val}) FROM {tab}");
	
	private String sql;
	
	SQLEnums(String sql){
		this.sql = sql;
	}
	
	public String getSql() {
		return sql;
	}
}
