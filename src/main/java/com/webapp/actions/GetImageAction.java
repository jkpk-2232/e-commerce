package com.webapp.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.json.JSONException;

import com.jeeutils.S3Utils;
import com.utils.myhub.WebappPropertyUtils;

@Path("/getimage")
public class GetImageAction extends BusinessAction {

	@GET
	//@formatter:off
	public void getFile(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@Context ServletContext context,
			@QueryParam("imageId") String fileName
			) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequest(request, response);

		try {

			String bucket = WebappPropertyUtils.getWebAppProperty("imageBucket");

			File fileToWrite = new File(WebappPropertyUtils.getWebAppProperty("tempDir") + "/" + fileName);
			S3Utils.downloadFromBucket(bucket, fileName, fileToWrite);

			byte[] image = getbyteArrayOfImage(fileToWrite);

			if (image == null) {
				throw new ServletException(request.getRequestURI() + " : image not found");
			}

			response.setContentLength(image.length);

			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(image, 0, image.length);
			outputStream.flush();
			outputStream.close();

			if (fileToWrite != null && fileToWrite.exists()) {
				fileToWrite.delete();
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	@Path("/fromFolder")
	@GET
	//@formatter:off
	public void getFileFromFolder (
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@Context ServletContext context,
			@QueryParam("folderName") String folderName,
			@QueryParam("imageId") String fileName
			) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequest(request, response);

		try {

			String bucket = WebappPropertyUtils.getWebAppProperty("imageBucket");
			
			// File fileToWrite = new File(WebappPropertyUtils.getWebAppProperty("tempDir") + "/" + folderName + "/" + fileName);
			File fileToWrite = new File(WebappPropertyUtils.getWebAppProperty("tempDir") + "/" + fileName);
			S3Utils.downloadFromBucketFolder(bucket, fileName, fileToWrite, folderName);

			byte[] image = getbyteArrayOfImage(fileToWrite);

			if (image == null) {
				throw new ServletException(request.getRequestURI() + " : image not found");
			}

			response.setContentLength(image.length);

			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(image, 0, image.length);
			outputStream.flush();
			outputStream.close();

			if (fileToWrite != null && fileToWrite.exists()) {
				fileToWrite.delete();
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}

	private static byte[] getbyteArrayOfImage(File f) {

		FileInputStream fin = null;
		FileChannel ch = null;

		byte[] bytes = null;

		try {
			fin = new FileInputStream(f);

			ch = fin.getChannel();

			int size = (int) ch.size();

			MappedByteBuffer buf = ch.map(MapMode.READ_ONLY, 0, size);

			bytes = new byte[size];

			buf.get(bytes);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {

				if (fin != null) {
					fin.close();
				}

				if (ch != null) {
					ch.close();
				}

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		return bytes;
	}

}