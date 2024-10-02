jQuery(document).ready(function() {

	//setMenuActiveDoubleTab("21", "42");
	
	setMenuActiveSub("50");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();

        document.forms["add-qr-code"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
    
});




