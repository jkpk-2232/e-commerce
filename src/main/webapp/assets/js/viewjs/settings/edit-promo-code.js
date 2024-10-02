jQuery(document).ready(function() {

	//setMenuActive("40");
	setMenuActiveSub("62");
	
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["edit-promo-code"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
    
    jQuery("#promoCodeEndDate").datepicker({
        startDate: jQuery("#promoCodeStartDate").val(),
        format: 'dd/mm/yyyy'
    });
});