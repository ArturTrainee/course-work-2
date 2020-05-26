package com.geekhub.secretaryhelperapp.event.controller;

import com.geekhub.secretaryhelperapp.abstraction.mapper.Mapper;
import com.geekhub.secretaryhelperapp.attendee.model.Attendee;
import com.geekhub.secretaryhelperapp.attendee.model.AttendeeDto;
import com.geekhub.secretaryhelperapp.attendee.model.AttendeeMapper;
import com.geekhub.secretaryhelperapp.attendee.service.AttendeeService;
import com.geekhub.secretaryhelperapp.calendar.model.Calendar;
import com.geekhub.secretaryhelperapp.calendar.model.CalendarDto;
import com.geekhub.secretaryhelperapp.calendar.model.CalendarMapper;
import com.geekhub.secretaryhelperapp.calendar.service.CalendarService;
import com.geekhub.secretaryhelperapp.event.model.*;
import com.geekhub.secretaryhelperapp.event.service.EventService;
import com.geekhub.secretaryhelperapp.eventattendee.model.EventAttendeeRelation;
import com.geekhub.secretaryhelperapp.eventattendee.model.EventAttendeeRelationBuilder;
import com.geekhub.secretaryhelperapp.eventattendee.service.EventAttendeeRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventsController {

	private static final Logger logger = LoggerFactory.getLogger(EventsController.class);

	private final EventService eventService;

	private final CalendarService calendarService;

	private final AttendeeService attendeeService;

	private final EventAttendeeRelationService eventAttendeeRelationService;

	private final Mapper<EventDto, Event> eventMapper;

	private final Mapper<CalendarDto, Calendar> calendarMapper;

	private final Mapper<AttendeeDto, Attendee> attendeeMapper;

	private final EventAttendeeRelationBuilder eventAttendeeRelationBuilder;

	@Autowired
	public EventsController(final EventService eventService, final CalendarService calendarService,
			final AttendeeService attendeeService, final EventAttendeeRelationService eventAttendeeRelationService) {
		this.eventService = eventService;
		this.calendarService = calendarService;
		this.attendeeService = attendeeService;
		this.eventAttendeeRelationService = eventAttendeeRelationService;
		this.eventMapper = new EventMapper();
		this.calendarMapper = new CalendarMapper();
		this.attendeeMapper = new AttendeeMapper();
		this.eventAttendeeRelationBuilder = new EventAttendeeRelationBuilder();
	}

	@GetMapping
	public String getEventsPage(Model model) {
		model.addAttribute("calendars", calendarMapper.mapToDtos(calendarService.getAll()));
		model.addAttribute("attendees", attendeeMapper.mapToDtos(attendeeService.getAll()));
		model.addAttribute("events", eventMapper.mapToDtos(eventService.getLastCreated(10)));
		return "event/events-page";
	}

	@GetMapping("/create")
	public String getCreateEventPage(Model model) {
		model.addAttribute("event", new EventDto());
		model.addAttribute("calendars", calendarMapper.mapToDtos(calendarService.getAll()));
		model.addAttribute("eventTypes", EventType.values());
		model.addAttribute("attendees", attendeeMapper.mapToDtos(attendeeService.getAll()));
		model.addAttribute("importanceLvls", EventImportance.values());
		return "event/create-event";
	}

	@PostMapping("/create")
	public String createEvent(@RequestParam(value = "attendeesIds") List<Long> attendeesIds,
			@RequestParam(value = "isBusy", required = false) boolean isBusy,
			@RequestParam(value = "isReadonly", required = false) boolean isReadonly, @Valid Event event,
			BindingResult result) {
		if (result.hasErrors() || event.getStartDate().isAfter(event.getEndDate())) {
			final var msg = result.toString();
			logger.info("{}", msg);
			return "event/create-event";
		}
		event.setBusy(isBusy).setReadonly(isReadonly);
		final var savedEvent = eventService.save(event);
		final var relations = eventAttendeeRelationBuilder.build(savedEvent.getId(), attendeesIds);
		eventAttendeeRelationService.saveRelations(relations);
		return "redirect:/events/show/" + savedEvent.getId();
	}

	@GetMapping("/show/{eventId}")
	public String getShowEventPage(@PathVariable("eventId") long id, Model model) {
		final var event = eventService.getById(id);
		if (event.isEmpty()) {
			model.addAttribute("errorMsg", "Event not found");
			return "event/show-event";
		}
		model.addAttribute("event", eventMapper.mapToDto(event.get()));
		final var calendar = calendarService.getCalendarById(event.get().getCalendarId());
		calendar.ifPresent(c -> model.addAttribute("calendar", calendarMapper.mapToDto(c)));
		final var attendeesIds = eventAttendeeRelationService.getRelationsByEventId(id).stream()
				.map(EventAttendeeRelation::getAttendeeId)
				.collect(Collectors.toUnmodifiableList());
		model.addAttribute("attendees", attendeeMapper.mapToDtos(attendeeService.getByIds(attendeesIds)));
		return "event/show-event";
	}

	@GetMapping("/edit/{eventId}")
	public String getEventEditPage(@PathVariable("eventId") long id, Model model) {
		final var event = eventService.getById(id);
		if (event.isEmpty()) {
			return "redirect:/events";
		}
		model.addAttribute("event", eventMapper.mapToDto(event.get()));
		model.addAttribute("calendars", calendarMapper.mapToDtos(calendarService.getAll()));
		model.addAttribute("eventTypes", EventType.values());
		model.addAttribute("attendees", attendeeMapper.mapToDtos(attendeeService.getAll()));
		model.addAttribute("importanceLvls", EventImportance.values());
		return "event/edit-event";
	}

	@PostMapping("/update/{eventId}")
	public String updateEvent(@PathVariable("eventId") long id,
			@RequestParam(value = "attendeesIds") List<Long> attendeesIds,
			@RequestParam(value = "isBusy", required = false) boolean isBusy,
			@RequestParam(value = "isReadonly", required = false) boolean isReadonly, @Valid Event event,
			BindingResult result) {
		if (result.hasErrors() || event.getStartDate().isAfter(event.getEndDate())) {
			final var msg = result.toString();
			logger.info("{}", msg);
			return "redirect:/events/edit/" + id;
		}
		event.setBusy(isBusy).setReadonly(isReadonly);
		eventAttendeeRelationService.saveRelations(eventAttendeeRelationBuilder.build(event.getId(), attendeesIds));
		eventService.update(event);
		return "redirect:/events/show/" + id;
	}

}
