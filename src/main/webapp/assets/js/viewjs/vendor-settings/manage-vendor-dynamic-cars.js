jQuery(document).ready(function() {

	setMenuActiveSub("50");
	
	setMultiSelectPicker("carTypeList");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
    	document.forms["manage-vendor-dynamic-cars"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});