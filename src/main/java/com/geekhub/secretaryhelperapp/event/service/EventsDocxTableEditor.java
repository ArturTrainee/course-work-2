package com.geekhub.secretaryhelperapp.event.service;

import com.geekhub.secretaryhelperapp.event.model.Event;
import com.geekhub.secretaryhelperapp.event.model.EventsTableHeaders;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.util.List;

public class EventsDocxTableEditor {

	public static void addHeadersToTable(XWPFTable table) {
		final var headersRow = table.getRow(0);
		final var tableHeaders = EventsTableHeaders.values();
		headersRow.getCell(0).setText(tableHeaders[0].header);

		int headersLen = tableHeaders.length;
		for (int i = 1; i < headersLen; i++) {
			headersRow.addNewTableCell().setText(tableHeaders[i].header);
		}
	}

	public static void addEventsToTable(XWPFTable table, List<Event> events) {
		int i = 1;
		for (final var event : events) {
			final var row = table.createRow();
			row.getCell(0).setText(String.valueOf(i++));
			row.getCell(1).setText(event.getTitle());
			row.getCell(2).setText(event.getType());
			row.getCell(3).setText(event.getStartDate().toString());
			row.getCell(4).setText(event.getEndDate().toString());
			row.getCell(5).setText(event.getLocation());
			row.getCell(6).setText(event.getDescription());
		}
	}

}
