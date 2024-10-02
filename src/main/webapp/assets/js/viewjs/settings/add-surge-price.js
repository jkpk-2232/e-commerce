jQuery(document).ready(function() {
	
	setMenuActiveSub("11");
	
	initTimePicker("startTimeHourMins");
    initTimePicker("endTimeHourMins");
	
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();

        document.forms["add-surge-price"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();

        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});