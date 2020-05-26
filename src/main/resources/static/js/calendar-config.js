$(document).ready(function () {
    const CALENDAR_THEME = {
        'common.border': '1px solid #ffbb3b',
        'common.backgroundColor': '#ffbb3b0f',
        'common.holiday.color': '#f54f3d',
        'common.saturday.color': '#f54f3d',
        'common.dayname.color': '#333'
    };
    var CalendarList = [],
        ScheduleSet = new Set(),
        calendarToUpdateId;

    function CalendarInfo() {
        this.id = null;
        this.name = null;
        this.checked = true;
        this.color = null;
        this.bgColor = null;
        this.borderColor = null;
        this.dragBgColor = null;
    }

    function showErrorModal(msg) {
        $('#ErrorDescription').text(msg);
        $('#errorModal').modal('show');
    }

    function findCalendarById(id) {
        const searchId = String(id);
        let searchResult = null;
        CalendarList.forEach(function (calendar) {
            if (calendar.id === searchId) {
                searchResult = calendar;
            }
        });
        searchResult = (searchResult === null ? CalendarList[0] : searchResult);
        return searchResult;
    }

    function findEvent(title) {
        let searchResult = null;
        ScheduleSet.forEach(function (event) {
            if (event.title === title) {
                searchResult = event;
            }
        });
        searchResult = (searchResult === null ? ScheduleSet.values().next().value : searchResult);
        return searchResult;
    }

    function findEventById(id) {
        let searchResult = null;
        ScheduleSet.forEach(function (event) {
            if (event.id === id) {
                searchResult = event;
            }
        });
        searchResult = (searchResult === null ? ScheduleSet.values().next().value : searchResult);
        return searchResult;
    }

    function createCalendar(id, name, textColor, bgColor) {
        const calendar = new CalendarInfo();
        calendar.id = String(id);
        calendar.name = name;
        calendar.color = textColor;
        calendar.bgColor = bgColor;
        calendar.dragBgColor = bgColor;
        calendar.borderColor = bgColor;
        return calendar;
    }

    function createSchedule(e) {
        const calendar = findCalendarById(e.calendarId);
        const daysDuration = moment.duration(moment(e.endDate).diff(e.startDate)).asDays();
        const id = (e.id === undefined ? chance.guid() : e.id);

        return {
            id: String(id),
            calendarId: calendar.id,
            title: e.title,
            body: e.description,
            start: e.startDate || e.start,
            end: e.endDate || e.end,
            isAllDay: daysDuration > 1,
            category: (daysDuration > 1) ? 'allday' : 'time',
            dueDateClass: '',
            location: e.location,
            //attendees: [],
            isVisible: true,
            isReadOnly: e.readonly,
            color: calendar.color,
            bgColor: calendar.bgColor,
            dragBgColor: calendar.bgColor,
            borderColor: calendar.bgColor,
            raw: null,
            state: e.state || ((e.busy === true) ? 'Busy' : 'Free')
        };
    }

    function getCsrfTokenAndHeader() {
        return {token: $('#csrfToken').val(), header: $('#csrfHeader').val()};
    }

    function sendCalendarCreationRequest(event) {
        event.preventDefault();
        const calendarName = $('#cal-name').val().trim();
        const textColor = $('#text-color').val();
        const bgColor = $('#bg-color').val();

        let calendar = {
            id: 0,
            name: calendarName,
            textColor: textColor,
            bgColor: bgColor,
        };
        $('#createCalModal').modal('hide');

        $.ajax({
            type: 'POST',
            url: '/api/calendars',
            dataType: 'json',
            data: JSON.stringify(calendar),
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
                const csrfAttr = getCsrfTokenAndHeader();
                xhr.setRequestHeader(csrfAttr.header, csrfAttr.token);
            },
            success: function (response) {
                console.log(response);
                calendar = response;
                CalendarList.push(createCalendar(calendar.id, calendar.name, calendar.textColor, calendar.bgColor));
                renderCalendarsList();
            },
            error: function(xhr) {
                if (xhr.status === 405) {
                    showErrorModal('Access Denied');
                } else {
                    showErrorModal(`Error ${xhr.status}`);
                }
            }
        });
    }

    function sendCalendarDeletionRequest(e) {
        confirm('Delete this calendar?');

        const calendar = findCalendarById(calendarToUpdateId);

        $.ajax({
            url: ('/api/calendars/' + calendar.id),
            type: 'DELETE',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
                const csrfAttr = getCsrfTokenAndHeader();
                xhr.setRequestHeader(csrfAttr.header, csrfAttr.token);
            },
            success: function (response) {
                console.log('cal del resp', response);
                CalendarList = jQuery.grep(CalendarList, function (value) {
                    return value !== calendar;
                });
                renderCalendarsList();
                refreshSchedules();
                cal.render(true);
            },
            error: function(xhr) {
                if (xhr.status === 405) {
                    showErrorModal('Access Denied');
                } else {
                    showErrorModal(`Error ${xhr.status}`);
                }
            }
        });
    }

    function renderCalendarColorChange() {
        cal.render(true);
        refreshSchedules();
        renderCalendarsList();
    }

    function sendCalendarUpdateRequest(e) {
        $('#editCalModal').modal('hide');
        let calendar = findCalendarById(calendarToUpdateId);
        calendar.textColor = $('#new-text-color').val();
        calendar.bgColor = $('#new-bg-color').val();

        $.ajax({
            url: `/api/calendars/${calendar.id}`,
            type: 'PUT',
            data: JSON.stringify(calendar),
            async: false,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "text/csv");
                xhr.setRequestHeader("Content-Type", "application/json");
                const csrfAttr = getCsrfTokenAndHeader();
                xhr.setRequestHeader(csrfAttr.header, csrfAttr.token);
            },
            success: function (response) {
                renderCalendarColorChange();
            },
            error: function(xhr) {
                if (xhr.status === 405) {
                    showErrorModal('Access Denied');
                } else {
                    showErrorModal(`Error ${xhr.status}`);
                }
            }
        });
    }

    function sendEventCreationRequest(e) {
        let event = {
            id: 0,
            calendarId: e.calendarId,
            title: e.title,
            type: 'meeting',
            startDate: moment(e.start._date).add(3, 'hours').toISOString(),
            endDate: moment(e.end._date).add(3, 'hours').toISOString(),
            creationDate: (moment().add(1, 'hours'))._d,
            location: e.location,
            importanceLvl: 'UNIMPORTANT',
            description: '',
            isBusy: (e.state === 'Busy'),
            isReadonly: e.raw.class !== 'public'
        };
        $.ajax({
            type: 'POST',
            url: '/api/events',
            dataType: 'json',
            data: JSON.stringify(event),
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
                const csrfAttr = getCsrfTokenAndHeader();
                xhr.setRequestHeader(csrfAttr.header, csrfAttr.token);
            },
            success: function (response) {
                console.log(response);
                event = createSchedule(response);
                ScheduleSet.add(event);
                cal.createSchedules([event], true);
                cal.render(true);
            },
            error: function(xhr) {
                if (xhr.status === 405) {
                    showErrorModal('Access Denied');
                } else {
                    showErrorModal(`Error ${xhr.status}`);
                }
            }
        });
    }

    function renderCalendarsList() {
        var calendarList = document.getElementById('calendarList');
        var html = [];
        CalendarList.forEach(function (calendar) {
            html.push(
                `<div class="lnb-calendars-item" id="${calendar.id}" 
                    style="display: flex;align-items: start;justify-content: space-between;">
                    <label>
                        <div style="display:flex;justify-content: space-between">
                            <input type="checkbox" class="tui-full-calendar-checkbox-round" value="${calendar.id}" checked>
                            <div style="border-color: ${calendar.borderColor};background-color: ${calendar.borderColor};
                                min-width: 15px;max-height: 15px;border-radius: 50%;margin-right: 3px;"></div>
                            <div>${calendar.name}</div>
                        </div>
                    </label>
                    <a class="btn-show-cal-edit-modal" id="${calendar.id}" style="cursor: pointer;display: flex">
                            <img id="${calendar.id}" alt="edit" src="/img/edit.svg" style="max-width: 20px; max-height: 24px"/>
                    </a>
                </div>`
            );
        });
        calendarList.innerHTML = html.join('\n');
    }

    function refreshSchedules() {
        const calendarElements = Array.prototype.slice.call(
            document.querySelectorAll('#calendarList input')
        );
        CalendarList.forEach(function (calendar) {
            cal.toggleSchedules(calendar.id, !calendar.checked, false);
        });
        cal.render(true);
        calendarElements.forEach(function (input) {
            const span = input.nextElementSibling;
            span.style.backgroundColor = input.checked ? span.style.borderColor : 'transparent';
        });
    }

    function getTimeTemplate(schedule, isAllDay) {
        var html = [];
        const start = moment(schedule.start.toUTCString());
        if (!isAllDay) {
            html.push('<strong>' + start.format('HH:mm') + '</strong> ');
        }
        if (schedule.isPrivate) {
            html.push('<span class="calendar-font-icon ic-lock-b"></span>');
            html.push(' Private');
        } else {
            if (schedule.isReadOnly) {
                html.push('<span class="calendar-font-icon ic-readonly-b"></span>');
            } else if (schedule.recurrenceRule) {
                html.push('<span class="calendar-font-icon ic-repeat-b"></span>');
            } else if (schedule.attendees.length) {
                html.push('<span class="calendar-font-icon ic-user-b"></span>');
            } else if (schedule.location) {
                html.push('<span class="calendar-font-icon ic-location-b"></span>');
            }
            html.push(' ' + schedule.title);
        }
        return html.join('');
    }

    (function (window, Calendar) {
        var resizeThrottled, selectedCalendar;

        var cal = new Calendar('#calendar', {
            defaultView: 'week',
            taskView: false,
            scheduleView: 'time',
            calendars: CalendarList,
            template: {
                milestone: function (model) {
                    return '<span class="calendar-font-icon ic-milestone-b"></span> <span style="background-color: ' + model.bgColor + '">' + model.title + '</span>';
                },
                allday: function (schedule) {
                    return getTimeTemplate(schedule, true);
                },
                time: function (schedule) {
                    return getTimeTemplate(schedule, false);
                }
            },
            them: CALENDAR_THEME,
            useCreationPopup: true,
            useDetailPopup: true
        });

        cal.on({
            clickSchedule: function (e) {
                $('.tui-full-calendar-schedule-title').wrap(function () {
                    return `<a href="/events/show/${findEvent($(this).text()).id}"></a>`
                });
            },
            beforeCreateSchedule: function (e) {
                sendEventCreationRequest(e);
                $('#modal-new-schedule').modal('hide');
                refreshSchedules();
            },
            beforeUpdateSchedule: function (e) {
                const schedule = e.schedule;
                const event = findEventById(schedule.id);
                event.type = 'event';
                event.startDate = moment(e.start._date).add(3, 'hours').toISOString();
                event.endDate = moment(e.end._date).add(3, 'hours').toISOString();
                event.calendarId = schedule.calendarId;
                event.isAllDay = schedule.isAllDay;
                event.importanceLvl = 'SLIGHTLY_IMPORTANT';
                event.isReadOnly = schedule.isReadOnly;
                event.isBusy = (schedule.state === 'Busy');

                $.ajax({
                    url: ('/api/events/' + e.schedule.id),
                    type: 'PUT',
                    data: JSON.stringify(event),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader("Accept", "text/csv");
                        xhr.setRequestHeader("Content-Type", "application/json");
                        const csrfAttr = getCsrfTokenAndHeader();
                        xhr.setRequestHeader(csrfAttr.header, csrfAttr.token);
                    },
                    success: function (response) {
                        console.log('put ev resp', response);
                        const changes = e.changes;
                        cal.updateSchedule(schedule.id, schedule.calendarId, changes);
                        cal.render(true);
                    },
                    error: function(xhr) {
                        if (xhr.status === 405) {
                            showErrorModal('Access Denied');
                        } else {
                            showErrorModal(`Error ${xhr.status}`);
                        }
                    }
                });
            },
            beforeDeleteSchedule: function (e) {
                if (confirm('Delete this event?')) {
                    $.ajax({
                        url: ('/api/events/' + e.schedule.id),
                        type: 'DELETE',
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader("Accept", "text/csv");
                            xhr.setRequestHeader("Content-Type", "application/json");
                            const csrfAttr = getCsrfTokenAndHeader();
                            xhr.setRequestHeader(csrfAttr.header, csrfAttr.token);
                        },
                        success: function (response) {
                            console.log(response);
                            ScheduleSet.delete(e.schedule);
                            cal.deleteSchedule(e.schedule.id, e.schedule.calendarId);
                            setSchedules();
                            cal.render(true);
                        },
                        error: function (xhr) {
                            if (xhr.status === 405) {
                                showErrorModal('Access Denied');
                            } else {
                                showErrorModal(`Error ${xhr.status}`);
                            }
                        }
                    });
                }
            },
            clickTimezonesCollapseBtn: function (timezonesCollapsed) {
                if (timezonesCollapsed) {
                    cal.setTheme({
                        'week.daygridLeft.width': '77px',
                        'week.timegridLeft.width': '77px'
                    });
                } else {
                    cal.setTheme({
                        'week.daygridLeft.width': '60px',
                        'week.timegridLeft.width': '60px'
                    });
                }
                return true;
            }
        });

        function getDataAction(target) {
            return target.dataset ? target.dataset.action : target.getAttribute('data-action');
        }

        function onClickMenu(e) {
            var target = $(e.target).closest('a[role="menuitem"]')[0];
            var action = getDataAction(target);
            var options = cal.getOptions();
            var viewName = '';
            switch (action) {
                case 'toggle-daily':
                    viewName = 'day';
                    break;
                case 'toggle-weekly':
                    viewName = 'week';
                    break;
                case 'toggle-monthly':
                    options.month.visibleWeeksCount = 0;
                    viewName = 'month';
                    break;
                case 'toggle-weeks2':
                    options.month.visibleWeeksCount = 2;
                    viewName = 'month';
                    break;
                case 'toggle-weeks3':
                    options.month.visibleWeeksCount = 3;
                    viewName = 'month';
                    break;
                case 'toggle-narrow-weekend':
                    options.month.narrowWeekend = !options.month.narrowWeekend;
                    options.week.narrowWeekend = !options.week.narrowWeekend;
                    viewName = cal.getViewName();

                    target.querySelector('input').checked = options.month.narrowWeekend;
                    break;
                case 'toggle-start-day-1':
                    options.month.startDayOfWeek = options.month.startDayOfWeek ? 0 : 1;
                    options.week.startDayOfWeek = options.week.startDayOfWeek ? 0 : 1;
                    viewName = cal.getViewName();

                    target.querySelector('input').checked = options.month.startDayOfWeek;
                    break;
                case 'toggle-workweek':
                    options.month.workweek = !options.month.workweek;
                    options.week.workweek = !options.week.workweek;
                    viewName = cal.getViewName();

                    target.querySelector('input').checked = !options.month.workweek;
                    break;
                default:
                    break;
            }

            cal.setOptions(options, true);
            cal.changeView(viewName, true);

            setDropdownCalendarType();
            setRenderRangeText();
            setSchedules();
        }

        function onClickNavi(e) {
            var action = getDataAction(e.target);

            switch (action) {
                case 'move-prev':
                    cal.prev();
                    break;
                case 'move-next':
                    cal.next();
                    break;
                case 'move-today':
                    cal.today();
                    break;
                default:
                    return;
            }
            setRenderRangeText();
            setSchedules();
        }

        function onChangeNewScheduleCalendar(e) {
            var target = $(e.target).closest('a[role="menuitem"]')[0];
            var calendarId = getDataAction(target);
            var calendarNameElement = document.getElementById('calendarName');
            var calendar = findCalendarById(calendarId);
            var html = [];
            html.push(
                '<span class="calendar-bar" style="background-color: ' + calendar.bgColor + '; border-color:' + calendar.borderColor + ';"></span>'
            );
            html.push('<span class="calendar-name">' + calendar.name + '</span>');
            calendarNameElement.innerHTML = html.join('');

            selectedCalendar = calendar;
        }

        function onChangeCalendars(e) {
            var calendarId = e.target.value;
            var checked = e.target.checked;
            var viewAll = document.querySelector('.lnb-calendars-item input');
            var calendarElements = Array.prototype.slice.call(
                document.querySelectorAll('#calendarList input')
            );
            var allCheckedCalendars = true;

            if (calendarId === 'all') {
                calendarElements.forEach(function (input) {
                    var span = input.parentNode;
                    input.checked = checked;
                    span.style.backgroundColor = checked ? span.style.borderColor : 'transparent';
                });
                CalendarList.forEach(function (calendar) {
                    calendar.checked = checked;
                });
            } else {
                findCalendarById(calendarId).checked = checked;

                allCheckedCalendars = calendarElements.every(function (input) {
                    return input.checked;
                });
                viewAll.checked = allCheckedCalendars;
            }
            refreshSchedules();
        }

        function setDropdownCalendarType() {
            var calendarTypeName = document.getElementById('calendarTypeName');
            var calendarTypeIcon = document.getElementById('calendarTypeIcon');
            var options = cal.getOptions();
            var type = cal.getViewName();
            var iconClassName;

            if (type === 'day') {
                type = 'Daily';
                iconClassName = 'calendar-icon ic_view_day';
            } else if (type === 'week') {
                type = 'Weekly';
                iconClassName = 'calendar-icon ic_view_week';
            } else if (options.month.visibleWeeksCount === 2) {
                type = '2 weeks';
                iconClassName = 'calendar-icon ic_view_week';
            } else if (options.month.visibleWeeksCount === 3) {
                type = '3 weeks';
                iconClassName = 'calendar-icon ic_view_week';
            } else {
                type = 'Monthly';
                iconClassName = 'calendar-icon ic_view_month';
            }
            calendarTypeName.innerHTML = type;
            calendarTypeIcon.className = iconClassName;
        }

        function setRenderRangeText() {
            var renderRange = document.getElementById('renderRange');
            var options = cal.getOptions();
            var viewName = cal.getViewName();
            const html = [];
            if (viewName === 'day') {
                html.push(moment(cal.getDate().getTime()).format('YYYY.MM.DD'));
            } else if (
                viewName === 'month' &&
                (!options.month.visibleWeeksCount ||
                    options.month.visibleWeeksCount > 4)
            ) {
                html.push(moment(cal.getDate().getTime()).format('YYYY.MM'));
            } else {
                html.push(
                    moment(cal.getDateRangeStart().getTime()).format('YYYY.MM.DD')
                );
            }
            renderRange.innerHTML = html.join('');
        }

        function setCalendars() {
            cal.clear();
            $.ajax({
                type: 'GET',
                url: '/api/calendars',
                async: false,
                success: function (response) {
                    console.log('resp', response);
                    $.each(response, function (index, cal) {
                        CalendarList.push(createCalendar(cal.id, cal.name, cal.textColor, cal.bgColor));
                    });
                    renderCalendarsList();
                    refreshSchedules();
                },
                error: function(xhr) {
                    if (xhr.status === 405) {
                        showErrorModal('Access Denied');
                    } else {
                        showErrorModal(`Error ${xhr.status}`);
                    }
                }
            });
        }

        function setSchedules() {
            cal.clear();
            ScheduleSet.clear();
            $.ajax({
                type: 'GET',
                url: '/api/events',
                success: function (response) {
                    console.log('resp', response);
                    $.each(response, function (index, e) {
                        ScheduleSet.add(createSchedule(e));
                    });
                    cal.createSchedules(Array.from(ScheduleSet), true);
                    cal.render(true);
                },
                error: function(xhr) {
                    if (xhr.status === 405) {
                        showErrorModal('Access Denied');
                    } else {
                        showErrorModal(`Error ${xhr.status}`);
                    }
                }
            });
        }

        function setEventListeners() {
            $('#menu-navi').on('click', onClickNavi);
            $('.dropdown-menu a[role="menuitem"]').on('click', onClickMenu);
            $('#lnb-calendars').on('change', onChangeCalendars);

            $('#btn-create-calendar').on('click', function () {
                $('#createCalModal').modal('show')
            });
            $('#btn-save-cal').on('click', sendCalendarCreationRequest);

            $('.btn-show-cal-edit-modal').on('click', function (e) {
                $('#editCalModal').modal('show');
                calendarToUpdateId = e.target.id;
            });
            $('#btn-update-cal').on('click', sendCalendarUpdateRequest);
            $('#btn-delete-cal').on('click', function (e) {
                if (confirm('Delete this calendar?')) {
                    sendCalendarDeletionRequest(e);
                }
            });
            $('#dropdownMenu-calendars-list').on('click', onChangeNewScheduleCalendar);
            window.addEventListener('resize', resizeThrottled);
            $('#add-event-btn').on('click', function (e) {
                window.location.href = `${window.location.protocol}//${window.location.host}/events/create`;
            });
        }
        resizeThrottled = tui.util.throttle(function () {cal.render();}, 50);
        window.cal = cal;
        setDropdownCalendarType();
        setRenderRangeText();
        setCalendars();
        setEventListeners();
        setSchedules();
    })(window, tui.Calendar);
});
