var adminFaqId = "";
var langPath = "";

jQuery(document).ready(function() {

    jQuery("#btnAdminFaq").addClass("settingsButtonActiveClass");
    jQuery("#btnAdminFaq div").addClass("activeSettingsButtonsText")
    jQuery("#btnAdminFaq").parent().css({
        'display': 'block'
    });
    
     if (jQuery("#language").val() == 1) {
        langPath = basePath + '/assets/datatable/language/French.lang';
    } else if (jQuery("#language").val() == 2)  {
        langPath = basePath + '/assets/datatable/language/English.lang';
    }else{
    	langPath = basePath + '/assets/datatable/language/English.lang';
    }

    jQuery("#btnSave").click(function() {

        document.forms["adminFaq"].submit();
    });

    jQuery(".updatePassengerCreditsDialog").hide();

    jQuery(document).on('click', '.deleteAdminFaq', function(e) {

        e.preventDefault();

        var adminFaqId = this.id;

        var warningDialogHtml = jQuery('<div></div>').html('<br><br><center>Are you sure you want to delete this faq?</center><br><br>');

        warningDialogHtml.dialog({
            modal: true,
            title: 'Warning!',
            resizable: false,
            draggable: false,
            buttons: [{
            	text : jQuery('#labelOK').val(),
                click: function() {
                    document.location.href = basePath + "/manage-admin-faq/delete-faq.do?adminFaqId=" + adminFaqId;
                },
            }]
        });
    });

    jQuery(document).on('click', '.editAdminFaq', function(e) {

        e.preventDefault();

        jQuery("#ajxBook").hide();

        adminFaqId = this.id;

        jQuery.ajax({
            url: basePath + "/manage-admin-faq/admin-faq-info.json?adminFaqId=" + this.id,
            type: 'GET',
            cache: false,
            dataType: 'json',
            timeout: 50000,
            error: function() {
                ajaxInProgress = false;
            },
            success: function(responseData) {

                if (responseData.type == "SUCCESS") {

                    jQuery("#questionEdit").html(responseData.question);
                    jQuery("#answerEdit").html(responseData.answer);
                    jQuery("#adminFaqId").val(responseData.adminFaqId);

                    jQuery(".updatePassengerCreditsDialog").dialog("open");

                } else {
                    displaySucessMsg(responseData.message);
                }
            }
        });
    });

    jQuery(".updatePassengerCreditsDialog").dialog({

        title: "Update Admin FAQ",
        autoOpen: false,
        width: 450,
        height: 380,
        resizable: false,
        draggable: false,
        modal: true,
        buttons: [{
            text: "Update",
            tabindex: 26,
            id: 'btnUpdateCreditsOk',
            click: function() {

            	jQuery("#questionEditError").html("");
            	jQuery("#answerEditError").html("");
            	
                if (jQuery("#questionEdit").val() == "") {

                    jQuery("#questionEditError").html("Question is required.");

                } else if (jQuery("#answerEdit").val() == "") {

                    jQuery("#answerEditError").html("Answer is required.");

                } else {

                    if (jQuery("#questionEdit").val().length > 4500) {

                        jQuery("#questionEditError").html("Question should not be greater than 4500 characters.");

                    } else if (jQuery("#answerEdit").val().length > 4500) {

                        jQuery("#answerEditError").html("Answer should not be greater than 4500 characters.");

                    } else {

                        jQuery.ajax({
                            url: basePath + "/manage-admin-faq/edit.json",
                            type: 'POST',
                            cache: false,
                            dataType: 'json',
                            data: {
                                question: jQuery("#questionEdit").val(),
                                answer: jQuery("#answerEdit").val(),
                                adminFaqId: jQuery("#adminFaqId").val()
                            },
                            timeout: 50000,
                            error: function() {
                                ajaxInProgress = false;
                            },
                            success: function(responseData) {

                                if (responseData.type == "SUCCESS") {

                                    displaySucessUpdateCreditsMsg(responseData.message);

                                    jQuery(".updatePassengerCreditsDialog").dialog("close");

                                } else {
                                    displaySucessMsg(responseData.message);
                                }
                            }
                        });
                    }
                }
            }
        }, {
            text: "Cancel",
            tabindex: 26,
            id: 'btnUpdateCreditsCancel',
            click: function() {
                jQuery(this).dialog("close");
            }
        }],
        backgroundColor: 'transparent !important'
    });

    jQuery("#btnCancel").click(function() {

        document.location.href = basePath + "/manage-admin-faq.do";
    });

    jQuery("#btnSearchAnnouncement").click(function() {

        searchString = jQuery("#searchBooking").val();

        document.location.href = basePath + "/manage-admin-faq.do?searchString=" + searchString;
    });

    adminAreaTable = jQuery('#adminFaqListTable').dataTable({

        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "iDisplayLength": 3,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-admin-faq/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {

            aoData.push({
                "name": "more_data",
                "value": jQuery("#searchBooking").val()
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
            "sWidth": "30%",
            "bSortable": false
        }, {
            "sWidth": "30%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }],
        "oLanguage": {
            "sUrl": langPath
        }
    });

    jQuery('.dataTables_length').hide();
    $("#adminFaqListTable_length").hide();
    $("#adminFaqListTable_first").html("<<");
    $("#adminFaqListTable_previous").html("<");
    $("#adminFaqListTable_next").html(">");
    $("#adminFaqListTable_last").html(">>");
});

function displaySucessMsg(message) {

    var displayStatusDialogHtml = jQuery('<div></div>').html(
        '<br><br><center>' + message + '</center><br><br>');

    displayStatusDialogHtml.dialog({
        modal: true,
        resizable: false,
        draggable: false,
        title: 'Message!',
        buttons: [{
        	text : jQuery('#labelOK').val(),
            click: function() {
                jQuery(this).dialog("close");
            },
        }]
    });
}

function fnGetSelected(oTableLocal) {

    var anSelected = fnGetSelectedTr(oTableLocal);

    if (anSelected[0] != undefined) {
        var rows = [];
        for (var i = 0; i < anSelected.length; i++) {
            var aPos = oTableLocal.fnGetPosition(anSelected[i]);
            var aData = oTableLocal.fnGetData(aPos);
            rows.push(aData);
        }

        return rows;
    } else {
        return null;
    }
}

function fnGetSelectedTr(oDataTable) {

    var aReturn = new Array();
    var aTrs = oDataTable.fnGetNodes();

    for (var i = 0; i < aTrs.length; i++) {
        if ($(aTrs[i]).hasClass('row_selected')) {
            aReturn.push(aTrs[i]);
        }
    }
    return aReturn;
}

function displaySucessUpdateCreditsMsg(message) {

    var displayStatusDialogHtml = jQuery('<div></div>').html(
        '<br><br><center>' + message + '</center><br><br>');

    displayStatusDialogHtml.dialog({
        modal: true,
        resizable: false,
        draggable: false,
        title: 'Message!',
        buttons: [{
        	text : jQuery('#labelOK').val(),
            click: function() {
                jQuery(this).dialog("close");
                document.location.href = basePath + "/manage-admin-faq.do";
            },
        }]
    });
}