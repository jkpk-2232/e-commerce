jQuery(document).ready(function() {
	
	//setMenuActiveSub("74");
	setMenuActiveSub("61");
	
	setMultiSelectPicker("regionList");
    setMultiSelectPicker("accessList");
    setMultiSelectPicker("exportAccessList");
    setMultiSelectPicker("productCategoryId");
    
    initializeFileUpload("vendorBrandImage", "hiddenVendorBrandImage_dummy", "hiddenVendorBrandImage");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		document.forms["add-erp-user"].submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
	
});