jQuery(document).ready(function() {
    
	setMenuActiveSub("49");
    
    setMultiSelectPicker("vendorStoreId");
	setMultiSelectPicker("regionList");
    setMultiSelectPicker("accessList");
    
    initializeFileUpload("profileImgUrl", "hiddenPhotoUrl_dummy", "hiddenPhotoUrl", false);
    
    var $elem = jQuery("#regionList");
	$elem.picker();
	$elem.on("sp-change", function() {
		var regionIds = jQuery("#regionList").picker("get");
		var vendorStoreIds = jQuery("#vendorStoreId").picker("get");
        console.log("regionId: " + regionIds);
        console.log("vendorStoreIds: " + vendorStoreIds);
        getVendorStoreListByRegion(regionIds, vendorStoreIds);
	});
    
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
			
		document.forms["add-sub-vendor"].submit();
	});

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});