var mediaList = [];
var showADList = false;
var finalFile ;
var url = '';
var filename = '';
var fileType = '';
var totalDuration = '';

jQuery(document).ready(function() {
	
	var table = jQuery('#dataTable').DataTable();
	
	setMenuActiveSub("38");
	
	jQuery("#showADList").hide();
	jQuery("#showSave").hide();
	
	fileUpload("uploadImageVideo","hiddenUploadImageVideo_dummy","hiddenUploadImageVideo");
	
	jQuery("#saveAll").click(function(e) {

		e.preventDefault();

		jQuery("#add-ad").submit();
	});

	jQuery("#btnCancel").click(function(e) {

		e.preventDefault();

		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	jQuery("#addObject").click(function() {
		
		if (jQuery("#ad_title").val()) {
			
			let ADObject = {
				ad_title: jQuery("#ad_title").val(),
				mediaTitle: jQuery("#media_title").val(),
				mediaType: jQuery("#mediaType").val(),
				url: url,
				format: fileType,
				resolution_id: jQuery('#resolution').val(),
				duration: totalDuration,
				fileName: fileName,
				vendorId: jQuery("#vendorId").val()
			}
			
			mediaList.push(ADObject);
			jQuery("#adModel").prop("value", JSON.stringify(mediaList)); 
		}
		
		
		if (mediaList.length > 0) {
			
			jQuery("#showADList").show();
			jQuery("#showSave").show();
		} else {
			
			jQuery("#showADList").hide();
			jQuery("#showSave").hide();
		}
	

		var data = {

			"data": mediaList
		};


		clearDataTable();
		
		jQuery("#add-ad").trigger("reset");

		// Populate the DataTable
		jQuery.each(data.data, function(index, item) {
			 var button = '<button data-toggle="tooltip" style="margin:5px;" class="btn btn-outline-theme" title="Delete" onclick="remove()"><i class="fas fa-lg fa-fw me-2 fa-trash-alt" ></i></button>';
			
			table.row.add([
				item.ad_title,
				item.mediaTitle,
				item.mediaType,
				//item.resolution_id,
				item.fileName,
				item.duration,
				button
			]);
		});

		table.draw();

	});
	
	function clearDataTable() {
		table.clear().draw();
	}
	
	// Add a click event listener to the button inside each row
	jQuery('#dataTable tbody').on('click', 'button', function() {
		// Get the DataTable row associated with the clicked button
		var row = $(this).closest('tr');

		// Get the index of the row in the DataTable
		var rowIndex = table.row(row).index();

		mediaList.splice(rowIndex, 1);
		
		jQuery("#adModel").prop("value", JSON.stringify(mediaList));
		if (mediaList.length > 0) {
			
			jQuery("#showADList").show();
			jQuery("#showSave").show();
		} else {
			
			jQuery("#showADList").hide();
			jQuery("#showSave").hide();
		}
		// Send the row index value wherever needed
		
		table.row(row).remove().draw();
		// Example: You can send the rowIndex to a function or an AJAX call
	});
	
	
	jQuery("#uploadImageVideo").on('change', function() {
		
		totalDuration = '';
		var videoInput = document.getElementById('uploadImageVideo');
		
		var file = videoInput.files[0];

		if (file) {
			finalFile = file;
			var video = document.createElement('video');
			video.preload = 'metadata';

			fileName = file.name;
			fileType = fileName.split('.').pop();

			video.onloadedmetadata = function() {
				window.URL.revokeObjectURL(video.src);
				var duration = video.duration;
				
				totalDuration = Math.floor(duration % 60);
			};

			video.src = URL.createObjectURL(file);
		} else {
			
			fileName = file.name;
			fileType = fileName.split('.').pop();
		}

	});
	
});

function fileUpload(id, previewFile, saveToFile){
	
	jQuery("#" + id).fileinput({
		theme: "gly",
		autoReplace: true,
		maxFileCount: 1,
		initialPreview: [
			"<img style='height:100%' src= '" + basePath + jQuery("#" + previewFile).val() + "'>",
		],
		uploadUrl: basePath + "/userimage/uploadImageToFolder.do?folderName=LED"
	});
	
	jQuery("#" + id).on("fileuploaded", function(event, data, previewId, index) {
    	
        jQuery("#" + saveToFile).val(data.response.profileImageURL);
        url = data.response.profileImageURL;
        
        console.log("Saving image to attr" + jQuery("#" + saveToFile).val());
    });
    
    jQuery(".kv-file-content").css("height", "160px");
}