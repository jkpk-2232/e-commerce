package com.webapp.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

@Path("/csv/download/file")
public class CsvFileDownlaodAction extends BusinessAction {

	@GET
	//@formatter:off
	public void getFile(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@Context ServletContext context,
			@QueryParam("fileName") String fileName
			) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequest(request, response);

		try {

			String bucket = WebappPropertyUtils.getWebAppProperty("imageBucket");

			File fileToWrite = new File(WebappPropertyUtils.getWebAppProperty("tempDir") + "/" + fileName);

			S3Utils.downloadFromBucket(bucket, fileName, fileToWrite);

			FileInputStream fis = new FileInputStream(fileToWrite);

			BufferedInputStream in = new BufferedInputStream(fis);

			response.setHeader("Content-Disposition", " inline; filename=" + fileToWrite.getName());

			ServletOutputStream out = response.getOutputStream();

			byte[] buffer = new byte[4 * 1024];

			int data;

			while ((data = in.read(buffer)) != -1) {
				out.write(buffer, 0, data);
			}

			out.flush();
			out.close();
			in.close();

			if (fileToWrite != null && fileToWrite.exists()) {
				fileToWrite.delete();
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}

}