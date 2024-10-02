jQuery(document).ready(function() {

	setMenuActiveSub("1");
	
	jQuery("#vendorStoreIdDivOuterDiv").hide();
	
	setMultiSelectPicker("vendorStoreId");
	
	clearErrorMessageFunction("serviceName");
    clearErrorMessageFunction("categoryName");
    
    jQuery("#serviceName").prop("readonly", true);
	jQuery("#categoryName").prop("readonly", true);
	
	 getVendorServiceCategoryInformationWise("add");
	
	
	jQuery("#vendorId").change(function() {
		
		getVendorServiceCategoryInformationWise("add");
		
		if (jQuery("#isProductForAllVendorStores").val() === "false") {
			getVendorStoresByVendorId(false, false);
		}
    });

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
	   jQuery("#import-csv-product").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	if (jQuery("#isProductForAllVendorStores").val() === "true") {
		jQuery("#vendorStoreIdDivOuterDiv").hide();
	} else {
		getVendorStoresByVendorId(false, false);
		jQuery("#vendorStoreIdDivOuterDiv").show();
	}
	
	jQuery("#isProductForAllVendorStores").change(function() {
		
		if (jQuery("#isProductForAllVendorStores").val() === "true") {
			jQuery("#vendorStoreIdDivOuterDiv").hide();
		} else {
			getVendorStoresByVendorId(false, false);
			jQuery("#vendorStoreIdDivOuterDiv").show();
		}
    });
    
	
});    