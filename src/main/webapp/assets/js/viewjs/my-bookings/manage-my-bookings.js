var taxiDatatable;

jQuery(document).ready(function() {

	jQuery(".dashboardWrapperLeftPanelMenuList li a").removeClass("dashboardWrapperLeftPanelMenuListCurrentActiveMenu");
    jQuery("#12 a").addClass("dashboardWrapperLeftPanelMenuListCurrentActiveMenu");

    jQuery(document).on('click', '.history', function(e) {

        e.preventDefault();

        var tourId = this.id;

        document.location.href = basePath + "/admin-bookings-details.do?tourId=" + tourId + "&tourType=my-bookings";
    });
    
    jQuery("#btnExport").click(function() {

        businessOwnerId = jQuery("#userId").val();
        document.location.href = basePath + "/export/business-owner.do?id=" + businessOwnerId + "&type=businessOwner&startDate=" + startDate + "&endDate=" + endDate;
    });
    
    //-------------------------------------------------

    var cb = function(start, end, label) {

        jQuery('#reportrange span').html(start.format('MM/DD/YYYY') + ' - ' + end.format('MM/DD/YYYY'));

        startDate = start.format('MM/DD/YYYY');

        endDate = end.format('MM/DD/YYYY');

        if (taxiDatatable != undefined) {
        	taxiDatatable.fnDestroy();
        }

        loadDatatable(startDate, endDate);
    };

    var optionSet = {
    	startDate: moment("01/01/2016").format('MM/DD/YYYY'),
        endDate: moment(),
        minDate: '01/01/2016',
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
        	'All': [moment("01012016", "MMDDYYYY").fromNow(), moment()],
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
        format: 'MM/DD/YYYY',
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

    jQuery('#reportrange span').html(moment("01/01/2016").format('MM/DD/YYYY') + ' - ' + moment().format('MM/DD/YYYY'));
    startDate = moment("01/01/2016").format('MM/DD/YYYY');
    endDate = moment().format('MM/DD/YYYY');

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

	taxiDatatable = jQuery('#adminBookingsListTable').dataTable({
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/my-bookings/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {

            aoData.push({
                "name": "startDate",
                "value": startDate
            }, {
                "name": "endDate",
                "value": endDate
            });
            $.getJSON(sSource, aoData, function(json) {

                fnCallback(json);
            });
        },
        "aoColumns": [{
            "bSearchable": false,
            "bVisible": false,
            "asSorting": ["desc"]
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
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
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }]
    });

    jQuery("table").css("width", "100%");
    jQuery("#adminBookingsListTable_first").hide();
    jQuery("#adminBookingsListTable_previous").html("<i class='icon-chevron-left'></i>");
    jQuery("#adminBookingsListTable_next").html("<i class='icon-chevron-right'></i>");
    jQuery("#adminBookingsListTable_last").hide();
}