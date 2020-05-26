package com.geekhub.secretaryhelperapp.event.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRowMapper implements RowMapper<Event> {

	@Override
	public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
		final var event = new Event();
		event.setId(rs.getLong("id"));
		event.setCalendarId(rs.getLong("calendar_id"));
		event.setTitle(rs.getString("title"));
		event.setType(rs.getString("type"));
		event.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
		event.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
		event.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
		event.setDescription(rs.getString("description"));
		event.setBusy(rs.getBoolean("busy"));
		event.setImportanceLvl(EventImportance.valueOfLvl(rs.getInt("importance_lvl")));
		event.setLocation(rs.getString("location"));
		event.setReadonly(rs.getBoolean("readonly"));
		return event;
	}

}
