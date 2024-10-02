jQuery(document).ready(function() {

   // setMenuActive("10");
	setMenuActiveSub("68");
	
    setMultiSelectPicker("regionList");
    setMultiSelectPicker("passengerList");
    setMultiSelectPicker("driverList");
    
    if (jQuery("#isDispalyPassList").val() == "YES") {

        jQuery("#regionListDiv").hide();
        jQuery("#driverListDiv").hide();
        jQuery("#passengerListDiv").show();

    } else {

        jQuery("#passengerListDiv").hide();
        jQuery("#regionListDiv").show();
        jQuery("#driverListDiv").show();
    }

    jQuery("#userRole").change(function() {

        if (jQuery("#userRole").val() == jQuery("#DRIVER_ROLE_ID").val()) {

            jQuery("#passengerListDiv").hide();
            jQuery("#regionListDiv").show();
            jQuery("#driverListDiv").show();

        } else {

            jQuery("#regionListDiv").hide();
            jQuery("#driverListDiv").hide();
            jQuery("#passengerListDiv").show();
        }
    });

    jQuery("#btnPost").click(function(e) {

        e.preventDefault();
        
        document.forms["add-announcements"].submit();
    });
    
    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
    
	var $elem = jQuery("#regionList");
	$elem.picker();
	$elem.on("sp-change", function() {
		var regionIds = jQuery("#regionList").picker("get");
        console.log("regionId: " + regionIds);
        getDriverList(regionIds);
	});
    
    initializeDateRangePicker("customDateRange");

    jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
        reinitializeStartEndDate(picker.startDate, picker.endDate);
        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
    });

    loadDatatable(startDateFormatted, endDateFormatted);
});

function loadDatatable(tempStartDate, tempEndDate) {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/announcements/list.json",
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
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "70%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }]
    });
    
    commonCssOverloadForDatatable("datatableDefault");
}

function getDriverList(regionIds) {

    var driverListAjax = jQuery.ajax({
        url: basePath + "/announcements/driver-list.json?regionIds=" + regionIds,
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        success: function(responseData) {
        	
        	console.log("responseData.driverListOptions: " + responseData.driverListOptions);
        	console.log("responseData.type: " + responseData.type);
        	
        	if (responseData.type === "ERROR") {
//        		jQuery("#driverList").picker("destroy");
//        		setMultiSelectPicker("driverList");
        		jQuery("#driverListDiv").hide();
        	} else {
        		jQuery("#driverList").picker("destroy");
        		jQuery("#driverListDiv").show();
	            jQuery("#driverList").children("option").remove();
	            jQuery("#driverList").append(responseData.driverListOptions);
//	            jQuery("#driverList").selectpicker('refresh');
	            setMultiSelectPicker("driverList");
        	}
        }
    });
}