jQuery(document).ready(function() {

	setMenuActiveDoubleTab("6", "23");

    setMultiSelectPicker("regionList");
    setMultiSelectPicker("carTypeList");
    setMultiSelectPicker("transmissionTypeList");

    jQuery("#dob").datepicker({
        endDate: new Date(),
        format: 'dd/mm/yyyy'
    });

    jQuery("#licenseExpiration").datepicker({
        startDate: new Date(),
        format: 'dd/mm/yyyy'
    });

    initializeFileUpload("profileImgUrl", "hiddenPhotoUrl_dummy", "hiddenPhotoUrl", false);
    initializeFileUpload("socialSecurityImgUrl", "hiddenSocialSecurityPhotoUrl_dummy", "hiddenSocialSecurityPhotoUrl");
    initializeFileUpload("drivingLicensePhoto", "hiddenDriverLicenseImage_dummy", "hiddenDriverLicenseImage");
    initializeFileUpload("driverLicenseBackPhotoUrl", "hiddenDriverLicenseBackImgUrl_dummy", "hiddenDriverLicenseBackImgUrl");
    initializeFileUpload("driverAccreditationPhotoUrl", "hiddenDriverAccreditationImgUrl_dummy", "hiddenDriverAccreditationImgUrl");
    initializeFileUpload("driverCriminalReportPhotoUrl", "hiddenDriverCriminalReportImgUrl_dummy", "hiddenDriverCriminalReportImgUrl");

    var insuranceEffectiveDateMoment;
    var insuranceExpirationDateMoment;
    
    if (jQuery("#insuranceEffectiveDate").val() != '' && jQuery("#insuranceExpirationDate").val() != '') {
    	insuranceEffectiveDateMoment = moment.unix(jQuery("#insuranceEffectiveDate").val());
    	insuranceExpirationDateMoment = moment.unix(jQuery("#insuranceExpirationDate").val());
    } else {
    	insuranceEffectiveDateMoment = moment();
    	insuranceExpirationDateMoment = moment();
    }

	jQuery("#reportrange span").html(insuranceEffectiveDateMoment.format(DATATABLE_FORMAT_DATE) + ' - ' + insuranceExpirationDateMoment.format(DATATABLE_FORMAT_DATE));

    jQuery("#reportrange").daterangepicker({
        "showDropdowns": true,
        "autoApply": true,
        "startDate": insuranceEffectiveDateMoment,
        "endDate": insuranceExpirationDateMoment
    }, function(start, end, label) {
        jQuery("#reportrange span").html(start.format(DATATABLE_FORMAT_DATE) + ' - ' + end.format(DATATABLE_FORMAT_DATE));
        jQuery("#insuranceEffectiveDate").val(start.unix());
        jQuery("#insuranceExpirationDate").val(end.unix());
    });

    var $elem = jQuery("#carTypeList");
    $elem.picker();
    $elem.on("sp-change", function() {
        var carTypeList = jQuery("#carTypeList").picker("get");
        updateCarList(carTypeList);
    });

    updateCarList(jQuery("#carTypeList").picker("get"));

    jQuery("#serviceType").change(function() {

        if (jQuery("#serviceType").val() === "1") {

            jQuery("#carTypesAndCarsDiv").show();
            jQuery("#driverTypeDiv").hide();

        } else {

            jQuery("#carTypesAndCarsDiv").hide();
            jQuery("#driverTypeDiv").show();
        }
    });

    if (jQuery("#serviceType").val() === "1") {
        jQuery("#carTypesAndCarsDiv").show();
        jQuery("#driverTypeDiv").hide();
    } else {
        jQuery("#carTypesAndCarsDiv").hide();
        jQuery("#driverTypeDiv").show();
    }

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["edit-driver"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});

function updateCarList(carTypeList) {

    var carTypeAjax = jQuery.ajax({
        url: basePath + "/add-driver/car-list.json?carTypeIds=" + carTypeList + "&carId=" + jQuery("#car").val(),
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        success: function(responseData) {
        	jQuery("#car").empty().append(responseData.carOptions);
            jQuery("#car").trigger("chosen:updated");
        }
    });
}