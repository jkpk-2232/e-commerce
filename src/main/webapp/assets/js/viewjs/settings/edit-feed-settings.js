jQuery(document).ready(function() {
	console.log("*** function ***")

	setMenuActiveSub("13");
	setMenuActiveSub("btnFeedSettings");

	if (jQuery("#feedFareDetailsErrorHidden").val() != "") {
		jQuery("#feedFareDetailsError").text(jQuery("#feedFareDetailsErrorHidden").val());
	}

	selectCarAvailability(jQuery("#feedFareAvailableList").val());

	jQuery("#btnSave").on("click", function(e) {
		
		var serviceAvailableList = [];
		jQuery("input:checked").each(function() {
			console.log("$(this).val()\t" + $(this).attr("name"));
			serviceAvailableList.push($(this).attr("name"));
		});
		serviceAvailableList.join("&&");

		var kvpairs = [];
		var form = document.forms.editFeedSettings;
		for (var i = 0; i < form.elements.length; i++) {
			var e = form.elements[i];
			if ((e.name != null && e.name != "") && (e.value != null && e.value != "")) {
				if (e.name.includes("_IsAvailable") && e.value.includes("_IsAvailable")) {

				} else {
					kvpairs.push(encodeURIComponent(e.name) + "=" + encodeURIComponent(e.value));
				}
			}
		}
		var queryString = kvpairs.join("&&&");

		console.log(queryString);
		jQuery("#queryString").val(queryString);
		jQuery("#servicesActiveList").val(serviceAvailableList);

		document.forms["editFeedSettings"].submit();
	});

	jQuery("#btnCancel").on("click", function(e) {
		e.preventDefault();

		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
});