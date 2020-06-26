package br.com.munhozrah.jjwtsample.dto;

public class UserDto {
	private String id;
	private String passwd;
	private String role;

	public UserDto() {}
	
	public UserDto(String id, String passwd) {
		this.id = id;
		this.passwd = passwd;
	}

	public String getId() {
		return id;
	}

	public UserDto setId(String id) {
		this.id = id;
		return this;
	}

	public String getPasswd() {
		return passwd;
	}

	public UserDto setPasswd(String passwd) {
		this.passwd = passwd;
		return this;
	}

	public String getRole() {
		return role;
	}

	public UserDto setRole(String role) {
		this.role = role;
		return this;
	}
}