package com.geekhub.secretaryhelperapp.eventattendee.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventAttendeeRelationRowMapper implements RowMapper<EventAttendeeRelation> {

	@Override
	public EventAttendeeRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
		final EventAttendeeRelation relation = new EventAttendeeRelation();
		relation.setEventId(rs.getLong("event_id"));
		relation.setAttendeeId(rs.getLong("attendee_id"));
		return relation;
	}

}
