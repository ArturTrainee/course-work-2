package com.geekhub.secretaryhelperapp.event.service;

import com.geekhub.secretaryhelperapp.event.model.Event;
import com.geekhub.secretaryhelperapp.event.model.EventsTableHeaders;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Collection;

public class EventsXlsxTableEditor {

	public static void addHeadersToTable(XSSFRow row) {
		final var tableHeaders = EventsTableHeaders.values();
		XSSFCell cell;
		for (int i = 0; i < tableHeaders.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(tableHeaders[i].header);
		}
	}

	public static void addEventsToTable(XSSFSheet spreadsheet, Collection<Event> events, int startRow) {
		int rowIndex = startRow;
		for (final var event : events) {
			final XSSFRow row = spreadsheet.createRow(rowIndex++);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue(rowIndex);
			cell = row.createCell(1);
			cell.setCellValue(event.getTitle());
			cell = row.createCell(2);
			cell.setCellValue(event.getType());
			cell = row.createCell(3);
			cell.setCellValue(event.getStartDate().toString());
			cell = row.createCell(4);
			cell.setCellValue(event.getEndDate().toString());
			cell = row.createCell(5);
			cell.setCellValue(event.getLocation());
			cell = row.createCell(6);
			cell.setCellValue(event.getDescription());
		}
	}

}
