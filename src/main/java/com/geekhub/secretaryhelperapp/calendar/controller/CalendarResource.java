package com.geekhub.secretaryhelperapp.calendar.controller;

import com.geekhub.secretaryhelperapp.abstraction.mapper.Mapper;
import com.geekhub.secretaryhelperapp.calendar.model.Calendar;
import com.geekhub.secretaryhelperapp.calendar.model.CalendarDto;
import com.geekhub.secretaryhelperapp.calendar.model.CalendarMapper;
import com.geekhub.secretaryhelperapp.calendar.service.CalendarService;
import com.geekhub.secretaryhelperapp.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CalendarResource {

	private final CalendarService calendarService;

	private final EventService eventService;

	private final Mapper<CalendarDto, Calendar> calendarMapper;

	@Autowired
	public CalendarResource(final CalendarService calendarService, final EventService eventService) {
		this.calendarService = calendarService;
		this.eventService = eventService;
		this.calendarMapper = new CalendarMapper();
	}

	@GetMapping("/calendars")
	@ResponseStatus(HttpStatus.OK)
	public List<CalendarDto> getAllCalendars() {
		return calendarMapper.mapToDtos(calendarService.getAll());
	}

	@PostMapping("/calendars")
	public ResponseEntity<CalendarDto> addCalendar(@RequestBody Calendar calendar) {
		return new ResponseEntity<>(calendarMapper.mapToDto(calendarService.save(calendar)), HttpStatus.CREATED);
	}

	@PutMapping("/calendars/{calendarId}")
	public ResponseEntity<HttpStatus> updateCalendar(@PathVariable long calendarId, @RequestBody Calendar calendar) {
		if (calendarService.getCalendarById(calendarId).isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		calendarService.update(calendar);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/calendars/{calendarId}")
	public ResponseEntity<HttpStatus> deleteCalendar(@PathVariable long calendarId) {
		if (calendarService.getCalendarById(calendarId).isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		eventService.deleteAllByCalendarId(calendarId);
		calendarService.deleteCalendarById(calendarId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
