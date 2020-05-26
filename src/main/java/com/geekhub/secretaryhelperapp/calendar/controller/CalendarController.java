package com.geekhub.secretaryhelperapp.calendar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {

	@GetMapping("/calendars")
	public String getCalendarPage() {
		return "calendar/calendar-page";
	}

}
