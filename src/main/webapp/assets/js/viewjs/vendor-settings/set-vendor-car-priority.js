jQuery(document).ready(function() {

	setMenuActiveSub("50");
	setMenuActiveSub("btnSetVendorCarPriority");

    jQuery("#btnSave").click(function() {

        var kvpairs = [];
        var form = document.forms.setVendorCarPriority;
        for (var i = 0; i < form.elements.length; i++) {
            var e = form.elements[i];
            if ((e.name != null && e.name != "") && (e.value != null && e.value != "")) {
                if (e.name == "serviceTypeId" || e.name == "vendorId") {
                } else {
                    kvpairs.push(encodeURIComponent(e.name) + "=" + encodeURIComponent(e.value));
                }
            }
        }
        
        var queryString = kvpairs.join("&&&");

        jQuery("#queryString").val(queryString);
    	
        document.forms["setVendorCarPriority"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();

        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});