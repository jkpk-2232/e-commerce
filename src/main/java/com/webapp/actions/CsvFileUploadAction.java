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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import com.jeeutils.FrameworkConstants;
import com.utils.myhub.WebappPropertyUtils;

@Path("/csv/upload/file")
public class CsvFileUploadAction extends BusinessAction {

	private File savedFile = null;

	PrintWriter writer = null;

	InputStream is = null;

	FileOutputStream fos = null;

	String tempStoragePath = "";
	String filename = "";

	boolean isMultipart = false;

	FileOutputStream fos1 = null;

	private File originalSavedFile = null;

	String originalFileName = "";

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response addForm(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res
			) throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		data.put("contextPath", req.getContextPath());

		request.setAttribute(FrameworkConstants.LOADED_VIEW, true);

		return loadView("/csv-upload.jsp");
	}

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

		filename = request.getHeader("X-File-Name");

		try {
			isMultipart = ServletFileUpload.isMultipartContent(request);

			if (isMultipart) {

				@SuppressWarnings("unchecked")
				List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

				for (FileItem item : items) {

					if (item.isFormField()) {

					} else {

						String str = item.getName();
						String replace = str.replace("\\", ":");
						String spilt[] = replace.split(":");

						filename = spilt[spilt.length - 1];
						savedFile = new File(tempStoragePath, filename);
						item.write(savedFile);

						originalFileName = "original_" + filename;
						originalSavedFile = new File(tempStoragePath, originalFileName);
						item.write(originalSavedFile);
					}
				}

			} else {

				savedFile = new File(tempStoragePath, filename);
				originalSavedFile = new File(tempStoragePath, originalFileName);

				is = request.getInputStream();

				fos = new FileOutputStream(savedFile);
				fos1 = new FileOutputStream(originalSavedFile);

				while (true) {

					int data = is.read();

					if (data == -1) {

						break;
					}

					fos.write(data);
					fos1.write(data);
				}

				fos.flush();
				fos.close();
				is.close();

				fos1.flush();
				fos1.close();
			}

			String profileFilename = filename;
			String bucket = WebappPropertyUtils.getWebAppProperty("imageBucket");

			try {
				UploadAction imageUpload = new UploadAction();
				imageUpload.uploadImage(profileFilename, bucket, tempStoragePath);
				imageUpload.uploadImage(originalFileName, bucket, tempStoragePath);
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(200).entity("ERROR").build();
			}

			if (!isMultipart) {
				is.close();
				fos.close();
				fos1.close();
			}

			String profileImage = "csv/download/file.do?fileName=" + profileFilename;

			String profileWithBasePath = "/" + profileImage;

			jsonObject.put("fileName", profileWithBasePath);

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