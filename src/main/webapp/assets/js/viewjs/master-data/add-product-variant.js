jQuery(document).ready(function() {
	
	setMenuActiveSub("31");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#add-product-variant").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	jQuery("#myFunction").click(function(e){
		console.log("*** select ***");
		jQuery("#brandFields").show();	
		
	})
	
	
});