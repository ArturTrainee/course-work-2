package com.geekhub.secretaryhelperapp.calendar.service;

import com.geekhub.secretaryhelperapp.calendar.model.Calendar;
import com.geekhub.secretaryhelperapp.calendar.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {

	private final CalendarRepository calendarRepository;

	@Autowired
	public CalendarService(final CalendarRepository calendarRepository) {
		this.calendarRepository = calendarRepository;
	}

	public Optional<Calendar> getCalendarById(final Long id) {
		return calendarRepository.getById(id);
	}

	public List<Calendar> getAll() {
		return (List<Calendar>) calendarRepository.getAll();
	}

	@Transactional
	public Calendar save(final Calendar calendar) {
		return calendarRepository.save(calendar);
	}

	@Transactional
	public void update(final Calendar calendar) {
		calendarRepository.update(calendar);
	}

	@Transactional
	public void deleteCalendarById(final Long id) {
		calendarRepository.deleteById(id);
	}

}
