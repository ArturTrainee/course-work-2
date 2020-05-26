package com.geekhub.secretaryhelperapp.user.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		final var user = new User();
		user.setId(rs.getLong("id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setDisplayName(rs.getString("display_name"));
		user.setEmail(rs.getString("email"));
		user.setRoles(rs.getString("roles"));
		user.setDepartment(rs.getString("department"));
		user.setEnabled(rs.getBoolean("enabled"));
		return user;
	}

}
