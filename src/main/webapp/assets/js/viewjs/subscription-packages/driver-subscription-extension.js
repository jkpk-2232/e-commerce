jQuery(document).ready(function() {

//setMenuActive("6");
setMenuActiveSub("65");

    setMultiSelectPicker("driverList");

    jQuery("#multicityCityRegionIdDiv").show();
    
    if (jQuery("#driverAll").val() === "true") {
		jQuery("#driverListDiv").hide();
	} else {
		getDriverList(jQuery("#multicityCityRegionId").val());
	}

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["driver-subscription-extension"].submit();
    });
    
    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
    
    jQuery("#driverAll").change(function() {
		
		if (jQuery("#driverAll").val() === "true") {
			jQuery("#driverListDiv").hide();
		} else {
			getDriverList(jQuery("#multicityCityRegionId").val());
		}
    });
    
    jQuery("#multicityCityRegionId").change(function() {
		if (jQuery("#driverAll").val() === "true") {
			
		} else {
			getDriverList(jQuery("#multicityCityRegionId").val());
    	}
    });
    
    initializeDateRangePicker("customDateRange");

    jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
        reinitializeStartEndDate(picker.startDate, picker.endDate);
        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
    });

    loadDatatable(startDateFormatted, endDateFormatted);
});

function getDriverList(regionIds) {

    jQuery.ajax({
        url: basePath + "/announcements/driver-list.json?regionIds=" + regionIds,
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        success: function(responseData) {
        	
        	console.log("responseData.driverListOptions: " + responseData.driverListOptions);
        	console.log("responseData.type: " + responseData.type);
        	
        	if (responseData.type === "ERROR") {
        		jQuery("#driverListDiv").hide();
        		displayBootstrapMessage("No drivers found for this selected reqion.");
        	} else {
        		jQuery("#driverList").picker("destroy");
        		jQuery("#driverListDiv").show();
	            jQuery("#driverList").children("option").remove();
	            jQuery("#driverList").append(responseData.driverListOptions);
	            setMultiSelectPicker("driverList");
        	}
        }
    });
}

function loadDatatable(tempStartDate, tempEndDate) {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/driver-subscription-extension/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
        	aoData.push({
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
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
            "sWidth": "10%",
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
        }, {
            "sWidth": "10%",
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