package com.geekhub.secretaryhelperapp.user.repository;

import com.geekhub.secretaryhelperapp.user.model.User;
import com.geekhub.secretaryhelperapp.user.model.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

	private static final String SELECT_USER =
			"SELECT id, username, password, display_name, email, roles, department, enabled ";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	private final UserRowMapper userRowMapper;

	@Autowired
	public UserRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.userRowMapper = new UserRowMapper();
	}

	public Optional<User> getUserByUsername(final String username) {
		final var sql = SELECT_USER + "FROM \"user\" WHERE username = :username";
		final var params = new MapSqlParameterSource();
		params.addValue("username", username);
		try {
			return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, userRowMapper));
		}
		catch (Exception e) {
			return Optional.empty();
		}
	}

	public Optional<User> getUserByEmail(final String email) {
		final var sql = SELECT_USER + "FROM \"user\" WHERE email = :email";
		final var params = new MapSqlParameterSource();
		params.addValue("email", email);
		try {
			return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, userRowMapper));
		}
		catch (Exception e) {
			return Optional.empty();
		}
	}

	public void save(User user) {
		final var sql =
				"INSERT INTO \"user\" (username, password, display_name, email, roles, department, enabled) " +
				"VALUES (:username, :password, :displayName, :email, :roles, :department, :enabled)";
		final var params = new MapSqlParameterSource();
		params.addValue("username", user.getUsername());
		params.addValue("password", user.getPassword());
		params.addValue("displayName", user.getDisplayName());
		params.addValue("email", user.getEmail());
		params.addValue("roles", user.getRoles());
		params.addValue("department", user.getDepartment());
		params.addValue("enabled", user.isEnabled());
		jdbcTemplate.update(sql, params);
	}

}
