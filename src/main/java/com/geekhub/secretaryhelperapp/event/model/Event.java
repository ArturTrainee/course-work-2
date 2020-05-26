package com.geekhub.secretaryhelperapp.event.model;

import com.geekhub.secretaryhelperapp.abstraction.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class Event extends BaseEntity<Long> {

	private long calendarId;

	@NotEmpty
	private String title;

	@NotEmpty
	private String type;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime endDate;

	private LocalDateTime creationDate;

	@NotEmpty
	private String location;

	private EventImportance importanceLvl;

	private String description;

	private boolean isBusy;

	private boolean isReadonly;

	public Event() {
		this.title = "";
		this.type = "";
		this.creationDate = LocalDateTime.now();
		this.importanceLvl = EventImportance.UNIMPORTANT;
		this.description = "";
		this.isBusy = true;
	}

	@Override
	public Event setId(Long id) {
		super.setId(id);
		return this;
	}

	public long getCalendarId() {
		return this.calendarId;
	}

	public Event setCalendarId(long calendarId) {
		this.calendarId = calendarId;
		return this;
	}

	public String getTitle() {
		return this.title;
	}

	public Event setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getType() {
		return this.type;
	}

	public Event setType(String type) {
		this.type = type;
		return this;
	}

	public LocalDateTime getStartDate() {
		return this.startDate;
	}

	public Event setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
		return this;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public Event setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
		return this;
	}

	public LocalDateTime getCreationDate() {
		return this.creationDate;
	}

	public Event setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
		return this;
	}

	public String getLocation() {
		return this.location;
	}

	public Event setLocation(String location) {
		this.location = location;
		return this;
	}

	public EventImportance getImportanceLvl() {
		return this.importanceLvl;
	}

	public Event setImportanceLvl(EventImportance importanceLvl) {
		this.importanceLvl = importanceLvl;
		return this;
	}

	public String getDescription() {
		return this.description;
	}

	public Event setDescription(String description) {
		this.description = description;
		return this;
	}

	public boolean isBusy() {
		return this.isBusy;
	}

	public Event setBusy(boolean busy) {
		isBusy = busy;
		return this;
	}

	public boolean isReadonly() {
		return this.isReadonly;
	}

	public Event setReadonly(boolean readonly) {
		isReadonly = readonly;
		return this;
	}

}
