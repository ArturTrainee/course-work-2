package com.geekhub.secretaryhelperapp.attendee.model;

import com.geekhub.secretaryhelperapp.abstraction.mapper.Mapper;

public class AttendeeMapper implements Mapper<AttendeeDto, Attendee> {

	@Override
	public Attendee mapToEntity(AttendeeDto attendeeDto) {
		return new Attendee().setId(attendeeDto.getId()).setDisplayName(attendeeDto.getDisplayName())
				.setEmail(attendeeDto.getEmail()).setCategory(attendeeDto.getCategory());
	}

	@Override
	public AttendeeDto mapToDto(Attendee attendee) {
		return new AttendeeDto().setId(attendee.getId()).setDisplayName(attendee.getDisplayName())
				.setEmail(attendee.getEmail()).setCategory(attendee.getCategory());
	}

}
