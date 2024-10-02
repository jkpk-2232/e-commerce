package com.webapp.actions.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.DriverAppVersionModel;
import com.webapp.models.DriverVersionLightModel;
import com.webapp.models.VendorAppVersionModel;
import com.webapp.models.VendorVersionLightModel;
import com.webapp.models.VersionLightModel;
import com.webapp.models.VersionModel;

@Path("/api/version")
public class ApplicationVersionCheckAction extends BusinessApiAction {

	private static final String DEVICE_TYPE = "deviceType";
	private static final String VERSION = "version";

	@GET
	@Path("/{deviceType}/{version}")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVersionUpdates(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@PathParam(DEVICE_TYPE) String deviceType, 
		@PathParam(VERSION) String version,
		@HeaderParam("x-language-code") String lang
		) throws IOException {
	//@formatter:on

		if (deviceType == null || "".equals(deviceType)) {
			return sendBussinessError(messageForKey("errorDeviceType", request));
		}

		if (version == null || "".equals(version)) {
			return sendBussinessError(messageForKey("errorVersionRequired", request));
		}

		long currentDate = System.currentTimeMillis();

		VersionModel currentVersion = VersionModel.getVersionByVersion(deviceType, version);

		if (currentVersion == null) {
			return sendBussinessError(messageForKey("errorInvalidDevice", request));
		}

		VersionModel latestVersion = VersionModel.getLatestVersion(deviceType);

		VersionLightModel versionLightModel = new VersionLightModel(latestVersion);

		if (latestVersion.getVersion().equalsIgnoreCase(currentVersion.getVersion())) {

			versionLightModel.setResultCode(ProjectConstants.NO_NEW_RELEASE);
			versionLightModel.setMessage(messageForKey("successNoNewVersion", request));

			return sendDataResponse(versionLightModel);
		}

		if (currentVersion.getRemoveSupportFrom() <= currentDate) {

			versionLightModel.setResultCode(ProjectConstants.UNSUPPORTED_RELEASE);
			versionLightModel.setMessage(messageForKey("successSupportRemoved", request));

			return sendDataResponse(versionLightModel);
		}

		if (latestVersion.isMandatory() && VersionModel.isMandatoryReleaseAvailableAfterThisRelease(deviceType, currentVersion.getReleaseDate())) {

			versionLightModel.setResultCode(ProjectConstants.MANDATORY_RELEASE);
			versionLightModel.setMessage(messageForKey("successNewVersionAvailable", request));

			return sendDataResponse(versionLightModel);
		}

		versionLightModel.setResultCode(ProjectConstants.OPTIONAL_RELEASE);
		versionLightModel.setMessage(messageForKey("successNewVersionAvailable", request));

		return sendDataResponse(versionLightModel);
	}

	@GET
	@Path("/driver/{deviceType}/{version}")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverAppVersionUpdates(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@PathParam(DEVICE_TYPE) String deviceType, 
		@PathParam(VERSION) String version,
		@HeaderParam("x-language-code") String lang
		) throws IOException {
	//@formatter:on

		if (deviceType == null || "".equals(deviceType)) {
			return sendBussinessError(messageForKey("errorDeviceType", request));
		}

		if (version == null || "".equals(version)) {
			return sendBussinessError(messageForKey("errorVersionRequired", request));
		}

		long currentDate = System.currentTimeMillis();

		DriverAppVersionModel currentDriverAppVersion = DriverAppVersionModel.getDriverAppVersionByVersion(deviceType, version);

		if (currentDriverAppVersion == null) {
			return sendBussinessError(messageForKey("errorInvalidDevice", request));
		}

		DriverAppVersionModel latestDriverAppVersion = DriverAppVersionModel.getLatestDriverAppVersion(deviceType);

		DriverVersionLightModel driverVersionLightModel = new DriverVersionLightModel(latestDriverAppVersion);

		if (latestDriverAppVersion.getVersion().equalsIgnoreCase(currentDriverAppVersion.getVersion())) {

			driverVersionLightModel.setResultCode(ProjectConstants.NO_NEW_RELEASE);
			driverVersionLightModel.setMessage(messageForKey("successNoNewVersion", request));

			return sendDataResponse(driverVersionLightModel);
		}

		if (currentDriverAppVersion.getRemoveSupportFrom() <= currentDate) {

			driverVersionLightModel.setResultCode(ProjectConstants.UNSUPPORTED_RELEASE);
			driverVersionLightModel.setMessage(messageForKey("successSupportRemoved", request));

			return sendDataResponse(driverVersionLightModel);
		}

		if (latestDriverAppVersion.isMandatory() && DriverAppVersionModel.isMandatoryReleaseAvailableAfterThisRelease(deviceType, currentDriverAppVersion.getReleaseDate())) {

			driverVersionLightModel.setResultCode(ProjectConstants.MANDATORY_RELEASE);
			driverVersionLightModel.setMessage(messageForKey("successNewVersionAvailable", request));

			return sendDataResponse(driverVersionLightModel);
		}

		driverVersionLightModel.setResultCode(ProjectConstants.OPTIONAL_RELEASE);
		driverVersionLightModel.setMessage(messageForKey("successNewVersionAvailable", request));

		return sendDataResponse(driverVersionLightModel);
	}

	@GET
	@Path("/vendor/{deviceType}/{version}")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getvendorAppVersionUpdates(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@PathParam(DEVICE_TYPE) String deviceType, 
		@PathParam(VERSION) String version,
		@HeaderParam("x-language-code") String lang
		) throws IOException {
	//@formatter:on

		if (deviceType == null || "".equals(deviceType)) {
			return sendBussinessError(messageForKey("errorDeviceType", request));
		}

		if (version == null || "".equals(version)) {
			return sendBussinessError(messageForKey("errorVersionRequired", request));
		}

		long currentDate = System.currentTimeMillis();

		VendorAppVersionModel currentVendorAppVersion = VendorAppVersionModel.getVendorAppVersionByVersion(deviceType, version);

		if (currentVendorAppVersion == null) {
			return sendBussinessError(messageForKey("errorInvalidDevice", request));
		}

		VendorAppVersionModel latestVendorAppVersion = VendorAppVersionModel.getLatestVendorAppVersion(deviceType);

		VendorVersionLightModel vendorVersionLightModel = new VendorVersionLightModel(latestVendorAppVersion);

		if (latestVendorAppVersion.getVersion().equalsIgnoreCase(currentVendorAppVersion.getVersion())) {

			vendorVersionLightModel.setResultCode(ProjectConstants.NO_NEW_RELEASE);
			vendorVersionLightModel.setMessage(messageForKey("successNoNewVersion", request));

			return sendDataResponse(vendorVersionLightModel);
		}

		if (currentVendorAppVersion.getRemoveSupportFrom() <= currentDate) {

			vendorVersionLightModel.setResultCode(ProjectConstants.UNSUPPORTED_RELEASE);
			vendorVersionLightModel.setMessage(messageForKey("successSupportRemoved", request));

			return sendDataResponse(vendorVersionLightModel);
		}

		if (latestVendorAppVersion.isMandatory() && VendorAppVersionModel.isMandatoryReleaseAvailableAfterThisRelease(deviceType, currentVendorAppVersion.getReleaseDate())) {

			vendorVersionLightModel.setResultCode(ProjectConstants.MANDATORY_RELEASE);
			vendorVersionLightModel.setMessage(messageForKey("successNewVersionAvailable", request));

			return sendDataResponse(vendorVersionLightModel);
		}

		vendorVersionLightModel.setResultCode(ProjectConstants.OPTIONAL_RELEASE);
		vendorVersionLightModel.setMessage(messageForKey("successNewVersionAvailable", request));

		return sendDataResponse(vendorVersionLightModel);
	}
}