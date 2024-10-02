package com.webapp.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import com.jeeutils.StringUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.WebappPropertyUtils;

@Path("/userimage")
public class UserImageAction extends BusinessAction {

	private File savedFile = null;

	PrintWriter writer = null;

	InputStream is = null;

	FileOutputStream fos = null;

	String imageName = "";
	String bucketName = "";
	String tempStoragePath = "";
	String filename = "";

	int thumbWidth = 120;
	int thumbHeight = 120;
	int quality = 100;
	int avatarWidth = 300;
	int avatarHeight = 300;

	boolean isMultipart = false;

	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, "*/*" })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, "application/octet-stream" })
	//@formatter:off
	public Response addPost(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@Context ServletContext context
			) throws ServletException, IOException {
	//@formatter:on

		JSONObject jsonObject = new JSONObject();

		tempStoragePath = WebappPropertyUtils.getWebAppProperty("tempDir");
		logger.info("\n\n\n\ttempStoragePath\t" + tempStoragePath);

		filename = request.getHeader("X-File-Name");

		String generatedFileName = UUIDGenerator.generateUUID();

		try {

			isMultipart = ServletFileUpload.isMultipartContent(request);

			if (isMultipart) {

				logger.info("ismultipart");

				@SuppressWarnings("unchecked")
				List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

				for (FileItem item : items) {

					if (item.isFormField()) {

					} else {

						String str = item.getName();
						String replace = str.replace("\\", ":");
						String spilt[] = replace.split(":");

						filename = spilt[spilt.length - 1];

						savedFile = new File(tempStoragePath, generatedFileName + StringUtils.getExtension(filename));

						item.write(savedFile);
					}
				}

			} else {

				savedFile = new File(tempStoragePath, generatedFileName + StringUtils.getExtension(filename));

				is = request.getInputStream();

				fos = new FileOutputStream(savedFile);

				while (true) {

					int data = is.read();

					if (data == -1) {
						break;
					}

					fos.write(data);
				}

				fos.flush();
				fos.close();
				is.close();
			}

			String profileFilename = generatedFileName + StringUtils.getExtension(filename);

			String bucket = WebappPropertyUtils.getWebAppProperty("imageBucket");

			try {
				UploadAction imageUpload = new UploadAction();
				imageUpload.uploadImage(profileFilename, bucket, tempStoragePath);
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(200).entity("ERROR").build();
			}

			if (!isMultipart) {
				is.close();
				fos.close();
			}

			String profileImage = "getimage.do?imageId=" + profileFilename;
			String profileWithBasePath = "/" + profileImage;

			jsonObject.put("profileImageURL", profileWithBasePath);

			response.setStatus(HttpServletResponse.SC_OK);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (IOException ex) {
			ex.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return Response.ok().header("Content-Type", "text/html;charset=UTF-8").entity(jsonObject.toString()).build();
	}
	
	@Path("/uploadImageToFolder")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, "*/*" })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, "application/octet-stream" })
	//@formatter:off
	public Response uploadImageToFolder (
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@Context ServletContext context,
			@QueryParam("folderName") String folderName
			) throws ServletException, IOException {
	//@formatter:on

		JSONObject jsonObject = new JSONObject();

		// tempStoragePath = WebappPropertyUtils.getWebAppProperty("tempDir")+folderName;
		tempStoragePath = WebappPropertyUtils.getWebAppProperty("tempDir");
		logger.info("\n\n\n\ttempStoragePath\t" + tempStoragePath);

		filename = request.getHeader("X-File-Name");

		String generatedFileName = UUIDGenerator.generateUUID();

		try {

			isMultipart = ServletFileUpload.isMultipartContent(request);

			if (isMultipart) {

				logger.info("ismultipart");

				@SuppressWarnings("unchecked")
				List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

				for (FileItem item : items) {

					if (item.isFormField()) {

					} else {

						String str = item.getName();
						String replace = str.replace("\\", ":");
						String spilt[] = replace.split(":");

						filename = spilt[spilt.length - 1];

						savedFile = new File(tempStoragePath, generatedFileName + StringUtils.getExtension(filename));

						item.write(savedFile);
					}
				}

			} else {

				savedFile = new File(tempStoragePath, generatedFileName + StringUtils.getExtension(filename));

				is = request.getInputStream();

				fos = new FileOutputStream(savedFile);

				while (true) {

					int data = is.read();

					if (data == -1) {
						break;
					}

					fos.write(data);
				}

				fos.flush();
				fos.close();
				is.close();
			}

			String profileFilename = generatedFileName + StringUtils.getExtension(filename);

			String bucket = WebappPropertyUtils.getWebAppProperty("imageBucket");

			try {
				UploadAction imageUpload = new UploadAction();
				imageUpload.uploadImageToFloder(profileFilename, bucket, tempStoragePath, folderName);
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(200).entity("ERROR").build();
			}

			if (!isMultipart) {
				is.close();
				fos.close();
			}

			String profileImage = "getimage/fromFolder.do?folderName="+folderName+"&imageId=" + profileFilename;
			String profileWithBasePath = "/" + profileImage;

			jsonObject.put("profileImageURL", profileWithBasePath);
			jsonObject.put("filePath", folderName + "/" + profileFilename);

			response.setStatus(HttpServletResponse.SC_OK);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (IOException ex) {
			ex.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return Response.ok().header("Content-Type", "text/html;charset=UTF-8").entity(jsonObject.toString()).build();
	}

}