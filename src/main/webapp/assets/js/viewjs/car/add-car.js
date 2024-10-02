jQuery(document).ready(function() {
	
	//setMenuActiveDoubleTab("7", "24");
	//setMenuActiveSub("70");
	setMenuActiveSub("47");
	
	
	
	
	initializeFileUpload("frontImgUrl", "hiddenFrontImgUrl_dummy", "hiddenFrontImgUrl");
	initializeFileUpload("backImgUrl", "hiddenBackImgUrl_dummy", "hiddenBackImgUrl");
	initializeFileUpload("insuranceImgUrl", "hiddenInsuranceImgUrl_dummy", "hiddenInsuranceImgUrl");
	initializeFileUpload("inspectionImgUrl", "hiddenInspectionImgUrl_dummy", "hiddenInspectionImgUrl");
	initializeFileUpload("vehicleCommercialLicenseImgUrl", "hiddenVehicleCommercialLicenseImgUrl_dummy", "hiddenVehicleCommercialLicenseImgUrl");
	initializeFileUpload("vehicleRegistrationImgUrl", "hiddenVehicleRegistrationImgUrl_dummy", "hiddenVehicleRegistrationImgUrl");
    
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["add-car"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});