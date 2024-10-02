jQuery(document).ready(function() {

    setMenuActiveSub("403");

    jQuery("#btnEncash").click(function(e) {

        e.preventDefault();

        jQuery("#requestedAmount").val(0.0);

        getVendorAccountDetails();
    });

    jQuery("#btnEncashModelCancel").click(function(e) {

        e.preventDefault();

        jQuery("#encashRequestModel").modal("hide");

        location.reload();
    });

    jQuery("#btnEncashModelEncash").click(function(e) {

        e.preventDefault();

        sendEncashRequest();
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
        "sAjaxSource": basePath + "/vendor/my-account/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
            }, {
                "name": "userId",
                "value": jQuery("#userId").val()
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
            "sWidth": "17%",
            "bSortable": false
        }, {
            "sWidth": "30%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "13%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}

function getVendorAccountDetails() {

    jQuery.ajax({
        url: basePath + "/manage-drivers/account/details.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "userId": jQuery("#userId").val()
        },
        success: function(responseData) {

            if (responseData.type === TYPE_SUCCESS) {

                jQuery("#dCurrentBalence2").val(responseData.currentBalance);
                jQuery("#encashRequestModel").modal("show");

            } else {

                displayBootstrapMessage(responseData.message);
            }
        }
    });
}

function sendEncashRequest() {

    var requestedAmount = jQuery("#requestedAmount").val();
    var encashRemark = jQuery("#encashRemark").val();

    jQuery("#requestedAmountError").text("");
    jQuery("#encashRemarkError").text("");

    jQuery.ajax({
        url: basePath + "/vendor/my-account/encash.json",
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            requestedAmount: requestedAmount,
            encashRemark: encashRemark
        },
        success: function(responseData) {

            if (responseData.type == TYPE_SUCCESS) {

                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + jQuery("#SUCCESS_PAGE_REDIRECT_URL").val());

            } else if (responseData.type == TYPE_ERROR) {

                displayBootstrapMessage(responseData.message);

            } else {

                validateFields(responseData);
            }
        }
    });
}

function validateFields(data) {

    var requestedAmountError = "";
    var encashRemarkError = "";

    if ('requestedAmountError' in data) {
        requestedAmountError = data.requestedAmountError;
    }

    if ('encashRemarkError' in data) {
        encashRemarkError = data.encashRemarkError;
    }

    var arm1Error = errorMessageFunction("requestedAmount", requestedAmountError);
    var arm2Error = errorMessageFunction("encashRemark", encashRemarkError);

    if (arm1Error || arm2Error) {
        return false;
    }
}