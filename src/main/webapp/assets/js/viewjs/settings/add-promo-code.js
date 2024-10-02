jQuery(document).ready(function() {

	//setMenuActiveSub("75");
	setMenuActiveSub("62");
	
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["add-promo-code"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });

    if (jQuery("#mode").val() === "1") {
        jQuery("#maxDiscountDiv").show();
    } else {
        jQuery("#maxDiscountDiv").hide();
    }

    jQuery("#mode").change(function() {

        if (jQuery("#mode").val() === "1") {
            jQuery("#maxDiscountDiv").show();
        } else {
            jQuery("#maxDiscountDiv").hide();
        }
    });

    jQuery("#serviceTypeId").change(function() {
        getServiceTypeWiseVendors(true, "1");
    });

    var startDateMoment;
    var endDateMoment;

    if (jQuery("#startDate").val() != '' && jQuery("#endDate").val() != '') {
        startDateMoment = moment.unix(jQuery("#startDate").val());
        endDateMoment = moment.unix(jQuery("#endDate").val());
    } else {
        startDateMoment = moment();
        endDateMoment = moment();
    }

    jQuery("#validity span").html(startDateMoment.format(DATATABLE_FORMAT_DATE) + ' - ' + endDateMoment.format(DATATABLE_FORMAT_DATE));

    jQuery("#validity").daterangepicker({
        "showDropdowns": true,
        "autoApply": true,
        "startDate": startDateMoment,
        "endDate": endDateMoment
    }, function(start, end, label) {
        jQuery("#validity span").html(start.format(DATATABLE_FORMAT_DATE) + ' - ' + end.format(DATATABLE_FORMAT_DATE));
        jQuery("#startDate").val(start.unix());
        jQuery("#endDate").val(end.unix());
    });
});