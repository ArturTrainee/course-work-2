package com.geekhub.secretaryhelperapp.eventattendee.model;

import java.util.List;
import java.util.stream.Collectors;

public class EventAttendeeRelationBuilder {

	public EventAttendeeRelation build(Long eventId, Long attendeeId) {
		return new EventAttendeeRelation().setEventId(eventId).setAttendeeId(attendeeId);
	}

	public List<EventAttendeeRelation> build(Long eventId, List<Long> attendeesIds) {
		return attendeesIds.stream()
				.map(attendeeId -> this.build(eventId, attendeeId))
				.collect(Collectors.toUnmodifiableList());
	}

}
