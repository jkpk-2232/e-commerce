jQuery(document).ready(function() {
	
	setMenuActiveSub("47");
    
    initializeFileUpload("frontImgUrl", "hiddenFrontImgUrl_dummy", "hiddenFrontImgUrl");
	initializeFileUpload("backImgUrl", "hiddenBackImgUrl_dummy", "hiddenBackImgUrl");
	initializeFileUpload("insuranceImgUrl", "hiddenInsuranceImgUrl_dummy", "hiddenInsuranceImgUrl");
	initializeFileUpload("inspectionImgUrl", "hiddenInspectionImgUrl_dummy", "hiddenInspectionImgUrl");
	initializeFileUpload("vehicleCommercialLicenseImgUrl", "hiddenVehicleCommercialLicenseImgUrl_dummy", "hiddenVehicleCommercialLicenseImgUrl");
	initializeFileUpload("vehicleRegistrationImgUrl", "hiddenVehicleRegistrationImgUrl_dummy", "hiddenVehicleRegistrationImgUrl");
    
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["edit-car"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
    
    loadDatatable();
});

function loadDatatable() {

	var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/edit-car/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "carId",
                "value": jQuery("#carId").val()
            });
            $.getJSON(sSource, aoData, function(json) {
                fnCallback(json)
            });
        },
        "aoColumns": [{
            "bSearchable": false,
            "bVisible": false,
            "asSorting": ["desc"]
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }]
    });
	
	commonCssOverloadForDatatable("datatableDefault");
}