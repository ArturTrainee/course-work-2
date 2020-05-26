package com.geekhub.secretaryhelperapp.event.model;

import com.geekhub.secretaryhelperapp.abstraction.dto.AbstractDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class EventDto extends AbstractDto {

	private long id;

	private long calendarId;

	@NotEmpty
	private String title;

	@NotEmpty
	private String type;

	@NotNull
	private LocalDateTime startDate;

	@NotNull
	private LocalDateTime endDate;

	private LocalDateTime creationDate;

	@NotEmpty
	private String location;

	@NotNull
	private EventImportance importanceLvl;

	private String description;

	private boolean isBusy;

	private boolean isReadonly;

	public long getId() {
		return id;
	}

	public EventDto setId(long id) {
		this.id = id;
		return this;
	}

	public long getCalendarId() {
		return calendarId;
	}

	public EventDto setCalendarId(long calendarId) {
		this.calendarId = calendarId;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public EventDto setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getType() {
		return type;
	}

	public EventDto setType(String type) {
		this.type = type;
		return this;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public EventDto setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
		return this;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public EventDto setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
		return this;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public EventDto setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public EventDto setLocation(String location) {
		this.location = location;
		return this;
	}

	public EventImportance getImportanceLvl() {
		return importanceLvl;
	}

	public EventDto setImportanceLvl(EventImportance importanceLvl) {
		this.importanceLvl = importanceLvl;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public EventDto setDescription(String description) {
		this.description = description;
		return this;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public EventDto setBusy(boolean busy) {
		isBusy = busy;
		return this;
	}

	public boolean isReadonly() {
		return isReadonly;
	}

	public EventDto setReadonly(boolean readonly) {
		isReadonly = readonly;
		return this;
	}

}
