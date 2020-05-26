package com.geekhub.secretaryhelperapp.attendee.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AttendeeRowMapper implements RowMapper<Attendee> {

	@Override
	public Attendee mapRow(ResultSet rs, int rowNum) throws SQLException {
		final Attendee attendee = new Attendee();
		attendee.setId(rs.getLong("id"));
		attendee.setDisplayName(rs.getString("display_name"));
		attendee.setEmail(rs.getString("email"));
		attendee.setCategory(rs.getString("category"));
		return attendee;
	}

}
