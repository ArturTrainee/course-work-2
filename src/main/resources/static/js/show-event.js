$(document).ready(function () {

    function getCsrfTokenAndHeader() {
        return {token: $('#csrfToken').val(), header: $('#csrfHeader').val()};
    }

    $('#delete-event').on('click', function (e) {
        console.log('beforeDelete event', e);
        if (confirm('Delete this event?')) {
            const id = $(e.target).val();
            $.ajax({
                url: ('/api/events/' + id),
                type: 'DELETE',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Accept", "application/json");
                    xhr.setRequestHeader("Content-Type", "application/json");
                    const csrfAttr = getCsrfTokenAndHeader();
                    xhr.setRequestHeader(csrfAttr.header, csrfAttr.token);
                },
                success: function (response) {
                    window.location = `${window.location.protocol}//${window.location.host}/events`;
                },
                error: function (er) {
                    console.log(er);
                }
            });
        }
    });
});