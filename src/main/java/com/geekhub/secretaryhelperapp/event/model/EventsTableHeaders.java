package com.geekhub.secretaryhelperapp.event.model;

public enum EventsTableHeaders {

	ROW("#"), TITLE("Title"), TYPE("Type"), START_DATE("Start date"), END_DATE("End date"), LOCATION(
			"Location"), DESCRIPTION("Description");

	public final String header;

	EventsTableHeaders(String header) {
		this.header = header;
	}

}
