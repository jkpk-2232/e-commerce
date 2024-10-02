jQuery(document).ready(function() {

    setMenuActiveSub("301");

    jQuery("#btnARECancel").click(function(e) {

        e.preventDefault();

        jQuery("#encashRequestIdHidden").val("");

        jQuery("#approveRejectEncashModel").modal("hide");

        location.reload();
    });

    jQuery("#btnAREApprove").click(function(e) {

        e.preventDefault();

        sendRequestToApproveReject("APPROVED")
    });

    jQuery("#btnAREReject").click(function(e) {

        e.preventDefault();

        sendRequestToApproveReject("REJECTED")
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
        "sAjaxSource": basePath + "/encash-requests/hold/list.json",
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

function approveRequest(encashRequestId) {

    jQuery("#encashRequestIdHidden").val(encashRequestId);

    jQuery("#approveRejectRemark").val("");

    getEncashRequestDetails(encashRequestId, "APPROVED");
}

function rejectRequest(encashRequestId) {

    jQuery("#encashRequestIdHidden").val(encashRequestId);

    jQuery("#approveRejectRemark").val("");

    getEncashRequestDetails(encashRequestId, "REJECTED");
}

function getEncashRequestDetails(encashRequestId, requestStatus) {

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

            if (responseData.type === "SUCCESS") {

                jQuery("#firstName").val(responseData.firstName);
                jQuery("#email").val(responseData.email);
                jQuery("#phoneNumber").val(responseData.phoneNumber);
                jQuery("#requestedAmount").val(responseData.requestedAmount);
                jQuery("#remainingBalence").val(responseData.remainingBalence);

                if ("REJECTED" === requestStatus) {
                    jQuery("#btnAREReject").show();
                    jQuery("#btnAREApprove").hide();
                } else {
                    jQuery("#btnAREReject").hide();
                    jQuery("#btnAREApprove").show();
                }

                jQuery("#approveRejectEncashModel").modal("show");

            } else {

                displayBootstrapMessage(responseData.message);
            }
        }
    });
}

function sendRequestToApproveReject(requestStatus) {

    var encashRequestApproveAjax = jQuery.ajax({
        url: basePath + "/encash-requests/hold/approve.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "encashRequestIds": jQuery("#encashRequestIdHidden").val(),
            "requestStatus": requestStatus,
            "approveRejectRemark": jQuery("#approveRejectRemark").val()
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