jQuery(document).ready(function() {
	
	//setMenuActive("51");
		setMenuActiveSub("61");
		
	setMultiSelectPicker("regionList");
    setMultiSelectPicker("accessList");
    setMultiSelectPicker("exportAccessList");
    
    initializeFileUpload("vendorBrandImage", "hiddenVendorBrandImage_dummy", "hiddenVendorBrandImage");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["edit-erp-user"].submit();
    });
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});