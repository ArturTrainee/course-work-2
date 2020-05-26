package com.geekhub.secretaryhelperapp.attendee.model;

import com.geekhub.secretaryhelperapp.abstraction.dto.AbstractDto;

public class AttendeeDto extends AbstractDto {

	private long id;

	private String displayName;

	private String email;

	private String category;

	public long getId() {
		return id;
	}

	public AttendeeDto setId(long id) {
		this.id = id;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public AttendeeDto setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public AttendeeDto setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public AttendeeDto setCategory(String category) {
		this.category = category;
		return this;
	}

}
