var taxiDatatable;
var startDateStringHidden = "";
var dateFormatHidden = "";
var startDateWithoutSeparatorHidden = "";
var dateFormatWithoutSeparatorHidden = "";
var startDate;
var endDate;

jQuery(document).ready(function() {

    jQuery(".dashboardWrapperLeftPanelMenuList li a").removeClass("dashboardWrapperLeftPanelMenuListCurrentActiveMenu");
    jQuery("#ordersOthersId").addClass("dashboardWrapperLeftPanelMenuListCurrentActiveMenu");
    jQuery("#ordersOthersId").parent().css({
        'display': 'block'
    });

	startDateStringHidden = jQuery("#startDateStringHidden").val();
    dateFormatHidden = jQuery("#dateFormatHidden").val();
    startDateWithoutSeparatorHidden = jQuery("#startDateWithoutSeparatorHidden").val();
    dateFormatWithoutSeparatorHidden = jQuery("#dateFormatWithoutSeparatorHidden").val();

    setInterval(function() { 
    	
    	if (taxiDatatable != undefined) {
        	taxiDatatable.fnDestroy();
        }

        loadDatatable(startDate, endDate);
        
    }, 30000);

	jQuery("#vendorId").change(function() {

        if (taxiDatatable != undefined) {
        	taxiDatatable.fnDestroy();
        }

        loadDatatable(startDate, endDate);
    });
    
    jQuery("#serviceId").change(function() {

		getCategoryServiceWise(true);

        if (taxiDatatable != undefined) {
        	taxiDatatable.fnDestroy();
        }

        loadDatatable(startDate, endDate);
    });
    
    jQuery("#orderStatusFilter").change(function() {

        if (taxiDatatable != undefined) {
        	taxiDatatable.fnDestroy();
        }

        loadDatatable(startDate, endDate);
    });
    
    jQuery("#vendorOrderManagement").change(function() {

        if (taxiDatatable != undefined) {
        	taxiDatatable.fnDestroy();
        }

        loadDatatable(startDate, endDate);
    });

    //-------------------------------------------------

    var cb = function(start, end, label) {

        jQuery('#reportrange span').html(start.format(dateFormatHidden) + ' - ' + end.format(dateFormatHidden));

        startDate = start.format(dateFormatHidden);

        endDate = end.format(dateFormatHidden);

        if (taxiDatatable != undefined) {
        	taxiDatatable.fnDestroy();
        }

        loadDatatable(startDate, endDate);
    };

    var optionSet = {
    	startDate: moment(startDateStringHidden, dateFormatHidden, true).format(dateFormatHidden),
        endDate: moment(),
        minDate: startDateStringHidden,
        maxDate: {
            days: 3650
        },
        dateLimit: {
            days: 3650
        },
        showDropdowns: true,
        showWeekNumbers: true,
        timePicker: false,
        timePickerIncrement: 1,
        timePicker12Hour: true,
        ranges: {
        	'All': [moment(startDateWithoutSeparatorHidden, dateFormatWithoutSeparatorHidden).fromNow(), moment()],
            'Today': [moment(), moment()],
            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
            'This Month': [moment().startOf('month'), moment().endOf('month')],
            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        opens: 'right',
        buttonClasses: ['btn btn-default'],
        applyClass: 'btn-sm btn-primary',
        cancelClass: 'btn-sm',
        format: dateFormatHidden,
        separator: ' to ',
        locale: {
            applyLabel: 'Submit',
            cancelLabel: 'Clear',
            fromLabel: 'From',
            toLabel: 'To',
            customRangeLabel: 'Custom',
            daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
            monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
            firstDay: 1
        }
    };

    jQuery('#reportrange span').html(moment(startDateStringHidden, dateFormatHidden, true).format(dateFormatHidden) + ' - ' + moment().format(dateFormatHidden));
    
    startDate = moment(startDateStringHidden, dateFormatHidden, true).format(dateFormatHidden);
    endDate = moment().format(dateFormatHidden);

    jQuery('#reportrange').daterangepicker(optionSet, cb);

    jQuery('#reportrange').on('cancel.daterangepicker', function(ev, picker) {
        console.log("cancel event fired");
    });

    jQuery('#reportrange').data('daterangepicker').setOptions(optionSet, cb);

    jQuery(".btn").css("color", "black");
    
    loadDatatable(startDate, endDate);

    //-------------------------------------------------------
});

function loadDatatable(startDate, endDate) {

    taxiDatatable = jQuery('#passangerListTable').dataTable({
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "iDisplayLength": 10,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-orders-new/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": startDate
            }, {
                "name": "endDate",
                "value": endDate
            }, {
                "name": "vendorId",
                "value": jQuery("#vendorId").val()
            }, {
                "name": "serviceId",
                "value": jQuery("#serviceId").val()
            }
//            , {
//                "name": "categoryId",
//                "value": jQuery("#categoryId").val()
//            }
            , {
                "name": "type",
                "value": jQuery("#type").val()
            }, {
                "name": "orderStatusFilter",
                "value": jQuery("#orderStatusFilter").val()
            }, {
                "name": "vendorOrderManagement",
                "value": jQuery("#vendorOrderManagement").val()
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

    jQuery("table").css("width", "100%");
    jQuery("#passangerListTable_first").hide();
    jQuery("#passangerListTable_previous").html("<i class='icon-chevron-left'></i>");
    jQuery("#passangerListTable_next").html("<i class='icon-chevron-right'></i>");
    jQuery("#passangerListTable_last").hide();
}