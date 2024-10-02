jQuery(document).ready(function() {
    
   //setMenuActiveSub("72");
   setMenuActiveSub("59"); 
   
    setMultiSelectPicker("regionList");
    setMultiSelectPicker("accessList");
    setMultiSelectPicker("exportAccessList");

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["add-admin-user"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});