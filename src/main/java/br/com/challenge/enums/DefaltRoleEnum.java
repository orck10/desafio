package br.com.challenge.enums;

import java.util.ArrayList;
import java.util.List;

public enum DefaltRoleEnum {
	
	DEVELOPER("Developer"), 
	PO("Product Owner"),
	TESTER("Tester");
	
	private String role;
	
	DefaltRoleEnum(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
	
	public static List<String> valueOfRole() {
		List<String> list = new ArrayList<String>();
		for(DefaltRoleEnum e : values()) {
			list.add(e.role);
		}
		return list;
	}
}
