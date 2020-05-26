$(document).ready(function () {

    const msg = $('#confirm-msg'),
        passField = $('#password'),
        confirmPassField = $('#confirm-password');

    $('#password, #confirm-password').on('keyup', function () {
        if (passField.val() === confirmPassField.val()) {
            msg.text('Matching').css('color', 'green');
        } else
            msg.text('Not Matching').css('color', 'red');
    });

    $('#sign-in').on('click', function (e) {
        if (passField.val() !== confirmPassField.val()) {
            e.preventDefault();
            alert('Passwords not matching!');
        }
    })
});