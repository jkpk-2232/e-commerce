package com.utils.myhub;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.actions.UploadAction;
import com.webapp.models.CarTypeModel;
import com.webapp.models.ServiceModel;
import com.webapp.models.SubscriptionPackageModel;
import com.webapp.models.UserProfileModel;

public class FeedAutomationUtils {
	
	private static Logger logger = Logger.getLogger(FeedAutomationUtils.class);

	public static void uploadAutoFeed(String subscriptionPackageId, String loggedInuserId, String defaultVendorId, String serviceId) {

		BufferedImage image = null;

		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInuserId);

		String vendorId = null;

		if (subscriptionPackageId != null) {
			
			vendorId = defaultVendorId;

			SubscriptionPackageModel subscriptionPackageModel = SubscriptionPackageModel.getSubscriptionPackageDetailsById(subscriptionPackageId);

			CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(subscriptionPackageModel.getCarTypeId());

			try {

				String carType = null;

				String firstAppendString = "I,";
				
				if (carTypeModel.getCarType().toLowerCase().contains("Bike".toLowerCase())) {

					image = ImageIO.read(new File(WebappPropertyUtils.FEED_AUTOMATION_PATH + "bike.png"));
					carType = "Bike";

				} else if (carTypeModel.getCarType().toLowerCase().contains("Driver".toLowerCase())) {

					image = ImageIO.read(new File(WebappPropertyUtils.FEED_AUTOMATION_PATH + "personal_driver.png"));
					carType = "Personal";

				} else if (carTypeModel.getCarType().toLowerCase().contains("Auto".toLowerCase())) {

					image = ImageIO.read(new File(WebappPropertyUtils.FEED_AUTOMATION_PATH + "auto.png"));
					carType = "Auto";

				} else {
					image = ImageIO.read(new File(WebappPropertyUtils.FEED_AUTOMATION_PATH + "cab.png"));
					carType = "Car";
				}

				String secoundAppendString = " joined My Hub as a ";
				String thirdAppendString = carType + " driver.";
				
				// Get Graphics2D from the image
				Graphics2D g2d = image.createGraphics();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				String textWithspecial = firstAppendString + "##" + userProfileModel.getFullName().trim() + "##" + secoundAppendString + "##" + thirdAppendString;
				
				prepareAutoFeedImageUpload(g2d, textWithspecial, firstAppendString, secoundAppendString, userProfileModel, image, vendorId, loggedInuserId, subscriptionPackageId, null, thirdAppendString );

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			
			vendorId = loggedInuserId;

			try {

				image = ImageIO.read(new File(WebappPropertyUtils.FEED_AUTOMATION_PATH + "retailer.png"));

				// Get Graphics2D from the image
				Graphics2D g2d = image.createGraphics();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				ServiceModel serviceModel = ServiceModel.getServiceDetailsByServiceId(serviceId);

				String firstAppendString = "Hi,I am ";
				String secoundAppendString = " Partnerd With My Hub " ; 
				String thirdAppendString = "as a "+ serviceModel.getServiceName()+".";

				String textWithspecial = firstAppendString + "##" + userProfileModel.getFullName().trim() + "##" + secoundAppendString + "##" + thirdAppendString;

				prepareAutoFeedImageUpload(g2d, textWithspecial, firstAppendString, secoundAppendString, userProfileModel, image, vendorId, loggedInuserId, null, serviceModel.getServiceName(), thirdAppendString);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
	
	private static void prepareAutoFeedImageUpload(Graphics2D g2d, String textWithspecial, String firstAppendString, String secoundAppendString, 
				UserProfileModel userProfileModel, BufferedImage image, String vendorId, String loggedInuserId, String subscriptionPackageId, 
				String serviceName, String thirdAppendString ) {
		
		String feedMessage = null;
		
		
		
		if (subscriptionPackageId != null) {
			feedMessage = "As our drivers, you're our trusted resource for safe and reliable pick-up and drop services. Thank you for being a part of our team!";
		} else {
			feedMessage = "Exciting news! I'm thrilled to announce that "+ userProfileModel.getVendorBrandName() +" has teamed up with My Hub app to bring our "+ serviceName+ " services to you! \n"
						+ " Join us on this exciting journey as we make our services more accessible and convenient than ever before!  #ImAvailableOnMyHub";
		}

		int fontSize = 38;
		Font font = new Font("Comic Sans Ms", Font.ITALIC & Font.BOLD, fontSize);
		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics();

		// Define the maximum width for text wrapping
		int maxWidth = 1000;

		// Define initial position for the text
		int x = 100;
		int y = 850;

		// Map to hold word-color associations
		Map<String, Color> wordColorMap = new HashMap<>();
		wordColorMap.put(firstAppendString, Color.BLACK);
		wordColorMap.put(userProfileModel.getFullName().trim(), new Color(0x256fb7));
		wordColorMap.put(secoundAppendString, Color.BLACK);
		wordColorMap.put(thirdAppendString, Color.BLACK);

		// Wrap the text into lines
		List<String> lines = wrapText(textWithspecial, fontMetrics, maxWidth);
		
		// Draw each line with specific colors
		for (String line : lines) {
			int currentX = x;

			// Split line into words
			String[] words = line.split("##");
			
			for (String word : words) {
				// Apply color based on the word
				
				Color wordColor = wordColorMap.getOrDefault(word, Color.WHITE);
				g2d.setColor(wordColor);

				// Draw the word
				g2d.drawString(word, currentX, y);

				// Update x position for the next word
				currentX += fontMetrics.stringWidth(word + " ");
			}

			// Move to the next line
			y += fontMetrics.getHeight();
		}

		// Dispose of the graphics context and release any system resources
		g2d.dispose();

		// Save the modified image
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "png", os);
			
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			String tempStoragePath = WebappPropertyUtils.getWebAppProperty("tempDir");

			String generatedFileName = UUIDGenerator.generateUUID();

			File savedFile = new File(tempStoragePath, generatedFileName + ".png");

			FileOutputStream fos = new FileOutputStream(savedFile);

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

			String profileFilename = generatedFileName + ".png";

			String bucket = WebappPropertyUtils.getWebAppProperty("imageBucket");

			try {
				UploadAction imageUpload = new UploadAction();
				imageUpload.uploadImageToFloder(profileFilename, bucket, tempStoragePath, "feed");
			} catch (Exception e) {
				e.printStackTrace();
			}

			String profilePath = "feed/"  + profileFilename;
			
			logger.info(">>> file path >>>"+profileFilename);
			System.out.println("*** file path ***" + profilePath);
			
			FeedUtils.insertFeed(vendorId, "Welcome " + userProfileModel.getFullName(), feedMessage, profilePath, loggedInuserId, ProjectConstants.Image, "false", null, null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	// Function to wrap text into lines
	    private static List<String> wrapText(String text, FontMetrics fontMetrics, int maxWidth) {
	        List<String> lines = new ArrayList<>();
	        String[] words = text.split("##");
	        StringBuilder line = new StringBuilder();

	        for (String word : words) {
	            String testLine = line + word + "##";
	            int lineWidth = fontMetrics.stringWidth(testLine);
	            if (lineWidth > maxWidth) {
	                lines.add(line.toString());
	                line = new StringBuilder(word + "##");
	            } else {
	                line.append(word).append("##");
	            }
	        }

	        if (line.length() > 0) {
	            lines.add(line.toString());
	        }

	        return lines;
	    }
	
	
	

}
