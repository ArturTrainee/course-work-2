$(document).ready(function () {

    function getAllShownRows() {
        const rows = [];
        $('#searchTable tr').each(function (i, obj) {
            rows.push($(obj).attr('id').split('-')[1]);
        });
        console.log(rows);
        return rows;
    }

    $('#add-event-btn').on('click', function (e) {
        window.location.href = `${window.location.pathname}/create`;
    });

    $('#select-all-calendars').on('click', function () {
        if (this.checked) {
            $('.cal-checkbox').prop('checked', true);
            $('.event-row').show();
        } else {
            $('.cal-checkbox').prop('checked', false);
            $('.event-row').hide();
        }
    });

    $('.cal-checkbox').on('click', function () {
        $('.cal-' + $(this).val()).toggle();
        if ($('.cal-checkbox:checked').length === $('.cal-checkbox').length) {
            $('#select-all-calendars').prop('checked', true);
        } else {
            $('#select-all-calendars').prop('checked', false);
        }
    });

    $('#select-all-attendees').on('click', function () {
        if (this.checked) {
            $('.att-checkbox').prop('checked', true);
        } else {
            $('.att-checkbox').prop('checked', false);
        }
    });

    $('.att-checkbox').on('click', function () {
        if ($('.att-checkbox:checked').length === $('.att-checkbox').length) {
            $('#select-all-attendees').prop('checked', true);
        } else {
            $('#select-all-attendees').prop('checked', false);
        }
    });

    $('#tableSearch').on("keyup", function () {
        const value = $(this).val().toLowerCase();
        $("#searchTable tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    function clearTableContent() {
        $('#searchTable tr').remove();
    }

    function pushCheckedBoxesToArray(selector) {
        const array = [];
        $(selector).each(function (i, obj) {
            if (obj.checked) {
                array.push($(obj).val());
            }
        });
        return array;
    }

    function generateUrlParamsFrom(selector, paramName) {
        const params = $(selector).val();
        return generateUrlParams(params, paramName);
    }

    function generateUrlParams(params, paramName) {
        return params ? `${paramName + '=' + params.split(' ').join(',')}` : '';
    }

    function formatLocalDateTimeStr(date) {
        return date.replace('T', ' ').substring(0, 16);
    }

    $('#search-btn').on('click', function (e) {
        const calendarsIds = pushCheckedBoxesToArray('.cal-checkbox');
        const attendeesIds = pushCheckedBoxesToArray('.att-checkbox');
        const url = `/api/events?q=true
            &${generateUrlParamsFrom('#tableSearch', 'keywords')}
            &${generateUrlParams(calendarsIds.join(','), 'cal-ids')}
            &${generateUrlParams(attendeesIds.join(','), 'att-ids')}
            &${generateUrlParamsFrom('#from-date', 'from')}
            &${generateUrlParamsFrom('#to-date', 'to')}`;

        $.ajax({
            type: 'GET',
            url: url,
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                console.log('resp', response);
                clearTableContent();

                const eventsTbody = $('#searchTable');
                $.each(response, function (index, e) {
                    eventsTbody.append(
                        `<tr id="event-${e.id}" class="event-row cal-${e.calendarId}">
                            <td>
                                <a href="/events/show/${e.id}" target="_blank">${e.title}</a>
                            </td>
                            <td>${formatLocalDateTimeStr(e.startDate)}</td>
                            <td>${formatLocalDateTimeStr(e.endDate)}</td>
                            <td>${e.location}</td>
                        </tr>`
                    )
                });
            },
            error: function (e) {
                console.log(e);
            }
        });
    });

    $('#save-pdf-btn').on('click', function () {
        window.location.href = `/api/events/pdf?ids=${getAllShownRows().join(',')}`;
    });

    $('#save-docx-btn').on('click', function () {
        window.location.href = `/api/events/docx?ids=${getAllShownRows().join(',')}`;
    });

    $('#save-xlsx-btn').on('click', function () {
        window.location.href = `/api/events/xlsx?ids=${getAllShownRows().join(',')}`;
    });
});