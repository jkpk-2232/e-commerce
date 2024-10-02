jQuery(document).ready(function() {

    jQuery("#btnLogin").click(function(event) {

        event.preventDefault();

        document.loginForm.submit();
    });

    jQuery("#email").on("keypress", function(e) {
        if (e.which == 32)
            return false;
    });

    jQuery("#password").on("keypress", function(e) {
        if (e.which == 32)
            return false;
    });

    jQuery(document).keypress(function(event) {

        var keycode = (event.keyCode ? event.keyCode : event.which);

        if (keycode == '13') {
            document.loginForm.submit();
        }
    });

    document.body.onkeypress = enterKey;

    function enterKey(evt) {

        evt = (evt) ? evt : event;

        var charCode = (evt.which) ? evt.which : evt.keyCode;

        if (charCode == 13) {

        }
    }
});