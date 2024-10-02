jQuery(document).ready(function() {
    
   // setMenuActive("21");
   setMenuActiveSub("50");
    
    setMultiSelectPicker("regionList");
    setMultiSelectPicker("accessList");
    setMultiSelectPicker("exportAccessList");

    initializeFileUpload("vendorBrandImage", "hiddenVendorBrandImage_dummy", "hiddenVendorBrandImage");
    initializeFileUpload("profileImgUrl", "hiddenPhotoUrl_dummy", "hiddenPhotoUrl", false);
    
    var $elem = jQuery("#regionList");
	$elem.picker();
	$elem.on("sp-change", function() {
		var regionIds = jQuery("#regionList").picker("get");
        console.log("regionId: " + regionIds);
        getVendorStoreListByRegion(regionIds);
	});
    
    jQuery("#serviceId").change(function() {
    	getCategoryServiceWiseAddEdit(false, "");
    });
    
    jQuery("#selfDeliveryFeeParent").hide();
    
    if (jQuery("#isSelfDeliveryWithinXKm").val() === "true") {
    	jQuery("#selfDeliveryFeeParent").show();
    } else {
    	jQuery("#selfDeliveryFeeParent").hide();
    }
    
    jQuery("#isSelfDeliveryWithinXKm").change(function() {

    	if (jQuery("#isSelfDeliveryWithinXKm").val() === "true") {
        	jQuery("#selfDeliveryFeeParent").show();
        } else {
        	jQuery("#selfDeliveryFeeParent").hide();
        }
    });
    
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();

		if (jQuery("#serviceId").val() == null || jQuery("#serviceId").val() == "") {
			displayBootstrapMessage("Select at least one super service.");
            return;
		}
		
		if (jQuery("#categoryId").val() == null || jQuery("#categoryId").val() == "") {
			displayBootstrapMessage("Select at least one category.");
            return;
		}
        
        document.forms["edit-vendor"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});