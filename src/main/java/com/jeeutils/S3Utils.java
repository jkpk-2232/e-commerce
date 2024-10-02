package com.jeeutils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import com.utils.myhub.WebappPropertyUtils;

public class S3Utils {

	protected static Logger logger = Logger.getLogger(S3Utils.class);

	private static S3Utils s3UtilsInstance = null;

	private S3Service s3Service;

	private static S3Utils getInstance() throws IOException, S3ServiceException {

		if (s3UtilsInstance != null) {
			return s3UtilsInstance;
		}

		return createS3UtilsInstance();
	}

	private static S3Utils createS3UtilsInstance() throws IOException, S3ServiceException {

		s3UtilsInstance = new S3Utils();

		return s3UtilsInstance;
	}

	private S3Utils() throws IOException, S3ServiceException {

		String awsAccessKey = WebappPropertyUtils.getWebAppProperty("awsAccessKey");

		String awsSecretKey = WebappPropertyUtils.getWebAppProperty("awsSecretKey");

		AWSCredentials awsCredentials = new AWSCredentials(awsAccessKey, awsSecretKey);

		s3Service = new RestS3Service(awsCredentials);
	}

	private void upload(String bucket, File fileToUpload) throws IOException, S3ServiceException, NoSuchAlgorithmException {

		if (fileToUpload.exists()) {

			S3Object s3FileObject = new S3Object(fileToUpload);

			S3Bucket s3Bucket = s3Service.getBucket(bucket);

			s3Service.putObject(s3Bucket, s3FileObject);

		} else {

			throw new IOException(fileToUpload.getAbsolutePath() + " does not exists");
		}
	}

	private void download(String bucket, String nameOfFileToDownload, File fileToWrite) throws IOException, ServiceException {

		S3Object objectComplete = s3Service.getObject(bucket, nameOfFileToDownload);

		BufferedInputStream inputStream = null;
		BufferedOutputStream ouputStream = null;

		try {
			inputStream = new BufferedInputStream(objectComplete.getDataInputStream());
			ouputStream = new BufferedOutputStream(new FileOutputStream(fileToWrite));

			byte[] buffer = new byte[1024];

			int bytesRead = -1;

			while ((bytesRead = inputStream.read(buffer)) > -1) {
				ouputStream.write(buffer, 0, bytesRead);
			}

		} finally {

			if (inputStream != null) {
				inputStream.close();
			}

			if (ouputStream != null) {
				ouputStream.flush();
				ouputStream.close();
			}
		}
	}

	public BufferedInputStream downloadInputStream(String bucket, String nameOfFileToDownload) throws IOException, ServiceException {

		S3Object objectComplete = s3Service.getObject(bucket, nameOfFileToDownload);

		BufferedInputStream inputStream = null;

		try {
			inputStream = new BufferedInputStream(objectComplete.getDataInputStream());
		} finally {

			if (inputStream != null) {
				inputStream.close();
			}

		}

		return inputStream;
	}

	private void download(String bucket, String nameOfFileToDownload, OutputStream outputStreamToWrite) throws IOException, ServiceException {

		S3Object objectComplete = s3Service.getObject(bucket, nameOfFileToDownload);

		BufferedInputStream inputStream = null;
		BufferedOutputStream ouputStream = null;

		try {
			inputStream = new BufferedInputStream(objectComplete.getDataInputStream());
			ouputStream = new BufferedOutputStream(outputStreamToWrite);

			byte[] buffer = new byte[1024];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) > -1) {
				ouputStream.write(buffer, 0, bytesRead);
			}

		} finally {

			if (inputStream != null) {
				inputStream.close();
			}

			if (ouputStream != null) {
				ouputStream.flush();
				ouputStream.close();
			}
		}
	}

	public static void uploadToBucket(String bucket, File fileToUpload) throws IOException, S3ServiceException, NoSuchAlgorithmException {

		S3Utils s3Utils = getInstance();

		s3Utils.upload(bucket, fileToUpload);
	}

	public static void downloadFromBucket(String bucket, String nameOfFileToDownload, File fileToWrite) throws IOException, ServiceException {

		S3Utils s3Utils = getInstance();

		s3Utils.download(bucket, nameOfFileToDownload, fileToWrite);

		System.out.println("Done");
	}

	public static BufferedInputStream downloadFromBucketInputStrem(String bucket, String nameOfFileToDownload) throws IOException, ServiceException {

		S3Utils s3Utils = getInstance();

		return s3Utils.downloadInputStream(bucket, nameOfFileToDownload);
	}

	public static void downloadFileOutputStreem(String bucket, String nameOfFileToDownload, String tempDirPath, HttpServletResponse response, long fileSize) throws IOException, ServiceException {

		String fileName = tempDirPath + File.separatorChar + nameOfFileToDownload;

		response.setHeader("Content-Disposition", " inline; filename=" + fileName);

		response.setHeader("Content-Length", String.valueOf(fileSize));

		ServletOutputStream out = response.getOutputStream();

		S3Utils s3Utils = getInstance();

		s3Utils.download(bucket, nameOfFileToDownload, out);
	}

	public static void downloadInputStream(String bucket, String nameOfFileToDownload, InputStream inputStream) throws IOException, ServiceException {

		S3Utils s3Utils = getInstance();

		s3Utils.download(bucket, nameOfFileToDownload, inputStream);
	}

	private void download(String bucket, String nameOfFileToDownload, InputStream inputStream) throws IOException, ServiceException {

		S3Object objectComplete = s3Service.getObject(bucket, nameOfFileToDownload);

		try {
			inputStream = new BufferedInputStream(objectComplete.getDataInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteFileFromBucket(String bucketName, String fileName) throws S3ServiceException, IOException, NoSuchAlgorithmException {

		S3Utils s3Utils = getInstance();

		s3Utils.delete(bucketName, fileName);
	}

	private void delete(String bucket, String fileKeyToDelete) throws IOException, S3ServiceException, NoSuchAlgorithmException {

		try {
			s3Service.deleteObject(bucket, fileKeyToDelete);
		} catch (org.jets3t.service.ServiceException e) {
			e.printStackTrace();
		}
	}

	public static void deleteProfileImageFromBucket(String photoUrl) throws S3ServiceException, NoSuchAlgorithmException, IOException {

		String fileName = "";

		if (photoUrl.contains("=")) {
			String fileNameUrl[] = photoUrl.split("=");
			fileName = fileNameUrl[1];
		} else {
			fileName = photoUrl;
		}

		S3Utils.deleteFileFromBucket(WebappPropertyUtils.getWebAppProperty("imageBucket"), fileName);
	}

	public static void uploadFolderWiseToBucket(String bucket, File fileToUpload, String fileName, String folderName) throws NoSuchAlgorithmException, IOException, S3ServiceException {
		
		S3Utils s3Utils = getInstance();

		s3Utils.uploadToBucketFolder(bucket, fileToUpload, fileName, folderName);
		
	}
	
	private  void uploadToBucketFolder(String bucket, File fileToUpload, String fileName, String folderName) throws NoSuchAlgorithmException, IOException, S3ServiceException {
		
		if (fileToUpload.exists()) {

			S3Object s3FileObject = new S3Object(fileToUpload);
			
			s3FileObject.setKey(folderName + "/" + fileName);

			S3Bucket s3Bucket = s3Service.getBucket(bucket);

			s3Service.putObject(s3Bucket, s3FileObject);

		} else {

			throw new IOException(fileToUpload.getAbsolutePath() + " does not exists");
		}
	}

	public static void downloadFromBucketFolder(String bucket, String nameOfFileToDownload, File fileToWrite, String folderName) throws IOException, ServiceException {
		
		S3Utils s3Utils = getInstance();

		s3Utils.downloadFromFolder(bucket, nameOfFileToDownload, fileToWrite, folderName);

		System.out.println("Done");
		
	}
	
	private void downloadFromFolder(String bucket, String nameOfFileToDownload, File fileToWrite, String folderName) throws IOException, ServiceException {

		String objectKey = folderName + "/" + nameOfFileToDownload;
		
		S3Object objectComplete = s3Service.getObject(bucket, objectKey);

		BufferedInputStream inputStream = null;
		BufferedOutputStream ouputStream = null;

		try {
			inputStream = new BufferedInputStream(objectComplete.getDataInputStream());
			ouputStream = new BufferedOutputStream(new FileOutputStream(fileToWrite));

			byte[] buffer = new byte[1024];

			int bytesRead = -1;

			while ((bytesRead = inputStream.read(buffer)) > -1) {
				ouputStream.write(buffer, 0, bytesRead);
			}

		} finally {

			if (inputStream != null) {
				inputStream.close();
			}

			if (ouputStream != null) {
				ouputStream.flush();
				ouputStream.close();
			}
		}
	}

}