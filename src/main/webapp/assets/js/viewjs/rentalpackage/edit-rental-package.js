jQuery(document).ready(function() {

    setMenuActiveSub("15");
    setMenuActiveSub("btnRentalPackageSettings");
    
    if (jQuery("#carFareDetailsErrorHidden").val() != "") {
        jQuery("#carFareDetailsError").text(jQuery("#carFareDetailsErrorHidden").val());
    }

    selectCarAvailability(jQuery("#fareAvailableCarList").val());

    jQuery("#btnSave").click(function() {

        var carAvailableList = [];
        jQuery("input:checked").each(function() {
            console.log("$(this).val()\t" + $(this).attr("name"));
        	carAvailableList.push($(this).attr("name"));
        });
        carAvailableList.join("&&");
        console.log(carAvailableList);
        
        var kvpairs = [];
        var form = document.forms.editRentalPackage;
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

        console.log(queryString);
        jQuery("#queryString").val(queryString);
        jQuery("#carAvailableList").val(carAvailableList);
        jQuery("#fareAvailableCarList").val(carAvailableList);

        document.forms["editRentalPackage"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();

        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});