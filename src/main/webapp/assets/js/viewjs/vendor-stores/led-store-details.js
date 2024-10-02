jQuery(document).ready(function() {

    setMenuActiveDoubleTab("21", "42");

    
    jQuery("#btnSave").click(function() {
        jQuery("#led-store-details").submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });

    jQuery("#categoryGroup").change(function(e) {

        e.preventDefault();
        
        getStoreCategories();
    });
    
    jQuery("#city").change(function(e) {

        e.preventDefault();
        
        getLocations();
    });
    
});

function getStoreCategories(categoryGroup) {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-vendor-store/get-store-categories.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "categoryGroup": jQuery("#categoryGroup").val(),
        },
        success: function(responseData) {

        	console.log("\n\n\tresponseData.type\t"+responseData.type);
        	console.log("\n\n\tresponseData.carTypeListOptions\t"+responseData.storeCategoryOptions);
        	jQuery("#storeCategory").children('option').remove();
  	       	jQuery("#storeCategory").append(responseData.storeCategoryOptions);
  	       	jQuery("#storeCategory").trigger("chosen:updated");
        }
    });
}

function getLocations(city) {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-vendor-store/get-locations.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "city": jQuery("#city").val(),
        },
        success: function(responseData) {

        	console.log("\n\n\tresponseData.type\t"+responseData.type);
        	console.log("\n\n\tresponseData.carTypeListOptions\t"+responseData.locationOptions);
        	jQuery("#location").children('option').remove();
  	       	jQuery("#location").append(responseData.locationOptions);
  	       	jQuery("#location").trigger("chosen:updated");
        }
    });
}