package com.geekhub.secretaryhelperapp.attendee.model;

import com.geekhub.secretaryhelperapp.abstraction.entity.BaseEntity;

public class Attendee extends BaseEntity<Long> {

	protected String displayName;

	protected String email;

	protected String category;

	@Override
	public Attendee setId(Long id) {
		super.setId(id);
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Attendee setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Attendee setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public Attendee setCategory(String category) {
		this.category = category;
		return this;
	}

}
