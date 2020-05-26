package com.geekhub.secretaryhelperapp.calendar.repository;

import com.geekhub.secretaryhelperapp.abstraction.repository.CrudRepository;
import com.geekhub.secretaryhelperapp.calendar.model.Calendar;
import com.geekhub.secretaryhelperapp.calendar.model.CalendarRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
public class CalendarRepository implements CrudRepository<Calendar, Long> {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	private final CalendarRowMapper calendarRowMapper;

	@Autowired
	public CalendarRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.calendarRowMapper = new CalendarRowMapper();
	}

	@Override
	public boolean existsById(final Long id) {
		final var sql = "SELECT EXISTS(SELECT 1 FROM \"calendar\" WHERE id = :id)";
		final var params = new MapSqlParameterSource();
		params.addValue("id", id);
		final var isExists = jdbcTemplate.queryForObject(sql, params, Boolean.TYPE);
		return isExists != null && isExists;
	}

	@Override
	public Optional<Calendar> getById(final Long id) {
		final var sql = "SELECT id, name, text_color, bg_color FROM \"calendar\" WHERE id = :id";
		final var params = new MapSqlParameterSource();
		params.addValue("id", id);
		return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, calendarRowMapper));
	}

	@Override
	public Iterable<Calendar> getAllByIds(final Iterable<Long> ids) {
		final var sql = "SELECT id, name, text_color, bg_color FROM \"event\" WHERE id IN (:ids)";
		final var params = new MapSqlParameterSource();
		params.addValue("ids", ids);
		return jdbcTemplate.query(sql, params, calendarRowMapper);
	}

	@Override
	public Iterable<Calendar> getAll() {
		final var sql = "SELECT id, name, text_color, bg_color FROM \"calendar\" ORDER BY id";
		return jdbcTemplate.query(sql, calendarRowMapper);
	}

	@Override
	public Calendar save(final Calendar calendar) {
		final var sql =
				"INSERT INTO \"calendar\" (name, text_color, bg_color) " +
				"VALUES (:name, :textColor, :bgColor) RETURNING id";
		final var params = new MapSqlParameterSource();
		params.addValue("name", calendar.getName());
		params.addValue("textColor", calendar.getTextColor());
		params.addValue("bgColor", calendar.getBgColor());
		final var keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(sql, params, keyHolder);
		final var generatedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
		calendar.setId((long)generatedId);
		return calendar;
	}

	@Override
	public void update(final Calendar calendar) {
		final var sql = "UPDATE \"calendar\" SET text_color = :textColor, bg_color = :bgColor WHERE id = :id";
		final var params = new MapSqlParameterSource();
		params.addValue("id", calendar.getId());
		params.addValue("textColor", calendar.getTextColor());
		params.addValue("bgColor", calendar.getBgColor());
		jdbcTemplate.update(sql, params);
	}

	@Override
	public void deleteById(final Long id) {
		final var sql = "DELETE FROM \"calendar\" WHERE id = :id";
		final var params = new MapSqlParameterSource();
		params.addValue("id", id);
		jdbcTemplate.update(sql, params);
	}

}
