package br.com.challenge.enums;

public enum RoleEnum {
	
	DEVELOPER("Developer"), 
	PO("Product Owner"),
	TESTER("Tester");
	
	private String role;
	
	RoleEnum(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
	
	public static RoleEnum valueOfRole(String role) {
		for(RoleEnum e : values()) {
			if(e.getRole().equals(role)) return e;
		}
		return null;
	}
}
