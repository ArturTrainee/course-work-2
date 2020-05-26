package com.geekhub.secretaryhelperapp.event.service;

import com.geekhub.secretaryhelperapp.event.model.Event;
import com.geekhub.secretaryhelperapp.event.repository.EventCriteriaQueryBuilder;
import com.geekhub.secretaryhelperapp.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

	private final EventRepository eventRepository;

	@Autowired
	public EventService(final EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	public Optional<Event> getById(final Long id) {
		final var isExists = eventRepository.existsById(id);
		return isExists ? eventRepository.getById(id) : Optional.empty();
	}

	public List<Event> getAll() {
		return (List<Event>) eventRepository.getAll();
	}

	public List<Event> getLastCreated(final int amount) {
		return eventRepository.getLastCreatedEvents(amount);
	}

	public List<Event> getAllByIds(final List<Long> ids) {
		return ids.isEmpty() ? Collections.emptyList() : (List<Event>) eventRepository.getAllByIds(ids);
	}

	public List<Event> getByQueryCriteria(final EventCriteriaQueryBuilder queryCriteriaBuilder) {
		final var params = new MapSqlParameterSource();
		return eventRepository.getByQuery(queryCriteriaBuilder.createQuery(params), params);
	}

	@Transactional
	public Event save(final Event event) {
		if (event.getStartDate().isBefore(event.getEndDate())) {
			event.setCreationDate(LocalDateTime.now());
			return eventRepository.save(event);
		}
		return event;
	}

	@Transactional
	public void update(final Event event) {
		if (event.getStartDate().isBefore(event.getEndDate()) && eventRepository.existsById(event.getId())) {
			eventRepository.update(event);
		}
	}

	@Transactional
	public void deleteById(final Long id) {
		if (eventRepository.existsById(id)) {
			eventRepository.deleteById(id);
		}
	}

	@Transactional
	public void deleteAllByCalendarId(final long calendarId) {
		eventRepository.deleteEventsByCalendarId(calendarId);
	}

}
