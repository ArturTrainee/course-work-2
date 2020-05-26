package com.geekhub.secretaryhelperapp.event.controller;

import com.geekhub.secretaryhelperapp.attendee.model.Attendee;
import com.geekhub.secretaryhelperapp.attendee.service.AttendeeService;
import com.geekhub.secretaryhelperapp.event.model.Event;
import com.geekhub.secretaryhelperapp.event.model.EventDto;
import com.geekhub.secretaryhelperapp.event.model.EventMapper;
import com.geekhub.secretaryhelperapp.event.repository.EventCriteriaQueryBuilder;
import com.geekhub.secretaryhelperapp.event.service.EventService;
import com.geekhub.secretaryhelperapp.event.service.EventsDocxTableEditor;
import com.geekhub.secretaryhelperapp.event.service.EventsPdfTableEditor;
import com.geekhub.secretaryhelperapp.event.service.EventsXlsxTableEditor;
import com.geekhub.secretaryhelperapp.eventattendee.model.EventAttendeeRelation;
import com.geekhub.secretaryhelperapp.eventattendee.service.EventAttendeeRelationService;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class EventsResource {

    private final EventService eventService;

    private final AttendeeService attendeeService;

    private final EventAttendeeRelationService eventAttendeeRelationService;

    private final EventMapper eventMapper;

    public EventsResource(final EventService eventService, final AttendeeService attendeeService,
                          final EventAttendeeRelationService eventAttendeeRelationService) {
        this.eventService = eventService;
        this.attendeeService = attendeeService;
        this.eventAttendeeRelationService = eventAttendeeRelationService;
        this.eventMapper = new EventMapper();
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> getEventsByRequestParams(
            @RequestParam(name = "q", required = false, defaultValue = "false") boolean useQuery,
            @RequestParam(name = "keywords", required = false) List<String> keywords,
            @RequestParam(name = "cal-ids", required = false) List<String> calIds,
            @RequestParam(name = "att-ids", required = false) List<String> attIds,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to) {
        if (useQuery) {
            return eventMapper.mapToDtos(eventService.getByQueryCriteria(
                    new EventCriteriaQueryBuilder(keywords, calIds, attIds, from, to)
                    )
            );
        }
        return eventMapper.mapToDtos(eventService.getAll());
    }

    @GetMapping("/events/{eventId}/attendees")
    public ResponseEntity<List<Attendee>> getEventAttendees(@PathVariable long eventId) {
        final var relations = eventAttendeeRelationService.getRelationsByEventId(eventId);
        if (relations.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(attendeeService.getByIds(relations.stream()
                .map(EventAttendeeRelation::getAttendeeId)
                .collect(Collectors.toUnmodifiableList())),
                HttpStatus.OK
        );
    }

    @PostMapping("/events")
    public ResponseEntity<EventDto> addEventToCalendar(@RequestBody Event event) {
        return new ResponseEntity<>(eventMapper.mapToDto(eventService.save(event)), HttpStatus.CREATED);
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<HttpStatus> updateEvent(@PathVariable long eventId, @RequestBody Event event) {
        if (eventService.getById(eventId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        eventService.update(event);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable long eventId) {
        if (eventService.getById(eventId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        eventAttendeeRelationService.deleteRelationsByEventId(eventId);
        eventService.deleteById(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/events/pdf")
    public ResponseEntity<ByteArrayResource> saveInPdf(@RequestParam(name = "ids") List<String> ids) {
        if (ids.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try (final var stream = new ByteArrayOutputStream()) {
            final var document = new Document();
            PdfWriter.getInstance(document, stream);

            document.open();
            final var selectedEvents = eventService.getAllByIds(ids.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toUnmodifiableList())
            );
            final var table = new PdfPTable(7);
            EventsPdfTableEditor.addTableHeaders(table);
            EventsPdfTableEditor.addEventsToTable(table, selectedEvents);
            document.add(table);
            document.close();

            final var resource = new ByteArrayResource(stream.toByteArray());
            final var header = new HttpHeaders();
            header.setContentType(new MediaType("application", "octet-stream"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=events.pdf");
            return new ResponseEntity<>(resource, header, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/docx")
    public ResponseEntity<ByteArrayResource> saveInDocx(@RequestParam(name = "ids") List<String> ids) {
        if (ids.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try (final var stream = new ByteArrayOutputStream(); final var document = new XWPFDocument()) {
            final var header = new HttpHeaders();
            header.setContentType(new MediaType("application", "octet-stream"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=events.docx");

            final var events = eventService.getAllByIds(ids.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toUnmodifiableList())
            );
            final var table = document.createTable();
            EventsDocxTableEditor.addHeadersToTable(table);
            EventsDocxTableEditor.addEventsToTable(table, events);
            document.write(stream);

            final var resource = new ByteArrayResource(stream.toByteArray());
            return new ResponseEntity<>(resource, header, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/xlsx")
    public ResponseEntity<ByteArrayResource> saveInXlsx(@RequestParam(name = "ids") List<String> ids) {
        if (ids.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try (final var stream = new ByteArrayOutputStream(); final var workbook = new XSSFWorkbook()) {
            final var header = new HttpHeaders();
            header.setContentType(new MediaType("application", "force-download"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=events.xlsx");

            final var spreadsheet = workbook.createSheet("events");
            final var headersRow = spreadsheet.createRow(0);
            EventsXlsxTableEditor.addHeadersToTable(headersRow);
            final var events = eventService.getAllByIds(ids.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toUnmodifiableList())
            );
            EventsXlsxTableEditor.addEventsToTable(spreadsheet, events, 1);
            workbook.write(stream);

            final var resource = new ByteArrayResource(stream.toByteArray());
            return new ResponseEntity<>(resource, header, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
