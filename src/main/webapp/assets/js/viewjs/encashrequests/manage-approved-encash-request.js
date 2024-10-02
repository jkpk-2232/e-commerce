jQuery(document).ready(function() {

    setMenuActiveSub("302");

    jQuery("#btnTRECancel").click(function(e) {

        e.preventDefault();

        jQuery("#encashRequestIdHidden").val("");

        jQuery("#transferRejectEncashModel").modal("hide");

        location.reload();
    });

    jQuery("#btnTRETransfer").click(function(e) {

        e.preventDefault();

        var requestStatus = "TRANSFERRED";

        sendRequestToApproveReject(requestStatus)
    });

    jQuery("#btnTREReject").click(function(e) {

        e.preventDefault();

        var requestStatus = "REJECTED";

        sendRequestToApproveReject(requestStatus)
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
        "sAjaxSource": basePath + "/encash-requests/approved/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
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
            "sWidth": "12%",
            "bSortable": false
        }, {
            "sWidth": "12%",
            "bSortable": false
        }, {
            "sWidth": "12%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "13%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}

function transferRequest(encashRequestId) {
    jQuery("#encashRequestIdHidden").val(encashRequestId);
    getEncashRequestDetails(encashRequestId, "TRANSFERRED");
}

function rejectRequest(encashRequestId) {
    jQuery("#encashRequestIdHidden").val(encashRequestId);
    getEncashRequestDetails(encashRequestId, "REJECTED");
}

function getEncashRequestDetails(encashRequestId, requestStatus) {

	jQuery("#encashRequestRemark").val("");
	
    jQuery.ajax({
        url: basePath + "/encash-requests/approved/details.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "encashRequestId": encashRequestId
        },
        success: function(responseData) {

            if (responseData.type === TYPE_SUCCESS) {

                jQuery("#firstName").val(responseData.firstName);
                jQuery("#email").val(responseData.email);
                jQuery("#phoneNumber").val(responseData.phoneNumber);
                jQuery("#requestedAmount").val(responseData.requestedAmount);
                jQuery("#remainingBalence").val(responseData.remainingBalence);

                if ("REJECTED" === requestStatus) {
                    jQuery("#btnTREReject").show();
                    jQuery("#btnTRETransfer").hide();
                } else {
                    jQuery("#btnTREReject").hide();
                    jQuery("#btnTRETransfer").show();
                }

                jQuery("#transferRejectEncashModel").modal("show");

            } else {

                displayBootstrapMessage(responseData.message);
            }
        }
    });
}

function sendRequestToApproveReject(requestStatus) {

    encashRequestApproveAjax = jQuery.ajax({
        url: basePath + "/encash-requests/approved/transfer.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "encashRequestIds": jQuery("#encashRequestIdHidden").val(),
            "requestStatus": requestStatus,
            "approveRejectRemark": jQuery("#encashRequestRemark").val()
        },
        success: function(responseData) {

            if (responseData.type == TYPE_SUCCESS) {

                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + jQuery("#SUCCESS_PAGE_REDIRECT_URL").val())

            } else {

                displayBootstrapMessage(responseData.message);
            }
        }
    });
}