jQuery(document).ready(function() {
    
   // setMenuActive("4");
   setMenuActiveSub("59");
   
    setMultiSelectPicker("regionList");
    setMultiSelectPicker("accessList");
    setMultiSelectPicker("exportAccessList");

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["edit-admin-user"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});