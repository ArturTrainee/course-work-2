package com.geekhub.secretaryhelperapp.attendee.service;

import com.geekhub.secretaryhelperapp.attendee.model.Attendee;
import com.geekhub.secretaryhelperapp.attendee.repository.AttendeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendeeService {

	private final AttendeeRepository attendeeRepository;

	@Autowired
	public AttendeeService(final AttendeeRepository attendeeRepository) {
		this.attendeeRepository = attendeeRepository;
	}

	public List<Attendee> getByIds(final List<Long> attendeesIds) {
		return attendeesIds.isEmpty() ? Collections.emptyList() : (List<Attendee>) attendeeRepository.getAllByIds(attendeesIds);
	}

	public List<Attendee> getAll() {
		return (List<Attendee>) attendeeRepository.getAll();
	}

	public Optional<Attendee> getById(final long id) {
		if (attendeeRepository.existsById(id)) {
			return attendeeRepository.getById(id);
		}
		return Optional.empty();
	}

	public List<Attendee> getByEventId(final long eventId) {
		return (List<Attendee>) attendeeRepository.getAllByEventId(eventId);
	}

	public Collection<Attendee> getByIdsAndEventId(Optional<List<String>> ids, Optional<String> eventId) {
		final boolean selectByIds = ids.isPresent();
		final boolean selectByEvent = eventId.isPresent();
		if (!selectByEvent && selectByIds) {
			return getByIds(ids.get().stream()
					.map(Long::parseLong)
					.collect(Collectors.toUnmodifiableList()));
		}
		if (!selectByIds && selectByEvent) {
			return getByEventId(Long.parseLong(eventId.get()));
		}
		return getAll();
	}
}
