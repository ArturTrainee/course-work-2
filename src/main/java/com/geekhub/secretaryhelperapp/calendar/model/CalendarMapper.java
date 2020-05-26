package com.geekhub.secretaryhelperapp.calendar.model;

import com.geekhub.secretaryhelperapp.abstraction.mapper.Mapper;

public class CalendarMapper implements Mapper<CalendarDto, Calendar> {

	@Override
	public Calendar mapToEntity(CalendarDto calendarDto) {
		return new Calendar().setId(calendarDto.getId()).setName(calendarDto.getName())
				.setTextColor(calendarDto.getTextColor()).setBgColor(calendarDto.getBgColor());
	}

	@Override
	public CalendarDto mapToDto(Calendar calendar) {
		return new CalendarDto().setId(calendar.getId()).setName(calendar.getName())
				.setTextColor(calendar.getTextColor()).setBgColor(calendar.getBgColor());
	}

}
