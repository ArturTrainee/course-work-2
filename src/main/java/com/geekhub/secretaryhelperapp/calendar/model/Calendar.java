package com.geekhub.secretaryhelperapp.calendar.model;

import com.geekhub.secretaryhelperapp.abstraction.entity.BaseEntity;

public class Calendar extends BaseEntity<Long> {

	private String name;

	private String textColor;

	private String bgColor;

	@Override
	public Calendar setId(Long id) {
		super.setId(id);
		return this;
	}

	public String getName() {
		return name;
	}

	public Calendar setName(String name) {
		this.name = name;
		return this;
	}

	public String getTextColor() {
		return textColor;
	}

	public Calendar setTextColor(String textColor) {
		this.textColor = textColor;
		return this;
	}

	public String getBgColor() {
		return bgColor;
	}

	public Calendar setBgColor(String bgColor) {
		this.bgColor = bgColor;
		return this;
	}

}
