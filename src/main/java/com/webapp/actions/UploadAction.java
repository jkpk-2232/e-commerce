package com.webapp.actions;

import java.io.File;
import java.io.IOException;

import com.jeeutils.S3Utils;

public class UploadAction extends BusinessAction {

	private File savedFile = null;

	public void uploadImage(String path, String bucket, String tempStoragePath) {

		String imageName[] = path.split("/");

		savedFile = new File(tempStoragePath + "/" + imageName[imageName.length - 1]);

		try {
			S3Utils.uploadToBucket(bucket, savedFile);
			deleteTempSignatureFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteTempSignatureFile() throws IOException {

		if (savedFile != null && savedFile.exists()) {
			savedFile.delete();
		}
	}
	
	public void uploadImageToFloder(String path, String bucket, String tempStoragePath, String folderName) {

		String imageName[] = path.split("/");

		savedFile = new File(tempStoragePath + "/" + imageName[imageName.length - 1]);

		try {
			S3Utils.uploadFolderWiseToBucket(bucket, savedFile, imageName[imageName.length - 1], folderName);
			deleteTempSignatureFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

}