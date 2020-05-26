package com.geekhub.secretaryhelperapp.event.service;

import com.geekhub.secretaryhelperapp.event.model.Event;
import com.geekhub.secretaryhelperapp.event.model.EventsTableHeaders;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.List;
import java.util.stream.Stream;

public class EventsPdfTableEditor {

	public static void addTableHeaders(PdfPTable table) {
		Stream.of(EventsTableHeaders.values()).forEach(h -> {
			final var cell = new PdfPCell();
			cell.setBackgroundColor(BaseColor.WHITE);
			cell.setBorderWidth(2);
			cell.setPhrase(new Phrase(h.header));
			table.addCell(h.header);
		});
	}

	public static void addEventsToTable(PdfPTable table, List<Event> events) {
		int rowNumber = 1;
		for (final var event : events) {
			table.addCell(String.valueOf(rowNumber++));
			table.addCell(event.getTitle());
			table.addCell(event.getType());
			table.addCell(event.getStartDate().toString());
			table.addCell(event.getEndDate().toString());
			table.addCell(event.getLocation());
			table.addCell(event.getDescription());
		}
	}

}
