package com.geekhub.secretaryhelperapp.calendar.model;

import com.geekhub.secretaryhelperapp.abstraction.dto.AbstractDto;

public class CalendarDto extends AbstractDto {

	private long id;

	private String name;

	private String textColor;

	private String bgColor;

	public long getId() {
		return id;
	}

	public CalendarDto setId(long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public CalendarDto setName(String name) {
		this.name = name;
		return this;
	}

	public String getTextColor() {
		return textColor;
	}

	public CalendarDto setTextColor(String textColor) {
		this.textColor = textColor;
		return this;
	}

	public String getBgColor() {
		return bgColor;
	}

	public CalendarDto setBgColor(String bgColor) {
		this.bgColor = bgColor;
		return this;
	}

}
