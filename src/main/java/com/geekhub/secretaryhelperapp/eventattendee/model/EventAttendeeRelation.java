package com.geekhub.secretaryhelperapp.eventattendee.model;

public class EventAttendeeRelation {

	private long eventId;

	private long attendeeId;

	public long getEventId() {
		return eventId;
	}

	public EventAttendeeRelation setEventId(long eventId) {
		this.eventId = eventId;
		return this;
	}

	public long getAttendeeId() {
		return attendeeId;
	}

	public EventAttendeeRelation setAttendeeId(long attendeeId) {
		this.attendeeId = attendeeId;
		return this;
	}

}
