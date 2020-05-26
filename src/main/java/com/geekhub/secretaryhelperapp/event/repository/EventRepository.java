package com.geekhub.secretaryhelperapp.event.repository;

import com.geekhub.secretaryhelperapp.abstraction.repository.CrudRepository;
import com.geekhub.secretaryhelperapp.event.model.Event;
import com.geekhub.secretaryhelperapp.event.model.EventRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class EventRepository implements CrudRepository<Event, Long> {

	public static final String SELECT_EVENT = "SELECT id, calendar_id, title, type, start_date, end_date, "
			+ "creation_date, location, importance_lvl, description, busy, readonly ";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	private final EventRowMapper eventRowMapper;

	@Autowired
	public EventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.eventRowMapper = new EventRowMapper();
	}

	@Override
	public boolean existsById(Long id) {
		final var sql = "SELECT EXISTS(SELECT 1 FROM \"event\" WHERE id = :id)";
		final var params = new MapSqlParameterSource();
		params.addValue("id", id);
		final var isExists = jdbcTemplate.queryForObject(sql, params, Boolean.TYPE);
		return isExists != null && isExists;
	}

	@Override
	public Optional<Event> getById(Long id) {
		final var sql = SELECT_EVENT + "FROM \"event\" WHERE id = :id";
		final var params = new MapSqlParameterSource();
		params.addValue("id", id);
		return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, eventRowMapper));
	}

	@Override
	public Iterable<Event> getAllByIds(Iterable<Long> ids) {
		final var sql = SELECT_EVENT + "FROM \"event\" WHERE id IN (:ids)";
		final var params = new MapSqlParameterSource();
		params.addValue("ids", ids);
		return jdbcTemplate.query(sql, params, eventRowMapper);
	}

	@Override
	public Iterable<Event> getAll() {
		final var sql = SELECT_EVENT + "FROM \"event\"";
		return jdbcTemplate.query(sql, eventRowMapper);
	}

	public List<Event> getByQuery(String query, MapSqlParameterSource params) {
		return jdbcTemplate.query(query, params, eventRowMapper);
	}

	public List<Event> getLastCreatedEvents(int amount) {
		final var sql = SELECT_EVENT + "FROM \"event\" ORDER BY creation_date, id DESC LIMIT :amount";
		final var params = new MapSqlParameterSource();
		params.addValue("amount", amount);
		return jdbcTemplate.query(sql, params, eventRowMapper);
	}

	@Override
	public Event save(Event event) {
		final var sql =
				"INSERT INTO \"event\" (calendar_id, title, type, start_date, end_date, creation_date, " +
						"location, importance_lvl, description, busy, readonly" +
						") " +
				"VALUES (:calendarId, :title, :type, :startDate, :endDate, :creationDate, " +
				":location, :importanceLvl, :description, :busy, :readonly" +
						") RETURNING id";
		final var params = buildEventParamsSource(event);
		final var keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(sql, params, keyHolder);
		final var generatedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
		return event.setId((long)generatedId);
	}

	@Override
	public void update(Event event) {
		final var sql =
				"UPDATE \"event\" " +
				"SET calendar_id = :calendarId, title = :title, type = :type, " +
				"start_date = :startDate, end_date = :endDate, creation_date = :creationDate, " +
				"location = :location, importance_lvl = :importanceLvl, description = :description, " +
				"busy = :busy, readonly = :readonly " + "WHERE id = :id";
		final var params = buildEventParamsSource(event);
		jdbcTemplate.update(sql, params);
	}

	@Override
	public void deleteById(Long id) {
		final var sql = "DELETE FROM \"event\" WHERE id = :id";
		final var params = new MapSqlParameterSource();
		params.addValue("id", id);
		jdbcTemplate.update(sql, params);
	}

	public void deleteEventsByCalendarId(long calendarId) {
		final var sql = "DELETE FROM \"event\" WHERE calendar_id = :calendarId";
		final var params = new MapSqlParameterSource();
		params.addValue("calendarId", calendarId);
		jdbcTemplate.update(sql, params);
	}

	private MapSqlParameterSource buildEventParamsSource(Event event) {
		final var params = new MapSqlParameterSource();
		params.addValue("id", event.getId());
		params.addValue("calendarId", event.getCalendarId());
		params.addValue("title", event.getTitle());
		params.addValue("type", event.getType());
		params.addValue("startDate", event.getStartDate());
		params.addValue("endDate", event.getEndDate());
		params.addValue("creationDate", event.getCreationDate());
		params.addValue("location", event.getLocation());
		params.addValue("importanceLvl", event.getImportanceLvl().lvl);
		params.addValue("description", event.getDescription());
		params.addValue("busy", event.isBusy());
		params.addValue("readonly", event.isReadonly());
		return params;
	}

}
