package com.geekhub.secretaryhelperapp.event.model;

import com.geekhub.secretaryhelperapp.abstraction.mapper.Mapper;

public class EventMapper implements Mapper<EventDto, Event> {

	@Override
	public Event mapToEntity(EventDto eventDto) {
		return new Event().setId(eventDto.getId())
				.setCalendarId(eventDto.getCalendarId())
				.setTitle(eventDto.getTitle())
				.setType(eventDto.getType())
				.setEndDate(eventDto.getEndDate())
				.setStartDate(eventDto.getStartDate())
				.setCreationDate(eventDto.getCreationDate())
				.setLocation(eventDto.getLocation())
				.setImportanceLvl(eventDto.getImportanceLvl())
				.setDescription(eventDto.getDescription())
				.setBusy(eventDto.isBusy())
				.setReadonly(eventDto.isReadonly());
	}

	@Override
	public EventDto mapToDto(Event event) {
		return new EventDto().setId(event.getId())
				.setCalendarId(event.getCalendarId())
				.setTitle(event.getTitle())
				.setType(event.getType())
				.setStartDate(event.getStartDate())
				.setEndDate(event.getEndDate())
				.setCreationDate(event.getCreationDate())
				.setLocation(event.getLocation())
				.setImportanceLvl(event.getImportanceLvl())
				.setDescription(event.getDescription())
				.setBusy(event.isBusy())
				.setReadonly(event.isReadonly());
	}

}
