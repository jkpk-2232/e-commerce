jQuery(document).ready(function() {
	
	jQuery("#btnMultiCitySettings").addClass('settingsButtonActiveClass');
	jQuery("#btnMultiCitySettings div").addClass('activeSettingsButtonsText');
	jQuery("#btnMultiCitySettings").parent().css({
        'display': 'block'
    });
	
	jQuery("#btnSearchAnnouncement").click(function() {
    	
        searchString = jQuery("#searchBooking").val();

        document.location.href = basePath + "/manage-multicity-settings-country.do?searchString=" + searchString;
    });
    
    adminAreaTable = jQuery('#adminAreaListTable').dataTable({

        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "iDisplayLength": 10,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-multicity-settings-country/list.json",
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
            "sWidth": "20%",
            "bSortable": false
        }, {
            "sWidth": "20%",
            "bSortable": false
        }, {
            "sWidth": "20%",
            "bSortable": false
        }, {
            "sWidth": "20%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }]
    });

    jQuery('.dataTables_length').hide();
    $("#adminAreaListTable_length").hide();
    $("#adminAreaListTable_first").html("<<");
    $("#adminAreaListTable_previous").html("<");
    $("#adminAreaListTable_next").html(">");
    $("#adminAreaListTable_last").html(">>");
    
    jQuery(".ui-corner-br").addClass('ui-widget-header_custom ');

    jQuery("#adminAreaListTable tbody").click(function(event) {
        if (jQuery(event.target.parentNode).hasClass('row_selected')) {
            jQuery(event.target.parentNode).removeClass('row_selected');

        } else {
            jQuery('#adminAreaListTable').find('tr').each(function() {
                jQuery('tr').removeClass('row_selected');

            });
            jQuery(event.target.parentNode).addClass('row_selected');

            var id = "";

            var anSelected = fnGetSelected(adminAreaTable);

            if (anSelected == "" || anSelected == null) {
                return;
            } else {
                for (var i = 0; i < anSelected.length; i++) {
                    var data1 = anSelected[i];

                    if (i > 0) {
                        id += ",";
                    }

                    id += data1[0];

                }
                if (id.length == 0) {
                    id = "NoData";
                }
            }

            document.location.href = basePath + "/manage-multicity-settings-city.do?multicityCountryId=" + id;
        }
    });
});

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