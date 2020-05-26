package com.geekhub.secretaryhelperapp.event.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EventCriteriaQueryBuilder {

	private static final String CALENDARS_CONSTRAIN = "calendar_id IN (:calendarsIds)";

	private final List<String> keywords;

	private final List<Long> calendarsIds;

	private final List<Long> attendeesIds;

	private final LocalDateTime from;

	private final LocalDateTime to;

	public EventCriteriaQueryBuilder(List<String> keywords, List<String> calendarsIds, List<String> attendeesIds,
									 String from, String to) {
		this.keywords = keywords == null ? Collections.emptyList() : keywords;
		this.calendarsIds = calendarsIds == null ? Collections.emptyList() : calendarsIds.stream()
					.map(Long::parseLong)
					.collect(Collectors.toList());
		this.attendeesIds = attendeesIds == null ? Collections.emptyList() : attendeesIds.stream()
				.map(Long::parseLong)
				.collect(Collectors.toList());

		final var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		final var now = LocalDateTime.now();
		this.from = from != null ? LocalDateTime.parse((from.strip() + "T00:00"), formatter) : now;
		this.to = to != null ? LocalDateTime.parse((to.strip() + "T23:59"), formatter) : now.plusYears(1);
	}

	public String createQuery(final MapSqlParameterSource params) {
		final var queryBuilder = new StringBuilder(
				EventRepository.SELECT_EVENT +
						"FROM \"event\" " +
						"WHERE start_date >= :from " +
						"AND end_date < :to"
		);
		params.addValue("from", this.from);
		params.addValue("to", this.to);

		if (!this.attendeesIds.isEmpty()) {
			queryBuilder.append(" AND ").append(buildAttendeeRelationsConstrain());
			params.addValue("attendeesIds", attendeesIds);
		}
		if (!this.calendarsIds.isEmpty()) {
			queryBuilder.append(" AND ").append(CALENDARS_CONSTRAIN);
			params.addValue("calendarsIds", calendarsIds);
		}
		if (!this.keywords.isEmpty()) {
			queryBuilder.append(" AND ").append(buildKeywordsConstrain(params));
		}
		return queryBuilder.toString();
	}

	private String buildAttendeeRelationsConstrain() {
		return "id IN (" +
				"SELECT event_id " +
				"FROM \"event_attendee_relation\" " +
				"WHERE attendee_id IN (:attendeesIds)" +
				")";
	}

	private String buildKeywordsConstrain(final MapSqlParameterSource params) {
		final var queryBuilder = new StringBuilder();
		int i = 1;
		for (final var keyword : keywords) {
			queryBuilder.append("strpos(lower(title), :word").append(i).append(") > 0").append(" OR ");
			params.addValue("word" + i, keyword);
			i++;
		}
		queryBuilder.setLength(queryBuilder.length() - 4);
		return queryBuilder.toString();
	}

}
