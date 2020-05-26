package com.geekhub.secretaryhelperapp.attendee.controller;

import com.geekhub.secretaryhelperapp.abstraction.mapper.Mapper;
import com.geekhub.secretaryhelperapp.attendee.model.*;
import com.geekhub.secretaryhelperapp.attendee.service.AttendeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AttendeeResource {

	private final AttendeeService attendeeService;

	private final Mapper<AttendeeDto, Attendee> attendeeMapper;

	@Autowired
	public AttendeeResource(final AttendeeService attendeeService) {
		this.attendeeService = attendeeService;
		this.attendeeMapper = new AttendeeMapper();
	}

	@GetMapping("/attendees/{attendeeId}")
	public ResponseEntity<AttendeeDto> getAttendee(@PathVariable long attendeeId) {
		final var attendee = attendeeService.getById(attendeeId);
		if (attendee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(attendeeMapper.mapToDto(attendee.get()), HttpStatus.OK);
	}

	@GetMapping("/attendees")
	public ResponseEntity<List<AttendeeDto>> getAttendees(@RequestParam(name = "ids") Optional<List<String>> ids,
			@RequestParam(name = "event") Optional<String> eventId) {
		return new ResponseEntity<>(attendeeMapper.mapToDtos(attendeeService.getByIdsAndEventId(ids, eventId)),
				HttpStatus.OK);
	}

}
