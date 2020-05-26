package com.geekhub.secretaryhelperapp.user.model;

public class User {

	private long id;

	private String username;

	private String password;

	private String displayName;

	private String email;

	private String roles;

	private String department;

	private boolean isEnabled;

	public long getId() {
		return id;
	}

	public User setId(long id) {
		this.id = id;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public User setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getRoles() {
		return roles;
	}

	public User setRoles(String roles) {
		this.roles = roles;
		return this;
	}

	public String getDepartment() {
		return department;
	}

	public User setDepartment(String department) {
		this.department = department;
		return this;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public User setEnabled(boolean enabled) {
		isEnabled = enabled;
		return this;
	}

}
