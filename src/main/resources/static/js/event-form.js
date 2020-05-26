$(document).ready(function () {
    const fromDateInput = $('#from-input');
    const toDateInput = $('#to-input');
    const locationInput = $('#location');
    const fromDateTip = $('#from-input-tip');
    const toDateTip = $('#to-input-tip');
    const invalidIntervalTip = $('#invalid-interval-tip');
    const locationConflictTip = $('#location-tip');
    const attendeesOptions = $('#attendeesIds option');
    const busyList = $('#busy-list');
    const allEvents = [];
    const defaultCreationDate = formatToClientDate(nowDate());
    let inpLocation = '';
    let fromDate = defaultCreationDate;
    let toDate = defaultCreationDate;
    let timeTips = [];

    function hasNoValue(element) {
        const val = element.val();
        return val === undefined || val === null || val === '';
    }

    if (hasNoValue(fromDateInput)) {
        fromDateInput.val(defaultCreationDate);
    }
    if (hasNoValue(toDateInput)) {
        toDateInput.val(defaultCreationDate);
    }

    $('#submit-btn').on('click', function (e) {
        fromDateInput.val(formatToISODate(fromDateInput.val()));
        toDateInput.val(formatToISODate(toDateInput.val()));
    });

    function displayElement(element) {
        element.css('display', 'block');
    }

    function hideElement(element) {
        element.css('display', 'none');
    }

    function sendGetAllEventsRequest() {
        $.ajax({
            type: 'GET',
            url: '/api/events',
            success: function (events) {
                const eventsLen = events.length;
                for (let i = 0; i < eventsLen; i++) {
                    allEvents.push(events[i]);
                }
            },
            error: function (xhr) {
                console.log(xhr);
            }
        });
    }

    sendGetAllEventsRequest();

    function nowDate() {
        const date = new Date();
        return new Date(date.setHours(date.getHours() + 3));
    }

    function formatToClientDate(date) {
        return date
            .toISOString()
            .replace('T', ', ')
            .substring(0, 17);
    }

    function formatToISODate(date) {
        return date
            .substring(0, 17)
            .replace(', ', 'T')
            .replace(/\./g, '-');
    }

    function formatToISOWithSec(date) {
        return date
            .substring(0, 17)
            .replace(', ', 'T')
            .replace(/\./g, '-')
            .concat(':00');
    }

    function formatToRequestParamDate(date) {
        return date.substring(0, 10).replace(/\./g, '-');
    }

    function generateAttendeeBusyPeriodsTip(events) {
        let html = ``;
        for (let i = 0; i < events.length; i++) {
            html += `
                <div style="margin: 5px 15px">
                    <span> From <strong>${moment(events[i].startDate).format('DD.MM.YYYY, HH:mm')}</strong></span>
                    <span> To <strong>${moment(events[i].endDate).format('DD.MM.YYYY, HH:mm')}</strong></span>
                </div>`;
        }
        return html;
    }

    function generateLocationBusyPeriodsHtml(events) {
        let html = ``;
        for (let i = 0; i < events.length; i++) {
            html += `
                <div style="margin: 5px 15px">
                    <span> From <strong>${moment(events[i].startDate).format('DD.MM.YYYY, HH:mm')}</strong></span>
                    <span> To <strong>${moment(events[i].endDate).format('DD.MM.YYYY, HH:mm')}</strong></span>
                </div>`;
        }
        return html;
    }

    function createTimeConflictsArray(inputStart, inputEnd) {
        const conflictsArr = [];
        const eventsCount = allEvents.length;
        for (let i = 0; i < eventsCount; i++) {
            const e = allEvents[i];
            const eventStart = moment(e.startDate).format('DD.MM.YYYY, HH:mm');
            const eventEnd = moment(e.endDate).format('DD.MM.YYYY, HH:mm');
            if (e.location !== inpLocation
                || (inputStart < eventStart && inputEnd < eventStart)
                || (inputStart > eventEnd && inputEnd > eventEnd)
            ) {
                continue;
            }
            conflictsArr.push(e);
        }
        return conflictsArr;
    }

    function sendGetEventsByAttendeeRequest(attendeeId, attendeeName) {
        if (!attendeeId) return;
        $.ajax({
            type: 'GET',
            url: `/api/events?q=true
            &att-ids=${attendeeId}
            &from=${formatToRequestParamDate(fromDate)}
            &to=${formatToRequestParamDate(toDate)}`,
            success: function (events) {
                console.log('Attendee events resp', events);
                const attendeeWithEvents = {
                    attendeeId: attendeeId,
                    events: events
                };
                timeTips.push(attendeeWithEvents);
                if (events.length > 0) {
                    displayElement(busyList);
                    busyList.append(
                        `<div class="tip">
                            <strong>${attendeeName} is busy:</strong>
                            ${generateAttendeeBusyPeriodsTip(events)}
                        </div>`
                    );
                }
            },
            error: function (xhr) {
                console.log(xhr);
            }
        });
    }

    $('#check-conflicts-btn').on('click', function (e) {
        e.preventDefault();
        const selectedAttendees = [];
        attendeesOptions.each(function () {
            const attendeeOption = $(this);
            if (attendeeOption.is(':selected')) {
                const attendee = {
                    id: parseInt(attendeeOption.val(), 10),
                    name: attendeeOption.text()
                };
                selectedAttendees.push(attendee);
            }
        });
        for (let i = 0; i < selectedAttendees.length; i++) {
            if (!timeTips.find((tip) => tip.attendeeId === selectedAttendees[i].id)) {
                sendGetEventsByAttendeeRequest(selectedAttendees[i].id, selectedAttendees[i].name);
            }
        }
        console.log(timeTips);
    });

    function isValidTimePeriod(from, to) {
        let isValid = true;
        if (from.toString() === 'Invalid date') {
            displayElement(fromDateTip);
            isValid = false;
        }
        if (to.toString() === 'Invalid date') {
            displayElement(toDateTip);
            isValid = false;
        }
        if (from > to) {
            displayElement(invalidIntervalTip);
            isValid = false;
        }
        return isValid;
    }

    function renderLocationConflictTips() {
        let inputStart = moment(formatToISOWithSec(fromDate)).format('DD.MM.YYYY, HH:mm');
        let inputEnd = moment(formatToISOWithSec(toDate)).format('DD.MM.YYYY, HH:mm');

        if (isValidTimePeriod(inputStart, inputEnd)) {
            const conflictEvents = createTimeConflictsArray(inputStart, inputEnd);
            if (conflictEvents.length > 0) {
                console.log(conflictEvents);
                displayElement(locationConflictTip);
                locationConflictTip.append(
                    `<div class="location-tip">
                    <strong>${inpLocation} is busy:</strong>
                    ${generateLocationBusyPeriodsHtml(conflictEvents)}
                </div>`
                );
            }
        }
    }

    function removeTimeTips(dateTip) {
        timeTips = [];
        $('.tip').remove();
        hideElement(busyList);
        hideElement(dateTip);
        hideElement(invalidIntervalTip);
    }

    function removeLocationTips() {
        hideElement(locationConflictTip);
        $('.location-tip').remove();
    }

    fromDateInput.bind('input', function (e) {
        fromDate = $(this).val();
        removeTimeTips(fromDateTip);
        removeLocationTips();
        renderLocationConflictTips();
    });

    toDateInput.bind('input', function (e) {
        toDate = $(this).val();
        removeTimeTips(toDateTip);
        removeLocationTips();
        renderLocationConflictTips();
    });

    locationInput.bind('input', function (e) {
        inpLocation = $(this).val();
        removeLocationTips();
        renderLocationConflictTips();
    });
});