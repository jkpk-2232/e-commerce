jQuery(document).ready(function() {

    setMenuActiveSub("13");
    setMenuActiveSub("btnFeedSettings");
    
    if (jQuery("#feedFareDetailsErrorHidden").val() != "") {
        jQuery("#feedFareDetailsError").text(jQuery("#feedFareDetailsErrorHidden").val());
    }

    selectCarAvailability(jQuery("#servicesActiveList").val());

		
	
	jQuery("#btnSave").on("click", function(e) {
       var serviceAvailableList = [];
        jQuery("input:checked").each(function() {
        	console.log("$(this).val()\t" + $(this).attr("name"));
        	serviceAvailableList.push($(this).attr("name"));
        });
        serviceAvailableList.join("&&");
        console.log(serviceAvailableList);

        var kvpairs = [];
        var form = document.forms.addFeedSettings;
        for (var i = 0; i < form.elements.length; i++) {
            var e = form.elements[i];
            if ((e.name != null && e.name != "") && (e.value != null && e.value != "")) {
                if (e.name.includes("_IsAvailable") && e.value.includes("_IsAvailable")) {

                } else {
                    kvpairs.push(encodeURIComponent(e.name) + "=" + encodeURIComponent(e.value));
                }
            }
        }
        var queryString = kvpairs.join("&&&");

        jQuery("#queryString").val(queryString);
        jQuery("#servicesActiveList").val(serviceAvailableList);

        document.forms["addFeedSettings"].submit();
    });
	
	jQuery("#btnCancel").on("click", function(e) {
        e.preventDefault();

		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);       
    });
    


});