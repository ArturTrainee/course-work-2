package com.geekhub.secretaryhelperapp.attendee.repository;

import com.geekhub.secretaryhelperapp.abstraction.repository.CrudRepository;
import com.geekhub.secretaryhelperapp.attendee.model.Attendee;
import com.geekhub.secretaryhelperapp.attendee.model.AttendeeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
public class AttendeeRepository implements CrudRepository<Attendee, Long> {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	private final AttendeeRowMapper attendeeRowMapper;

	@Autowired
	public AttendeeRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.attendeeRowMapper = new AttendeeRowMapper();
	}

	@Override
	public boolean existsById(final Long id) {
		final var sql = "SELECT EXISTS(SELECT 1 FROM \"attendee\" WHERE id = :id)";
		final var params = new MapSqlParameterSource();
		params.addValue("id", id);
		final var isExists = jdbcTemplate.queryForObject(sql, params, Boolean.TYPE);
		return isExists != null && isExists;
	}

	@Override
	public Optional<Attendee> getById(final Long id) {
		final var sql = "SELECT id, display_name, email, category FROM \"attendee\" WHERE id = :attendeeId";
		final var params = new MapSqlParameterSource();
		params.addValue("attendeeId", id);
		return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, attendeeRowMapper));
	}

	public Iterable<Attendee> getAllByEventId(final Long id) {
		final var sql = "SELECT id, display_name, email, category " +
				"FROM \"attendee\" a " +
				"WHERE a.id IN " +
				"(" +
				"SELECT attendee_id " +
				"FROM \"event_attendee_relation\" r " +
				"WHERE r.event_id = :eventId" +
				") " +
				"ORDER BY id";
		final var params = new MapSqlParameterSource();
		params.addValue("eventId", id);
		return jdbcTemplate.query(sql, params, attendeeRowMapper);
	}

	@Override
	public Iterable<Attendee> getAllByIds(final Iterable<Long> ids) {
		final var sql =
				"SELECT id, display_name, email, category " +
				"FROM \"attendee\" " +
				"WHERE id IN (:ids)";
		final var params = new MapSqlParameterSource();
		params.addValue("ids", ids);
		return jdbcTemplate.query(sql, params, attendeeRowMapper);
	}

	@Override
	public Iterable<Attendee> getAll() {
		final var sql = "SELECT id, display_name, email, category FROM \"attendee\"";
		return jdbcTemplate.query(sql, attendeeRowMapper);
	}

	@Override
	public <S extends Attendee> S save(final S attendee) {
		final var sql ="INSERT INTO \"attendee\" (id, display_name, email, category) " +
					   "VALUES (:id, :display_name, :email, :category) RETURNING id";
		final var params = new MapSqlParameterSource();
		params.addValue("displayName", attendee.getDisplayName());
		params.addValue("email", attendee.getEmail());
		params.addValue("category", attendee.getCategory());
		final var keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(sql, params, keyHolder);
		final var generatedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
		attendee.setId((long) generatedId);
		return attendee;
	}

	@Override
	public <S extends Attendee> void update(final S attendee) {
		final var sql = "UPDATE \"attendee\" " +
						"SET display_name = :displayName, email = :email, category = :category " +
						"WHERE id = :id";
		final var params = new MapSqlParameterSource();
		params.addValue("id", attendee.getId());
		params.addValue("displayName", attendee.getDisplayName());
		params.addValue("email", attendee.getEmail());
		params.addValue("category", attendee.getCategory());
		jdbcTemplate.update(sql, params);
	}

	@Override
	public void deleteById(final Long id) {
		final var sql = "DELETE FROM \"attendee\" WHERE id = :id";
		final var params = new MapSqlParameterSource();
		params.addValue("id", id);
		jdbcTemplate.update(sql, params);
	}

}
