package com.geekhub.secretaryhelperapp.eventattendee.repository;

import com.geekhub.secretaryhelperapp.eventattendee.model.EventAttendeeRelation;
import com.geekhub.secretaryhelperapp.eventattendee.model.EventAttendeeRelationRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventAttendeeRelationRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	private final EventAttendeeRelationRowMapper relationRowMapper;

	public EventAttendeeRelationRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.relationRowMapper = new EventAttendeeRelationRowMapper();
	}

	public List<EventAttendeeRelation> getRelationsByEventId(long id) {
		final var sql = "SELECT event_id, attendee_id FROM \"event_attendee_relation\" WHERE event_id = :eventId";
		final var params = new MapSqlParameterSource();
		params.addValue("eventId", id);
		return jdbcTemplate.query(sql, params, relationRowMapper);
	}

	public List<EventAttendeeRelation> getRelationsByAttendeeId(long id) {
		final var sql = "SELECT event_id, attendee_id FROM \"event_attendee_relation\" WHERE attendee_id = :attendeeId";
		final var params = new MapSqlParameterSource();
		params.addValue("attendeeId", id);
		return jdbcTemplate.query(sql, params, relationRowMapper);
	}

	public void saveRelation(EventAttendeeRelation relation) {
		final var sql = "INSERT INTO \"event_attendee_relation\" (event_id, attendee_id) VALUES (:eventId, :attendeeId)";
		final var params = new MapSqlParameterSource();
		params.addValue("eventId", relation.getEventId());
		params.addValue("attendeeId", relation.getAttendeeId());
		jdbcTemplate.update(sql, params);
	}

	public boolean deleteRelationsByEventId(long id) {
		final var sql = "DELETE FROM \"event_attendee_relation\" WHERE event_id = :eventId";
		final var params = new MapSqlParameterSource();
		params.addValue("eventId", id);
		return jdbcTemplate.update(sql, params) > 0;
	}

}
