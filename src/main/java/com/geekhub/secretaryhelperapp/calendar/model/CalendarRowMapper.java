package com.geekhub.secretaryhelperapp.calendar.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CalendarRowMapper implements RowMapper<Calendar> {

	@Override
	public Calendar mapRow(ResultSet rs, int rowNum) throws SQLException {
		final var calendar = new Calendar();
		calendar.setId(rs.getLong("id"));
		calendar.setName(rs.getString("name"));
		calendar.setTextColor(rs.getString("text_color"));
		calendar.setBgColor(rs.getString("bg_color"));
		return calendar;
	}

}
