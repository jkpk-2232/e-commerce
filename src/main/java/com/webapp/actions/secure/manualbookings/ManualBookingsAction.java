package com.webapp.actions.secure.manualbookings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.MetamorphSystemsSmsUtils;
import com.utils.CommonUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.PassangerNotificationThread;
import com.webapp.models.TourModel;

@Path("/manual-bookings")
public class ManualBookingsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response manualBookingsGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

//		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
//
//		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));
//		String multicityRegionId = MultiCityAction.setLatLngByUserId(loggedInUserModelViaSession, data);
//
//		if (loggedInUserModelViaSession.getUserRole().equalsIgnoreCase(UserRoles.OPERATOR_ROLE) || loggedInUserModelViaSession.getUserRole().equalsIgnoreCase(UserRoles.BUSINESS_OWNER_ROLE)) {
//			data.put("userType", "owner");
//		} else {
//			data.put("userType", "admin");
//		}
//
//		String tourStatus1Options = DropDownUtils.getTourStatus(ProjectConstants.All, "current");
//		data.put("tourStatus1Options", tourStatus1Options);
//
//		String tourStatus2Options = DropDownUtils.getTourStatus(ProjectConstants.All, "completed");
//		data.put("tourStatus2Options", tourStatus2Options);
//
//		String carTypeOptions = DropDownUtils.getDynamicCarOption(ProjectConstants.Second_Vehicle_ID, loginSessionMap.get(LoginUtils.USER_ID), multicityRegionId, true, true);
//		data.put("carTypeOptions", carTypeOptions);
//
//		String rideTypeOptions = DropDownUtils.getRideTypeOption(ProjectConstants.RIDE_NOW);
//		data.put("rideTypeOptions", rideTypeOptions);
//
//		data.put("rideLaterType", ProjectConstants.RIDE_LATER);
//		data.put("paymentTypeCard", ProjectConstants.CARD_ID);
//
//		String paymentTypeOptions = DropDownUtils.getPaymentTypeOption(ProjectConstants.CASH_ID);
//		data.put("paymentTypeOptions", paymentTypeOptions);
//
//		String passengerTypeOptions = DropDownUtils.getPassengerTypeOption(ProjectConstants.REGISTERED_PASSENGER_ID);
//		data.put("passengerTypeOptions", passengerTypeOptions);
//
//		data.put("passengerTypeRegistered", ProjectConstants.REGISTERED_PASSENGER_ID);
//
//		String passengerListOptions = "";
//		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
//			passengerListOptions = DropDownUtils.getVendorPassengerListOption(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, loginSessionMap.get(LoginUtils.USER_ID), UserRoles.PASSENGER_ROLE_ID, assignedRegionList);
//		} else {
//			passengerListOptions = DropDownUtils.getPassengerListOption(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
//
//		}
//		data.put("passengerListOptions", passengerListOptions);
//
//		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(adminSettings.getCountryCode());
//		data.put("countryCodeOptions", countryCodeOptions);
//
//		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();
//
//		long pickupTime = DateUtils.nowAsGmtMillisec();
//		long beforePickupTime = pickupTime + (rideLaterSettingsModel.getMinBookingTime() * 60000);
//		long afterPickupTime = pickupTime + (rideLaterSettingsModel.getMaxBookingTime() * 60000);
//
//		data.put("beforePickupTime", DateUtils.getRideLaterDateFromMilliSecond(beforePickupTime, timeZone));
//		data.put("afterPickupTime", DateUtils.getRideLaterDateFromMilliSecond(afterPickupTime, timeZone));
//
//		data.put("currencyHtmltxt", adminSettings.getCurrencySymbolHtml());
//		data.put("distanceHtmltxt", adminSettings.getDistanceType());
//
//		data.put("labelAjaxLoaderText", messageForKeyAdmin("labelAjaxLoaderText"));
//
//		data.put("labelSurgeApplied", messageForKeyAdmin("labelSurgeApplied"));
//		data.put("roleId", loginSessionMap.get(LoginUtils.ROLE_ID));
//
//		String rentalPackageTypeOptions = DropDownUtils.getRentalPackageTypeOptions(ProjectConstants.RENTAL_INTERCITY_ID, false);
//		data.put("rentalPackageTypeOptions", rentalPackageTypeOptions);
//
//		List<String> selectedTransmissionTypeIdList = new ArrayList<String>();
//		selectedTransmissionTypeIdList.add(ProjectConstants.TRANSMISSION_TYPE_NON_AUTOMATIC_ID);
//		String transmissionTypeListOptions = DropDownUtils.getTransmissionTypeListOptionsForMultiselect(selectedTransmissionTypeIdList);
//		data.put("transmissionTypeListOptions", transmissionTypeListOptions);
		
		
		 AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
	        String countryCodeOptions = DropDownUtils.getCountryCodeOptions(adminSettings.getCountryCode());
	        data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

	        String serviceTypeOptions = DropDownUtils.getServiceTypeOptions1(ProjectConstants.SERVICE_TYPE_CAR_ID);
	        data.put(FieldConstants.SERVICE_TYPE_OPTIONS, serviceTypeOptions);
	        
	        data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.MANUALBOOKING_ADDPRODUCTS_URL);

		return loadView("/secure/manual-bookings/manual-bookings.jsp");
	}

	@POST
	    @Produces(MediaType.TEXT_HTML)
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	    public Response postManualBooking(
	        @FormParam(FieldConstants.ADDRESS) String address,
	        @FormParam(FieldConstants.DESTINATION_ADDRESS) String Address,
	        @FormParam(FieldConstants.PLACE_ID) String placeId,
	        @FormParam(FieldConstants.COUNTRY_CODE) String countryCode,
	        @FormParam(FieldConstants.PHONE) String phone,
	        @FormParam(FieldConstants.SERVICE_TYPE) String serviceType,
	        @Context HttpServletRequest request, 
	        @Context HttpServletResponse response
	    ) throws ServletException, IOException, SQLException {
	        
	        preprocessRequestNewTheme(request, response);

	        if (loginSessionMap == null) {
	            return logoutUser();
	        }

	        if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_DRIVER_URL)) {
	            return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
	        }

	        // Store address and placeId for further processing or storage
	        data.put(FieldConstants.ADDRESS, address);
	        data.put(FieldConstants.PLACE_ID, placeId);
	        data.put(FieldConstants.PHONE, phone);
	        data.put(FieldConstants.SERVICE_TYPE, serviceType);

	        // Optionally, perform further actions with the address and placeId
	        // such as storing in a database or fetching additional details

	        return loadView("/secure/manual-bookings/manual-bookings.jsp");
	    }
	
	/*
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response bookCar(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@FormParam("carType") String carType,
			@FormParam("pickUpLocation") String pickUpLocation,
			@FormParam("destLocation") String destLocation,
			@FormParam("sourcePlaceLat") String sourcePlaceLat,
			@FormParam("sourcePlaceLng") String sourcePlaceLng,
			@FormParam("destinationPlaceLat") String destinationPlaceLat,
			@FormParam("destinationPlaceLng") String destinationPlaceLng,
			@FormParam("rideType") String rideType,
			@FormParam("rideLaterPickupTimeLogs") String rideLaterPickupTimeLogs,
			@FormParam("passengerType") String passengerType,
			@FormParam("passengerList") String passengerList,
			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("email") String email,
			@FormParam("phone") String phone,
			@FormParam("paymentType") String paymentType,
			@FormParam("creditCardNo") String creditCardNo,
			@FormParam("month") String month,
			@FormParam("year") String year,
			@FormParam("cvv") String cvv,
			@FormParam("stripeToken") String stripeToken,
			@FormParam("driverList") String driverList,
			@FormParam("driverListForRideLater") String driverListForRideLater,
			@FormParam("rentalBooking") boolean rentalBooking,
			@FormParam("rentalPackageId") String rentalPackageId,
			@FormParam("transmissionTypeId") String transmissionTypeId,
			@FormParam("markupFare") String markupFare,
			@DefaultValue("") @FormParam("rentalPackageCarTypeId") String rentalPackageCarTypeId,
			@FormParam("isAirportFixedFareApplied") boolean isAirportFixedFareApplied,
			@DefaultValue("") @FormParam("airportBookingType") String airportBookingType,
			@DefaultValue("0") @FormParam("airportFixedFare") String airportFixedFare
			) throws ServletException, IOException, InvalidRequestException, SQLException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();
		UserProfileModel userProfile = UserProfileModel.getAdminUserAccountDetailsById(loggedInUserId);
		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(userProfile);

		String roleId = UserModel.getRoleByUserId(loggedInUserId);
		String timeZone = TimeZoneUtils.getTimeZone();

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		if (sourcePlaceLat == null && sourcePlaceLng == null) {
			data.put("type", "Failure");
			data.put("message", messageForKeyAdmin("labelInvalidPickUpLocation"));
			return sendDataResponse(data);
		}

		if (rentalBooking) {

			RentalPackageFareModel rentalPackageFareModel = RentalPackageFareModel.getRentalPackageFareDetailsByRentalIdnCarType(rentalPackageId, carType);

			if (rentalPackageFareModel == null) {
				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("errorInvalidRentalPackage"));
				return sendDataResponse(data);
			}
		} else {

			if (destinationPlaceLat == null && destinationPlaceLng == null) {
				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("labelInvalidDropOffLocation"));
				return sendDataResponse(data);
			}
		}

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		data.put("carType", carType);
		data.put("pickUpLocation", pickUpLocation);
		data.put("destLocation", destLocation);
		data.put("sourcePlaceLat", sourcePlaceLat);
		data.put("sourcePlaceLng", sourcePlaceLng);
		data.put("destinationPlaceLat", destinationPlaceLat);
		data.put("destinationPlaceLng", destinationPlaceLng);
		data.put("rideType", rideType);
		data.put("rideLaterPickupTimeLogs", rideLaterPickupTimeLogs);
		data.put("passengerType", passengerType);
		data.put("passengerList", passengerList);
		data.put("firstName", firstName);
		data.put("lastName", lastName);
		data.put("email", email);
		data.put("phone", phone);
		data.put("paymentType", paymentType);
		data.put("creditCardNo", creditCardNo);
		data.put("month", month);
		data.put("year", year);
		data.put("cvv", cvv);
		data.put("driverList", driverList);
		data.put("driverListForRideLater", driverListForRideLater);
		data.put("roleId", roleId);
		data.put("markupFare", markupFare);

		String rideTypeOptions = DropDownUtils.getRideTypeOption(rideType);
		data.put("rideTypeOptions", rideTypeOptions);

		String paymentTypeOptions = DropDownUtils.getPaymentTypeOption(paymentType);
		data.put("paymentTypeOptions", paymentTypeOptions);

		String passengerTypeOptions = DropDownUtils.getPassengerTypeOption(passengerType);
		data.put("passengerTypeOptions", passengerTypeOptions);

		String passengerListOptions = "";
		if (roleId.equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID)) {
			passengerListOptions = DropDownUtils.getVendorPassengerListOption(passengerList, loggedInUserId, UserRoles.PASSENGER_ROLE_ID, assignedRegionList);
		} else {
			passengerListOptions = DropDownUtils.getPassengerListOption(passengerList);

		}
		data.put("passengerListOptions", passengerListOptions);

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(adminSettings.getCountryCode());
		data.put("countryCodeOptions", countryCodeOptions);

		boolean hasErrors = false;

		hasErrors = hasErrorsForEnglish(paymentType, passengerType, rideType, rentalBooking);

		if (passengerType.equalsIgnoreCase(ProjectConstants.REGISTERED_PASSENGER_ID)) {

			if ("-1".equals(passengerList)) {

				hasErrors = true;
				data.put("passengerListError", "Please select app user form list.");
			}
		}

		if (ProjectConstants.RIDE_NOW.equals(rideType)) {

			if (driverList == null || "".equals(driverList)) {
				hasErrors = true;
				data.put("driverListError", "Please select driver form list.");
			}

		} else {

			if (driverListForRideLater == null || "".equals(driverListForRideLater)) {
				hasErrors = true;
				data.put("driverListForRideLaterError", "Please select driver form list.");
			}
		}

		if (hasErrors) {

			return sendDataResponse(data);

		} else {

			if (passengerType.equalsIgnoreCase(ProjectConstants.REGISTERED_PASSENGER_ID)) {

				InvoiceModel invoiceModel = InvoiceModel.getPendingPaymentTourByPassengerId(passengerList);

				if (invoiceModel != null) {

					if (invoiceModel.isPaymentPaid() == false && invoiceModel.getFinalAmountCollected() > 0) {

						data.put("type", "Failure");
						data.put("message", messageForKeyAdmin("errorPendingPaymentOfPreviousBooking"));
						return sendDataResponse(data);
					}
				}
			}

			boolean driverBusy = false;
			String successMessage = "Car booked successfully."; // Default success msg

			String multicityCityRegionId = MultiCityAction.getMulticityRegionId(sourcePlaceLat, sourcePlaceLng);
			if (multicityCityRegionId == null) {
				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("errorNoServicesInThisCountry"));
				return sendDataResponse(data);
			}

			MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);

			if (multicityCityRegionModel == null) {

				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("errorNoServicesInThisCountry"));
				return sendDataResponse(data);
			}

			long pickupTimeForCheckDriverBusy = 0;
			long beforePickupTimeForCheckDriverBusy = 0;
			long afterPickupTimeForCheckDriverBusy = 0;

			RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

			if (rideType.equalsIgnoreCase(ProjectConstants.RIDE_LATER)) {

				if ((driverListForRideLater != null) && (!"".equals(driverListForRideLater)) && (!"-1".equals(driverListForRideLater))) {

					pickupTimeForCheckDriverBusy = DateUtils.getDateFromStringForRideLater(rideLaterPickupTimeLogs, timeZone);

					beforePickupTimeForCheckDriverBusy = pickupTimeForCheckDriverBusy - (rideLaterSettingsModel.getPassengerTourBeforeTime() * 60000);

					afterPickupTimeForCheckDriverBusy = pickupTimeForCheckDriverBusy + (rideLaterSettingsModel.getPassengerTourAfterTime() * 60000);

					boolean isDriverBusy = TourModel.checkDriverIsBusyInRideNowAndLaterTrip(driverListForRideLater, beforePickupTimeForCheckDriverBusy, afterPickupTimeForCheckDriverBusy);

					if (isDriverBusy) {

						data.put("type", "FailToDriver");
						data.put("message", messageForKeyAdmin("errorDriverIsBusyOrOffline"));
						return sendDataResponse(data);

					}

					UserModel userDetails = UserModel.getUserAccountDetailsById(driverListForRideLater);

					if ((userDetails != null) && (!userDetails.isOnDuty())) {

						data.put("type", "FailToDriver");
						data.put("message", messageForKeyAdmin("errorDriverIsBusyOrOffline"));
						return sendDataResponse(data);
					}
				}

			} else {

				if ((driverList != null) && (!driverList.equalsIgnoreCase("-1"))) {

					pickupTimeForCheckDriverBusy = DateUtils.nowAsGmtMillisec();

					beforePickupTimeForCheckDriverBusy = pickupTimeForCheckDriverBusy - (rideLaterSettingsModel.getPassengerTourBeforeTime() * 60000);

					afterPickupTimeForCheckDriverBusy = pickupTimeForCheckDriverBusy + (rideLaterSettingsModel.getPassengerTourAfterTime() * 60000);

					boolean isDriverBusy = TourModel.checkDriverIsBusyInRideNowAndLaterTrip(driverList, beforePickupTimeForCheckDriverBusy, afterPickupTimeForCheckDriverBusy);

					if (isDriverBusy) {

						data.put("type", "FailToDriver");
						data.put("message", messageForKeyAdmin("errorDriverIsBusyOrOffline"));
						return sendDataResponse(data);
					}

					UserModel userDetails = UserModel.getUserAccountDetailsById(driverList);

					if ((userDetails != null) && (!userDetails.isOnDuty())) {

						data.put("type", "FailToDriver");
						data.put("message", messageForKeyAdmin("errorDriverIsBusyOrOffline"));
						return sendDataResponse(data);
					}

				} else {

					String maxRadius = adminSettingsModel.getRadiusString();

					EstimateFareModel estimateFareModel = new EstimateFareModel();
					estimateFareModel.setsLatitude(sourcePlaceLat);
					estimateFareModel.setsLongitude(sourcePlaceLng);
					estimateFareModel.setCarTypeId(carType);

					CitySurgeModel applicableCitySurgeModel = TourUtils.getApplicableRadiusSurge(estimateFareModel, multicityCityRegionId, adminSettingsModel);

					if (!isAirportFixedFareApplied && applicableCitySurgeModel != null) {
						maxRadius = String.valueOf(applicableCitySurgeModel.getRadius() * adminSettingsModel.getDistanceUnits());
					}

//					String constant = "\"WGS 84\"";
//					String distance = "round(CAST(ST_Distance_Spheroid(car_location::geometry, ST_GeomFromText('POINT(" + sourcePlaceLng + " " + sourcePlaceLat + ")',4326), 'SPHEROID[" + constant + ",6378137,298.257223563]')As numeric),2)";
					String distance = GeoLocationUtil.getDistanceQuery(sourcePlaceLat, sourcePlaceLng, GeoLocationUtil.CAR_LOCATION);
					String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + sourcePlaceLng + " " + sourcePlaceLat + ")'),  " + maxRadius + ")";

					AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

					long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

					DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

					double minimumWalletAmount = 0.0;

					if (driverWalletSettingsModel != null) {
						minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
					}

					// Method MultiTenantUtils.getVendorIdByUserId handles all the role Id.
					String vendorId = MultiTenantUtils.getVendorIdByUserId(loggedInUserId);

					//@formatter:off
					//Driver subscription validity considered only for ride now. Driver subscription validity for ride later considered while assigning driver 
					List<DriverGeoLocationModel> carLocation = DriverGeoLocationModel.getNearByCarList(
								latAndLong, distance, multicityCityRegionModel.getMulticityCityRegionId(), 
								Arrays.asList(carType), transmissionTypeId, timeBeforeDriverIdealTimeInMillis, minimumWalletAmount,
								vendorId, DateUtils.nowAsGmtMillisec(), adminSettingsModel);
					//@formatter:on

					if (carLocation.size() <= 0 || carType == null) {

						data.put("type", "FailToDriver");
						data.put("message", messageForKeyAdmin("errorDriverIsBusyOrOffline"));
						return sendDataResponse(data);
					}
				}
			}

			boolean automatedBooking = false;

			TourModel tourModel = new TourModel();

			tourModel.setTourId(UUIDGenerator.generateUUID());
			tourModel.setCarTypeId(carType);

			if (carType.equals("5") || rentalPackageCarTypeId.equals("5")) {
				tourModel.setTransmissionTypeId(transmissionTypeId);
			}

			tourModel.setsLatitude(sourcePlaceLat);
			tourModel.setsLongitude(sourcePlaceLng);
			tourModel.setdLatitude(destinationPlaceLat);
			tourModel.setdLongitude(destinationPlaceLng);
			tourModel.setSourceAddress(pickUpLocation);
			tourModel.setDestinationAddress(destLocation);

			if (markupFare == null || "".equals(markupFare)) {
				markupFare = "0";
				tourModel.setMarkupFare(Double.parseDouble(markupFare));
			} else {
				tourModel.setMarkupFare(Double.parseDouble(markupFare));
			}

			tourModel.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
			tourModel.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());

			if (rentalBooking) {

				tourModel.setRentalBooking(true);
				tourModel.setRentalPackageId(rentalPackageId);

			} else {

				tourModel.setRentalBooking(false);
				tourModel.setRentalPackageId("-1");
			}

			if (passengerType.equalsIgnoreCase(ProjectConstants.REGISTERED_PASSENGER_ID)) {

				UserProfileModel userInfoModel = UserProfileModel.getAdminUserAccountDetailsById(passengerList);

				if (userInfoModel == null) {

					data.put("type", "Failure");
					data.put("message", "No passenger found");
					return sendDataResponse(data);
				}

				if (!rideType.equalsIgnoreCase(ProjectConstants.RIDE_LATER)) {

					TourModel currentTour = TourModel.getCurrentTourByPassangerId(passengerList);

					if (currentTour != null) {

						data.put("type", "Failure");
						data.put("message", messageForKeyAdmin("errorPreviousBooking"));
						return sendDataResponse(data);
					}
				}

				tourModel.setPassengerId(userInfoModel.getUserId());
				tourModel.setpFirstName(userInfoModel.getFirstName());
				tourModel.setpLastName(userInfoModel.getLastName());
				tourModel.setpEmail(userInfoModel.getEmail());
				tourModel.setpPhone(userInfoModel.getPhoneNo());
				tourModel.setpPhoneCode(userInfoModel.getPhoneNoCode());
				tourModel.setpPhotoUrl(userInfoModel.getPhotoUrl());

				tourModel.setBookingType(ProjectConstants.INDIVIDUAL_BOOKING);

				if (userProfile.getUserRole().equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE) || userProfile.getUserRole().equalsIgnoreCase(UserRoles.ADMIN_ROLE)) {

					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_ADMIN);

				} else if (userProfile.getUserRole().equalsIgnoreCase(UserRoles.VENDOR_ROLE)) {

					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_VENDOR);
				} else {

					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_BUSINESS_OWNER);
				}

			} else {

				tourModel.setPassengerId(loggedInUserId);
				tourModel.setpFirstName(firstName);
				tourModel.setpLastName(lastName);
				tourModel.setpEmail(email);
				tourModel.setpPhone(phone);
				tourModel.setpPhoneCode("");

				if (userProfile.getUserRole().equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE) || userProfile.getUserRole().equalsIgnoreCase(UserRoles.ADMIN_ROLE)) {

					tourModel.setBookingType(ProjectConstants.ADMIN_BOOKING);
					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_ADMIN);

				} else if (userProfile.getUserRole().equalsIgnoreCase(UserRoles.VENDOR_ROLE)) {

					tourModel.setBookingType(ProjectConstants.VENDOR_BOOKING);
					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_VENDOR);
				} else {

					tourModel.setBookingType(ProjectConstants.BUSINESS_OWNER_BOOKING);
					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_BUSINESS_OWNER);
				}
			}

			if (rideType.equalsIgnoreCase(ProjectConstants.RIDE_LATER)) {

				tourModel.setRideLater(true);
				tourModel.setTourRideLater(true);
				tourModel.setAcknowledged(false);
				tourModel.setCriticalTourRideLater(false);

				if ((driverListForRideLater != null) && (!"".equals(driverListForRideLater)) && (!"-1".equals(driverListForRideLater))) {

					TourModel tour1 = TourModel.getCurrentTourByDriverId(driverListForRideLater);

					if (tour1 == null) {

						successMessage = "Car booked successfully.";
						driverBusy = false;
						tourModel.setDriverId(driverListForRideLater);
						tourModel.setStatus(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR);
					} else {

						successMessage = "It seems driver is busy at this moment, please assign driver later.";
						driverBusy = true;
						tourModel.setDriverId("-1");
						tourModel.setStatus(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR);
					}

				} else {

					successMessage = "Car booked successfully, please assign driver later.";

					tourModel.setDriverId("-1");
					tourModel.setStatus(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR);
				}

				tourModel.setRideLaterPickupTimeLogs(rideLaterPickupTimeLogs);

				if (passengerType.equalsIgnoreCase(ProjectConstants.REGISTERED_PASSENGER_ID)) {

					ApnsDeviceModel apnsDeviceModel = ApnsDeviceModel.getDeviseByUserId(passengerList);

					if (apnsDeviceModel != null) {

						timeZone = apnsDeviceModel.getTimezone();
					}

					long pickupTime = DateUtils.getDateFromStringForRideLater(tourModel.getRideLaterPickupTimeLogs(), timeZone);

					long beforePickupTime = pickupTime - (rideLaterSettingsModel.getPassengerTourBeforeTime() * 60000);
					long afterPickupTime = pickupTime + (rideLaterSettingsModel.getPassengerTourAfterTime() * 60000);

					int count = TourModel.getRideLaterPassengerDetailsBetweenTimeSlot(passengerList, beforePickupTime, afterPickupTime);

					if (count > 0) {

						data.put("type", "Failure");
						data.put("message", messageForKeyAdmin("errorPassengerFutureRideLaterRequestExist"));
						return sendDataResponse(data);
					}

					tourModel.setRideLaterPickupTime(pickupTime);

				} else {

					long pickupTime = DateUtils.getDateFromStringForRideLater(tourModel.getRideLaterPickupTimeLogs(), timeZone);

					tourModel.setRideLaterPickupTime(pickupTime);
				}

			} else {

				successMessage = "Car booked successfully.";

				tourModel.setRideLater(false);
				tourModel.setTourRideLater(false);
				tourModel.setCriticalTourRideLater(false);

				tourModel.setAcknowledged(false);

				if (driverList.equalsIgnoreCase("-1")) {

					tourModel.setStatus(ProjectConstants.TourStatusConstants.NEW_TOUR);
					tourModel.setDriverId("-1");
					automatedBooking = true;

					tourModel.setAcknowledged(true);

				} else {

					TourModel tour = TourModel.getCurrentTourByDriverId(driverList);

					if (tour == null) {

						tourModel.setDriverId(driverList);
						automatedBooking = false;

						tourModel.setStatus(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);

					} else {

						tourModel.setDriverId("-1");
						automatedBooking = true;

						tourModel.setAcknowledged(true);

						tourModel.setStatus(ProjectConstants.TourStatusConstants.NEW_TOUR);
					}
				}
			}

			tourModel.setSourceGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + sourcePlaceLng + "  " + sourcePlaceLat + ")')");

			if (rentalBooking) {

				tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + sourcePlaceLng + "  " + sourcePlaceLat + ")')");

			} else {

				tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + destinationPlaceLng + "  " + destinationPlaceLat + ")')");
			}

			tourModel.setPaymentType(paymentType);

			tourModel.setPromoCodeId(null);
			tourModel.setPromoCodeApplied(false);

			tourModel.setLanguage(ProjectConstants.ENGLISH_ID);

			Map<String, Object> driverDetailsMap = BusinessOwnerFareCalculatorAction.getFareDetails(carType, tourModel, rideType, roleId.equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID) ? loggedInUserId : null);

			tourModel.setDistance((Double) driverDetailsMap.get("distanceInMeters"));
			tourModel.setTotal((Double) driverDetailsMap.get("totalRaw"));
			tourModel.setCharges((Double) driverDetailsMap.get("totalRaw"));

			tourModel.setSurgePriceApplied((boolean) driverDetailsMap.get("isSurgePriceApplied"));
			tourModel.setSurgePriceId((String) driverDetailsMap.get("surgePriceId"));
			tourModel.setSurgePrice((double) driverDetailsMap.get("surgePrice"));
			tourModel.setSurgeRadius((double) driverDetailsMap.get("surgeRadius"));
			tourModel.setSurgeType((String) driverDetailsMap.get("surgeType"));

			tourModel.setAirportFixedFareApplied(false);
			tourModel.setAirportBookingType("");

			Map<String, Object> airportRegionMap = new HashMap<String, Object>();
			airportRegionMap = BusinessApiAction.airPortbooking(tourModel.getsLatitude(), tourModel.getsLongitude(), tourModel.getdLatitude(), tourModel.getdLongitude(), tourModel.getCarTypeId());

			if ((boolean) airportRegionMap.get("isAirportBooking")) {

				tourModel.setSurgePriceApplied(false);
				tourModel.setSurgePriceId("-1");
				tourModel.setSurgePrice(1);
				tourModel.setAirportBooking(true);
			}

			//@formatter:off
			if (userProfile.getRoleId().equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID) 
				&& !MultiTenantUtils.validateVendorCarType(tourModel.getCarTypeId(), loggedInUserId, tourModel.getMulticityCityRegionId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {
			//@formatter:on
				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("errorInvalidCarTypeForVendorAndRegion"));
				return sendDataResponse(data);
			}

			String tourId = tourModel.createTourV2(loggedInUserId);

			if (tourId != null) {

				TourTimeModel tourTimeModel = new TourTimeModel();
				tourTimeModel.setTourTimeId(UUIDGenerator.generateUUID());
				tourTimeModel.setTourId(tourId);
				tourTimeModel.setBookingTime(DateUtils.nowAsGmtMillisec());
				tourTimeModel.createTourTime();

				data.put("rideLaterBooking", "YES");

				if (rideType.equalsIgnoreCase(ProjectConstants.RIDE_LATER)) {

					if ((driverListForRideLater != null) && (!"".equals(driverListForRideLater)) && (!"-1".equals(driverListForRideLater))) {

						if (!driverBusy) {

							TourModel tour = TourModel.getTourDetailsByTourId(tourId);

							if (tour != null) {

								assignDriverToRideLaterTour(tour, driverListForRideLater);
								data.put("rideLaterBooking", "NO");

							}
						}
					}

				} else {

					if (automatedBooking) {

						if (adminSettingsModel.getDriverProcessingVia() == ProjectConstants.DRIVER_PROCESSING_VIA_THREAD_BASED_ID) {
							new ProcessBookingRequest(tourId, tourModel.getPassengerId());
						}

					} else {

						TourUtils.assignTourDriver(tourId, driverList, ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST, driverList);

						MultiTenantUtils.changeTourFareParametersWithVendorFare(tourId, driverList);

						DriverTourStatusUtils.updateDriverTourStatus(driverList, ProjectConstants.DRIVER_HIRED_STATUS);

						DriverTourRequestUtils.createDriverTourRequest(tourId, driverList, ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);

						TourSearchDriverUtils.sendDriverNotification(driverList, ProjectConstants.TRIP_TYPE_TOUR_ID, null);

						ApnsDeviceModel driverAapnsDevice = ApnsDeviceModel.getDeviseByUserId(driverList);

						String driverPushMsg = BusinessAction.messageForKeyAdmin("driverPushMsgForManualBookingAssign", FrameworkConstants.LANGUAGE_ENGLISH);

						ApnsMessageModel driverApnsMessage = new ApnsMessageModel();
						driverApnsMessage.setMessage(driverPushMsg);
						driverApnsMessage.setMessageType("push");
						driverApnsMessage.setToUserId(driverList);
						driverApnsMessage.insertPushMessage();

						if (driverAapnsDevice != null) {

							driverAapnsDevice.sendNotification("1", "Push", driverPushMsg, ProjectConstants.NOTIFICATION_TYPE_MB_NEW_JOB, WebappPropertyUtils.getWebAppProperty("certificatePath"));
						}

						sendNotificationToPassenger(tourId);

					}

					data.put("rideLaterBooking", "NO");
				}

				data.put("tourId", tourId);
				data.put("type", "SUCCESS");
				data.put("message", successMessage);

				return sendDataResponse(data);

			} else {
				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("errorFailedSendRequest"));
				return sendDataResponse(data);
			}
		}
	}

	@Path("/book-car-bittor")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response bookCarBitoor(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@FormParam("carType") String carType,
			@FormParam("pickUpLocation") String pickUpLocation,
			@FormParam("destLocation") String destLocation,
			@FormParam("sourcePlaceLat") String sourcePlaceLat,
			@FormParam("sourcePlaceLng") String sourcePlaceLng,
			@FormParam("destinationPlaceLat") String destinationPlaceLat,
			@FormParam("destinationPlaceLng") String destinationPlaceLng,
			@FormParam("rideType") String rideType,
			@FormParam("rideLaterPickupTimeLogs") String rideLaterPickupTimeLogs,
			@FormParam("passengerType") String passengerType,
			@FormParam("passengerList") String passengerList,
			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("email") String email,
			@FormParam("phone") String phone,
			@FormParam("paymentType") String paymentType,
			@FormParam("creditCardNo") String creditCardNo,
			@FormParam("month") String month,
			@FormParam("year") String year,
			@FormParam("cvv") String cvv,
			@FormParam("stripeToken") String stripeToken,
			@FormParam("driverList") String driverList,
			@FormParam("driverListForRideLater") String driverListForRideLater,
			@FormParam("rentalBooking") boolean rentalBooking,
			@FormParam("rentalPackageId") String rentalPackageId,
			@FormParam("transmissionTypeId") String transmissionTypeId,
			@FormParam("markupFare") String markupFare,
			@DefaultValue("") @FormParam("rentalPackageCarTypeId") String rentalPackageCarTypeId,
			@FormParam("isAirportFixedFareApplied") boolean isAirportFixedFareApplied,
			@DefaultValue("") @FormParam("airportBookingType") String airportBookingType,
			@DefaultValue("0") @FormParam("airportFixedFare") String airportFixedFare
			) throws ServletException, IOException, InvalidRequestException, SQLException {
	//@formatter:on

		preprocessRequest(req, res);

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			data.put("type", "ERROR");
			data.put("message", "Not a valid vendor");
			return sendDataResponse(data);
		}

		UserProfileModel userProfile = UserProfileModel.getAdminUserAccountDetailsById(headerVendorId);
		String roleId = UserModel.getRoleByUserId(headerVendorId);
		String timeZone = WebappPropertyUtils.CLIENT_TIMEZONE;

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		if (sourcePlaceLat == null && sourcePlaceLng == null) {

			data.put("type", "Failure");
			data.put("message", messageForKeyAdmin("labelInvalidPickUpLocation", null));
			return sendDataResponse(data);
		}

		if (rentalBooking) {

			RentalPackageFareModel rentalPackageFareModel = RentalPackageFareModel.getRentalPackageFareDetailsByRentalIdnCarType(rentalPackageId, carType);

			if (rentalPackageFareModel == null) {

				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("errorInvalidRentalPackage", null));
				return sendDataResponse(data);
			}
		} else {

			if (destinationPlaceLat == null && destinationPlaceLng == null) {

				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("labelInvalidDropOffLocation", null));
				return sendDataResponse(data);
			}
		}

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		data.put("carType", carType);
		data.put("pickUpLocation", pickUpLocation);
		data.put("destLocation", destLocation);
		data.put("sourcePlaceLat", sourcePlaceLat);
		data.put("sourcePlaceLng", sourcePlaceLng);
		data.put("destinationPlaceLat", destinationPlaceLat);
		data.put("destinationPlaceLng", destinationPlaceLng);
		data.put("rideType", rideType);
		data.put("rideLaterPickupTimeLogs", rideLaterPickupTimeLogs);
		data.put("passengerType", passengerType);
		data.put("passengerList", passengerList);
		data.put("firstName", firstName);
		data.put("lastName", lastName);
		data.put("email", email);
		data.put("phone", phone);
		data.put("paymentType", paymentType);
		data.put("creditCardNo", creditCardNo);
		data.put("month", month);
		data.put("year", year);
		data.put("cvv", cvv);
		data.put("driverList", driverList);
		data.put("driverListForRideLater", driverListForRideLater);
		data.put("roleId", roleId);
		data.put("markupFare", markupFare);

		String rideTypeOptions = DropDownUtils.getRideTypeOption(rideType);
		data.put("rideTypeOptions", rideTypeOptions);

		String paymentTypeOptions = DropDownUtils.getPaymentTypeOption(paymentType);
		data.put("paymentTypeOptions", paymentTypeOptions);

		String passengerTypeOptions = DropDownUtils.getPassengerTypeOption(passengerType);
		data.put("passengerTypeOptions", passengerTypeOptions);

		String passengerListOptions = DropDownUtils.getPassengerListOption(passengerList);
		data.put("passengerListOptions", passengerListOptions);

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(adminSettings.getCountryCode());
		data.put("countryCodeOptions", countryCodeOptions);

		boolean hasErrors = false;

		hasErrors = hasErrorsForEnglish(paymentType, passengerType, rideType, rentalBooking);

		if (passengerType.equalsIgnoreCase(ProjectConstants.REGISTERED_PASSENGER_ID)) {

			if ("-1".equals(passengerList)) {

				hasErrors = true;
				data.put("passengerListError", "Please select app user form list.");
			}
		}

		if (ProjectConstants.RIDE_NOW.equals(rideType)) {

			if (driverList == null || "".equals(driverList)) {
				hasErrors = true;
				data.put("driverListError", "Please select driver form list.");
			}

		} else {

			if (driverListForRideLater == null || "".equals(driverListForRideLater)) {
				hasErrors = true;
				data.put("driverListForRideLaterError", "Please select driver form list.");
			}
		}

		if (hasErrors) {

			return sendDataResponse(data);

		} else {

			if (passengerType.equalsIgnoreCase(ProjectConstants.REGISTERED_PASSENGER_ID)) {

				InvoiceModel invoiceModel = InvoiceModel.getPendingPaymentTourByPassengerId(passengerList);

				if (invoiceModel != null) {

					if (invoiceModel.isPaymentPaid() == false && invoiceModel.getFinalAmountCollected() > 0) {

						data.put("type", "Failure");
						data.put("message", messageForKeyAdmin("errorPendingPaymentOfPreviousBooking", null));
						return sendDataResponse(data);
					}
				}
			}

			boolean driverBusy = false;
			String successMessage = "Car booked successfully."; // Default success msg

			String multicityCityRegionId = MultiCityAction.getMulticityRegionId(sourcePlaceLat, sourcePlaceLng);
			if (multicityCityRegionId == null) {
				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("errorNoServicesInThisCountry", null));
				return sendDataResponse(data);
			}

			MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);
			if (multicityCityRegionModel == null) {
				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("errorNoServicesInThisCountry", null));
				return sendDataResponse(data);
			}

			long pickupTimeForCheckDriverBusy = 0;
			long beforePickupTimeForCheckDriverBusy = 0;
			long afterPickupTimeForCheckDriverBusy = 0;

			RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

			if (rideType.equalsIgnoreCase(ProjectConstants.RIDE_LATER)) {

				if ((driverListForRideLater != null) && (!"".equals(driverListForRideLater)) && (!"-1".equals(driverListForRideLater))) {

					pickupTimeForCheckDriverBusy = DateUtils.getDateFromStringForRideLater(rideLaterPickupTimeLogs, timeZone);

					beforePickupTimeForCheckDriverBusy = pickupTimeForCheckDriverBusy - (rideLaterSettingsModel.getPassengerTourBeforeTime() * 60000);

					afterPickupTimeForCheckDriverBusy = pickupTimeForCheckDriverBusy + (rideLaterSettingsModel.getPassengerTourAfterTime() * 60000);

					boolean isDriverBusy = TourModel.checkDriverIsBusyInRideNowAndLaterTrip(driverListForRideLater, beforePickupTimeForCheckDriverBusy, afterPickupTimeForCheckDriverBusy);

					if (isDriverBusy) {

						data.put("type", "FailToDriver");
						data.put("message", messageForKeyAdmin("errorDriverIsBusyOrOffline", null));
						return sendDataResponse(data);

					}

					UserModel userDetails = UserModel.getUserAccountDetailsById(driverListForRideLater);

					if ((userDetails != null) && (!userDetails.isOnDuty())) {

						data.put("type", "FailToDriver");
						data.put("message", messageForKeyAdmin("errorDriverIsBusyOrOffline", null));
						return sendDataResponse(data);
					}
				}

			} else {

				if ((driverList != null) && (!driverList.equalsIgnoreCase("-1"))) {

					pickupTimeForCheckDriverBusy = DateUtils.nowAsGmtMillisec();

					beforePickupTimeForCheckDriverBusy = pickupTimeForCheckDriverBusy - (rideLaterSettingsModel.getPassengerTourBeforeTime() * 60000);

					afterPickupTimeForCheckDriverBusy = pickupTimeForCheckDriverBusy + (rideLaterSettingsModel.getPassengerTourAfterTime() * 60000);

					boolean isDriverBusy = TourModel.checkDriverIsBusyInRideNowAndLaterTrip(driverList, beforePickupTimeForCheckDriverBusy, afterPickupTimeForCheckDriverBusy);

					if (isDriverBusy) {

						data.put("type", "FailToDriver");
						data.put("message", messageForKeyAdmin("errorDriverIsBusyOrOffline", null));
						return sendDataResponse(data);
					}

					UserModel userDetails = UserModel.getUserAccountDetailsById(driverList);

					if ((userDetails != null) && (!userDetails.isOnDuty())) {

						data.put("type", "FailToDriver");
						data.put("message", messageForKeyAdmin("errorDriverIsBusyOrOffline", null));
						return sendDataResponse(data);
					}

				} else {

					String maxRadius = adminSettingsModel.getRadiusString();

					EstimateFareModel estimateFareModel = new EstimateFareModel();
					estimateFareModel.setsLatitude(sourcePlaceLat);
					estimateFareModel.setsLongitude(sourcePlaceLng);
					estimateFareModel.setCarTypeId(carType);

					CitySurgeModel applicableCitySurgeModel = TourUtils.getApplicableRadiusSurge(estimateFareModel, multicityCityRegionId, adminSettingsModel);

					if (!isAirportFixedFareApplied && applicableCitySurgeModel != null) {
						maxRadius = String.valueOf(applicableCitySurgeModel.getRadius() * adminSettingsModel.getDistanceUnits());
					}

//					String constant = "\"WGS 84\"";
//					String distance = "round(CAST(ST_Distance_Spheroid(car_location::geometry, ST_GeomFromText('POINT(" + sourcePlaceLng + " " + sourcePlaceLat + ")',4326), 'SPHEROID[" + constant + ",6378137,298.257223563]')As numeric),2)";
					String distance = GeoLocationUtil.getDistanceQuery(sourcePlaceLat, sourcePlaceLng, GeoLocationUtil.CAR_LOCATION);
					String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + sourcePlaceLng + " " + sourcePlaceLat + ")'),  " + maxRadius + ")";

					AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

					long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

					DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

					double minimumWalletAmount = 0.0;

					if (driverWalletSettingsModel != null) {
						minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
					}

					//@formatter:off
					//Driver subscription validity considered only for ride now. Driver subscription validity for ride later considered while assigning driver 
					List<DriverGeoLocationModel> carLocation = DriverGeoLocationModel.getNearByCarList(
								latAndLong, distance, multicityCityRegionModel.getMulticityCityRegionId(), 
								Arrays.asList(carType), transmissionTypeId, timeBeforeDriverIdealTimeInMillis, minimumWalletAmount,
								headerVendorId, DateUtils.nowAsGmtMillisec(), adminSettingsModel);
					//@formatter:on

					if (carLocation.size() <= 0 || carType == null) {

						data.put("type", "FailToDriver");
						data.put("message", messageForKeyAdmin("errorDriverIsBusyOrOffline", null));
						return sendDataResponse(data);
					}

				}
			}

			boolean automatedBooking = false;

			TourModel tourModel = new TourModel();

			tourModel.setTourId(UUIDGenerator.generateUUID());
			tourModel.setCarTypeId(carType);

			if (carType.equals("5") || rentalPackageCarTypeId.equals("5")) {
				tourModel.setTransmissionTypeId(transmissionTypeId);
			}

			tourModel.setsLatitude(sourcePlaceLat);
			tourModel.setsLongitude(sourcePlaceLng);
			tourModel.setdLatitude(destinationPlaceLat);
			tourModel.setdLongitude(destinationPlaceLng);
			tourModel.setSourceAddress(pickUpLocation);
			tourModel.setDestinationAddress(destLocation);

			if (markupFare == null || "".equals(markupFare)) {
				markupFare = "0";
				tourModel.setMarkupFare(Double.parseDouble(markupFare));
			} else {
				tourModel.setMarkupFare(Double.parseDouble(markupFare));
			}

			tourModel.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
			tourModel.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());

			if (rentalBooking) {

				tourModel.setRentalBooking(true);
				tourModel.setRentalPackageId(rentalPackageId);

			} else {

				tourModel.setRentalBooking(false);
				tourModel.setRentalPackageId("-1");
			}

			if (passengerType.equalsIgnoreCase(ProjectConstants.REGISTERED_PASSENGER_ID)) {

				UserProfileModel userInfoModel = UserProfileModel.getAdminUserAccountDetailsById(passengerList);

				if (userInfoModel == null) {

					data.put("type", "Failure");
					data.put("message", "No passenger found");
					return sendDataResponse(data);
				}

				if (!rideType.equalsIgnoreCase(ProjectConstants.RIDE_LATER)) {

					TourModel currentTour = TourModel.getCurrentTourByPassangerId(passengerList);

					if (currentTour != null) {

						data.put("type", "Failure");
						data.put("message", messageForKeyAdmin("errorPreviousBooking", null));
						return sendDataResponse(data);
					}
				}

				tourModel.setPassengerId(userInfoModel.getUserId());
				tourModel.setpFirstName(userInfoModel.getFirstName());
				tourModel.setpLastName(userInfoModel.getLastName());
				tourModel.setpEmail(userInfoModel.getEmail());
				tourModel.setpPhone(userInfoModel.getPhoneNo());
				tourModel.setpPhoneCode(userInfoModel.getPhoneNoCode());
				tourModel.setpPhotoUrl(userInfoModel.getPhotoUrl());

				tourModel.setBookingType(ProjectConstants.INDIVIDUAL_BOOKING);

				if (userProfile.getUserRole().equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE) || userProfile.getUserRole().equalsIgnoreCase(UserRoles.ADMIN_ROLE)) {

					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_ADMIN);

				} else if (userProfile.getUserRole().equalsIgnoreCase(UserRoles.VENDOR_ROLE)) {

					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_VENDOR);
				} else {

					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_BUSINESS_OWNER);
				}

			} else {

				tourModel.setPassengerId(headerVendorId);
				tourModel.setpFirstName(firstName);
				tourModel.setpLastName(lastName);
				tourModel.setpEmail(email);
				tourModel.setpPhone(phone);
				tourModel.setpPhoneCode("");

				if (userProfile.getUserRole().equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE) || userProfile.getUserRole().equalsIgnoreCase(UserRoles.ADMIN_ROLE)) {

					tourModel.setBookingType(ProjectConstants.ADMIN_BOOKING);
					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_ADMIN);

				} else if (userProfile.getUserRole().equalsIgnoreCase(UserRoles.VENDOR_ROLE)) {

					tourModel.setBookingType(ProjectConstants.VENDOR_BOOKING);
					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_VENDOR);
				} else {

					tourModel.setBookingType(ProjectConstants.BUSINESS_OWNER_BOOKING);
					tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_BUSINESS_OWNER);
				}
			}

			if (rideType.equalsIgnoreCase(ProjectConstants.RIDE_LATER)) {

				tourModel.setRideLater(true);
				tourModel.setTourRideLater(true);
				tourModel.setAcknowledged(false);
				tourModel.setCriticalTourRideLater(false);

				if ((driverListForRideLater != null) && (!"".equals(driverListForRideLater)) && (!"-1".equals(driverListForRideLater))) {

					TourModel tour1 = TourModel.getCurrentTourByDriverId(driverListForRideLater);

					if (tour1 == null) {

						successMessage = "Car booked successfully.";
						driverBusy = false;
						tourModel.setDriverId(driverListForRideLater);
						tourModel.setStatus(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR);
					} else {

						successMessage = "It seems driver is busy at this moment, please assign driver later.";
						driverBusy = true;
						tourModel.setDriverId("-1");
						tourModel.setStatus(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR);
					}

				} else {

					successMessage = "Car booked successfully, please assign driver later.";

					tourModel.setDriverId("-1");
					tourModel.setStatus(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR);
				}

				tourModel.setRideLaterPickupTimeLogs(rideLaterPickupTimeLogs);

				if (passengerType.equalsIgnoreCase(ProjectConstants.REGISTERED_PASSENGER_ID)) {

					ApnsDeviceModel apnsDeviceModel = ApnsDeviceModel.getDeviseByUserId(passengerList);
					if (apnsDeviceModel != null) {
						timeZone = apnsDeviceModel.getTimezone();
					}

					long pickupTime = DateUtils.getDateFromStringForRideLater(tourModel.getRideLaterPickupTimeLogs(), timeZone);

					long beforePickupTime = pickupTime - (rideLaterSettingsModel.getPassengerTourBeforeTime() * 60000);
					long afterPickupTime = pickupTime + (rideLaterSettingsModel.getPassengerTourAfterTime() * 60000);

					int count = TourModel.getRideLaterPassengerDetailsBetweenTimeSlot(passengerList, beforePickupTime, afterPickupTime);

					if (count > 0) {

						data.put("type", "Failure");
						data.put("message", messageForKeyAdmin("errorPassengerFutureRideLaterRequestExist", null));
						return sendDataResponse(data);
					}

					tourModel.setRideLaterPickupTime(pickupTime);

				} else {

					long pickupTime = DateUtils.getDateFromStringForRideLater(tourModel.getRideLaterPickupTimeLogs(), timeZone);

					tourModel.setRideLaterPickupTime(pickupTime);
				}

			} else {

				successMessage = "Car booked successfully.";

				tourModel.setRideLater(false);
				tourModel.setTourRideLater(false);
				tourModel.setCriticalTourRideLater(false);

				tourModel.setAcknowledged(false);

				if (driverList.equalsIgnoreCase("-1")) {

					tourModel.setStatus(ProjectConstants.TourStatusConstants.NEW_TOUR);
					tourModel.setDriverId("-1");
					automatedBooking = true;

					tourModel.setAcknowledged(true);

				} else {

					TourModel tour = TourModel.getCurrentTourByDriverId(driverList);

					if (tour == null) {

						tourModel.setDriverId(driverList);
						automatedBooking = false;

						tourModel.setStatus(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);

					} else {

						tourModel.setDriverId("-1");
						automatedBooking = true;

						tourModel.setAcknowledged(true);

						tourModel.setStatus(ProjectConstants.TourStatusConstants.NEW_TOUR);
					}
				}
			}

			tourModel.setSourceGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + sourcePlaceLng + "  " + sourcePlaceLat + ")')");

			if (rentalBooking) {

				tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + sourcePlaceLng + "  " + sourcePlaceLat + ")')");

			} else {

				tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + destinationPlaceLng + "  " + destinationPlaceLat + ")')");
			}

			tourModel.setPaymentType(paymentType);

			tourModel.setPromoCodeId(null);
			tourModel.setPromoCodeApplied(false);

			tourModel.setLanguage(ProjectConstants.ENGLISH_ID);

			Map<String, Object> driverDetailsMap = BusinessOwnerFareCalculatorAction.getFareDetails(carType, tourModel, rideType, roleId.equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID) ? headerVendorId : null);

			tourModel.setDistance((Double) driverDetailsMap.get("distanceInMeters"));
			tourModel.setTotal((Double) driverDetailsMap.get("totalRaw"));
			tourModel.setCharges((Double) driverDetailsMap.get("totalRaw"));

			tourModel.setSurgePriceApplied((boolean) driverDetailsMap.get("isSurgePriceApplied"));
			tourModel.setSurgePriceId((String) driverDetailsMap.get("surgePriceId"));
			tourModel.setSurgePrice((double) driverDetailsMap.get("surgePrice"));
			tourModel.setSurgeRadius((double) driverDetailsMap.get("surgeRadius"));
			tourModel.setSurgeType((String) driverDetailsMap.get("surgeType"));

			tourModel.setAirportFixedFareApplied(false);
			tourModel.setAirportBookingType("");

			Map<String, Object> airportRegionMap = new HashMap<String, Object>();
			airportRegionMap = BusinessApiAction.airPortbooking(tourModel.getsLatitude(), tourModel.getsLongitude(), tourModel.getdLatitude(), tourModel.getdLongitude(), tourModel.getCarTypeId());

			if ((boolean) airportRegionMap.get("isAirportBooking")) {

				tourModel.setSurgePriceApplied(false);
				tourModel.setSurgePriceId("-1");
				tourModel.setSurgePrice(1);
				tourModel.setAirportBooking(true);
			}

			//@formatter:off
			if (userProfile.getRoleId().equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID) 
				&& !MultiTenantUtils.validateVendorCarType(tourModel.getCarTypeId(), headerVendorId, tourModel.getMulticityCityRegionId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {
			//@formatter:on
				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("errorInvalidCarTypeForVendorAndRegion", null));
				return sendDataResponse(data);
			}

			String tourId = tourModel.createTourV2(headerVendorId);

			if (tourId != null) {

				TourTimeModel tourTimeModel = new TourTimeModel();
				tourTimeModel.setTourTimeId(UUIDGenerator.generateUUID());
				tourTimeModel.setTourId(tourId);
				tourTimeModel.setBookingTime(DateUtils.nowAsGmtMillisec());
				tourTimeModel.createTourTime();

				data.put("rideLaterBooking", "YES");

				if (rideType.equalsIgnoreCase(ProjectConstants.RIDE_LATER)) {

					if ((driverListForRideLater != null) && (!"".equals(driverListForRideLater)) && (!"-1".equals(driverListForRideLater))) {

						if (!driverBusy) {

							TourModel tour = TourModel.getTourDetailsByTourId(tourId);

							if (tour != null) {

								assignDriverToRideLaterTour(tour, driverListForRideLater);
								data.put("rideLaterBooking", "NO");

							}
						}
					}

				} else {

					if (automatedBooking) {

						if (adminSettingsModel.getDriverProcessingVia() == ProjectConstants.DRIVER_PROCESSING_VIA_THREAD_BASED_ID) {
							new ProcessBookingRequest(tourId, tourModel.getPassengerId());
						}

					} else {

						TourUtils.assignTourDriver(tourId, driverList, ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST, driverList);

						MultiTenantUtils.changeTourFareParametersWithVendorFare(tourId, driverList);

						DriverTourStatusUtils.updateDriverTourStatus(driverList, ProjectConstants.DRIVER_HIRED_STATUS);

						DriverTourRequestUtils.createDriverTourRequest(tourId, driverList, ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);

						TourSearchDriverUtils.sendDriverNotification(driverList, ProjectConstants.TRIP_TYPE_TOUR_ID, null);

						ApnsDeviceModel driverAapnsDevice = ApnsDeviceModel.getDeviseByUserId(driverList);

						String driverPushMsg = BusinessAction.messageForKeyAdmin("driverPushMsgForManualBookingAssign", FrameworkConstants.LANGUAGE_ENGLISH);

						ApnsMessageModel driverApnsMessage = new ApnsMessageModel();
						driverApnsMessage.setMessage(driverPushMsg);
						driverApnsMessage.setMessageType("push");
						driverApnsMessage.setToUserId(driverList);
						driverApnsMessage.insertPushMessage();

						if (driverAapnsDevice != null) {

							driverAapnsDevice.sendNotification("1", "Push", driverPushMsg, ProjectConstants.NOTIFICATION_TYPE_MB_NEW_JOB, WebappPropertyUtils.getWebAppProperty("certificatePath"));
						}

						sendNotificationToPassenger(tourId);

					}

					data.put("rideLaterBooking", "NO");
				}

				data.put("tourId", tourId);
				data.put("type", "SUCCESS");
				data.put("message", successMessage);

				return sendDataResponse(data);

			} else {
				data.put("type", "Failure");
				data.put("message", messageForKeyAdmin("errorFailedSendRequest", null));
				return sendDataResponse(data);
			}
		}
	}

	@Path("/check-promo-code")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response deleteArea(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@QueryParam("promoCodeString") String promoCodeString
			) throws ServletException, IOException {
	//@formatter:on

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(request, response);
		String loggedInUserId = userInfo.get("user_id").toString();

		Map<String, String> outputMap = new HashMap<String, String>();

		PromoCodeModel promoCode = PromoCodeModel.getPromoCodeDetailsByPromoCode(promoCodeString);

		long currentTime = DateUtils.nowAsGmtMillisec();

		if (promoCode != null) {

			if (currentTime >= promoCode.getStartDate() && currentTime <= promoCode.getEndDate()) {

				UserProfileModel adminModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInUserId);

				if (adminModel != null) {

					if ((!UserRoles.SUPER_ADMIN_ROLE_ID.equals(adminModel.getRoleId())) && (!UserRoles.ADMIN_ROLE_ID.equals(adminModel.getRoleId()))) {

						UtilizedUserPromoCodeModel utilizedUserPromoCodeModel = UtilizedUserPromoCodeModel.getUtilizedUserPromoCodeDetailsByPromoCodeIdAndUserId(loggedInUserId, promoCode.getPromoCodeId());

						if (utilizedUserPromoCodeModel != null) {

							outputMap.put("type", "ERROR");
							outputMap.put("message", messageForKeyAdmin("errorUsedPromoCode"));
							return sendDataResponse(outputMap);
						}
					}
				}

				if (promoCode.getUsageType().equalsIgnoreCase(ProjectConstants.ALL_ID)) {

					if (promoCode.getUsage().equalsIgnoreCase(ProjectConstants.UNLIMITED_ID)) {

						outputMap.put("promoCode", promoCode.getPromoCode());
						outputMap.put("promoCodeId", promoCode.getPromoCodeId());
						outputMap.put("discount", promoCode.getDiscount() + "");
						outputMap.put("mode", promoCode.getMode());
						outputMap.put("type", "SUCCESS");
						outputMap.put("message", messageForKeyAdmin("errorUsedPromoCode"));
						return sendDataResponse(outputMap);

					} else {

						if (promoCode.getUsedCount() < promoCode.getUsageCount()) {
							outputMap.put("promoCode", promoCode.getPromoCode());
							outputMap.put("promoCodeId", promoCode.getPromoCodeId());
							outputMap.put("discount", promoCode.getDiscount() + "");
							outputMap.put("mode", promoCode.getMode());
							outputMap.put("type", "SUCCESS");
							return sendDataResponse(outputMap);
						} else {

							outputMap.put("type", "ERROR");
							outputMap.put("message", messageForKeyAdmin("errorPromoCodeExpired"));
							return sendDataResponse(outputMap);
						}
					}

				} else {

					UserPromoCodeModel userPromoCodeModel = UserPromoCodeModel.getUserPromoCodeDetailsByPromoCodeIdAndUserId(loggedInUserId, promoCode.getPromoCodeId());

					if (userPromoCodeModel != null) {

						outputMap.put("promoCode", promoCode.getPromoCode());
						outputMap.put("promoCodeId", promoCode.getPromoCodeId());
						outputMap.put("discount", promoCode.getDiscount() + "");
						outputMap.put("mode", promoCode.getMode());
						outputMap.put("type", "SUCCESS");
						outputMap.put("message", messageForKeyAdmin("errorUsedPromoCode"));

						return sendDataResponse(outputMap);

					} else {

						outputMap.put("type", "ERROR");
						outputMap.put("message", messageForKeyAdmin("errorInvalidPromoCode"));

						return sendDataResponse(outputMap);
					}
				}

			} else {

				outputMap.put("type", "ERROR");
				outputMap.put("message", messageForKeyAdmin("errorPromoCodeExpired"));

				return sendDataResponse(outputMap);
			}

		} else {

			outputMap.put("type", "ERROR");
			outputMap.put("message", messageForKeyAdmin("errorInvalidPromoCode"));

			return sendDataResponse(outputMap);
		}
	}

	@Path("/rental-packages")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadRentalPackages(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@QueryParam("latitude") String latitude,
			@QueryParam("longitude") String longitude,
			@QueryParam("rentalPackageType") String rentalPackageType
			) throws ServletException, IOException {
	//@formatter:on

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(request, response);
		String loggedInUserId = userInfo.get("user_id").toString();
		UserProfileModel userProfile = UserProfileModel.getAdminUserAccountDetailsById(loggedInUserId);

		Map<String, Object> outputMap = new HashMap<String, Object>();

		if (latitude == null || "".equals(latitude)) {

			outputMap.put("type", "Failure");
			outputMap.put("message", "Latitude required.");
			return sendDataResponse(outputMap);
		}

		if (longitude == null || "".equals(longitude)) {

			outputMap.put("type", "Failure");
			outputMap.put("message", "Longitude required.");
			return sendDataResponse(outputMap);
		}

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(latitude, longitude);

		if (multicityCityRegionId == null || "".equals(multicityCityRegionId)) {

			outputMap.put("type", "Failure");
			outputMap.put("message", messageForKeyAdmin("errorNoServicesInThisCountry"));
			return sendDataResponse(outputMap);
		}

		if ((rentalPackageType == null) || ("".equals(rentalPackageType)) || ((!ProjectConstants.RENTAL_INTERCITY_ID.equals(rentalPackageType)) && (!ProjectConstants.RENTAL_OUTSTATION_ID.equals(rentalPackageType)))) {

			outputMap.put("type", "Failure");
			outputMap.put("message", messageForKeyAdmin("errorInvalidRentalPackageType"));
			return sendDataResponse(outputMap);
		}

		List<Map<String, Object>> rentalPackagesListMap = new ArrayList<Map<String, Object>>();
		List<RentalPackageModel> rentalPackageList = new ArrayList<RentalPackageModel>();

		if (userProfile.getRoleId().equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID)) {
			rentalPackageList = RentalPackageModel.getRentalPackageListPagination(0, 10000, multicityCityRegionId, rentalPackageType, loggedInUserId);
		} else {
			rentalPackageList = RentalPackageModel.getRentalPackageListPagination(0, 10000, multicityCityRegionId, rentalPackageType, WebappPropertyUtils.DEFAULT_VENDOR_ID);
		}

		if (rentalPackageList.size() > 0) {

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			List<CarTypeModel> carTypeModelList = CarTypeModel.getAllCars();

			for (RentalPackageModel rentalPackageModel : rentalPackageList) {

				Map<String, Object> innerOutPutMap = new HashMap<String, Object>();

				innerOutPutMap.put("rentalPackageId", rentalPackageModel.getRentalPackageId());

				if (rentalPackageModel.getPackageTime() == 1) {

					innerOutPutMap.put("packageTime", rentalPackageModel.getPackageTime() + " Hour");

				} else {

					innerOutPutMap.put("packageTime", rentalPackageModel.getPackageTime() + " Hours");
				}

				innerOutPutMap.put("packageDistance", df_new.format(((rentalPackageModel.getPackageDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());
				innerOutPutMap.put("createdDate", rentalPackageModel.getCreatedAt());

				List<Map<String, Object>> carWiseFareDetailsMap = new ArrayList<Map<String, Object>>();

				List<RentalPackageFareModel> rentalPackageFareList = RentalPackageFareModel.getRentalPackageFareListByRentalPackageId(rentalPackageModel.getRentalPackageId());

				for (RentalPackageFareModel rentalPackageFareModel : rentalPackageFareList) {

					Map<String, Object> carFareOutPutMap = new HashMap<String, Object>();

					carFareOutPutMap.put("rentalPackageFareId", rentalPackageFareModel.getRentalPackageFareId());
					carFareOutPutMap.put("carTypeId", rentalPackageFareModel.getCarTypeId());
					carFareOutPutMap.put("baseFare", df_new.format(rentalPackageFareModel.getBaseFare()));
					carFareOutPutMap.put("additionalPerKmFare", df_new.format(rentalPackageFareModel.getPerKmFare()));
					carFareOutPutMap.put("additionalPerMinuteFare", df_new.format(rentalPackageFareModel.getPerMinuteFare()));

					if (rentalPackageModel.getPackageTime() == 1) {

						carFareOutPutMap.put("packageTime", rentalPackageModel.getPackageTime() + " Hour");

					} else {

						carFareOutPutMap.put("packageTime", rentalPackageModel.getPackageTime() + " Hours");
					}

					carFareOutPutMap.put("packageDistance", df_new.format(((rentalPackageModel.getPackageDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());

					for (CarTypeModel carTypeModel : carTypeModelList) {

						if (rentalPackageFareModel.getCarTypeId().equals(carTypeModel.getCarTypeId())) {

							carFareOutPutMap.put("carTypeName", carTypeModel.getCarType());
						}
					}

					carWiseFareDetailsMap.add(carFareOutPutMap);
				}

				innerOutPutMap.put("carWiseFareDetails", carWiseFareDetailsMap);

				rentalPackagesListMap.add(innerOutPutMap);
			}

			outputMap.put("type", "SUCCESS");
			outputMap.put("rentalPackagesList", rentalPackagesListMap);

			return sendDataResponse(outputMap);

		} else {

			outputMap.put("type", "Failure");
			outputMap.put("message", "No package available for selected location.");
			return sendDataResponse(outputMap);
		}
	}

	@Path("/driver-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadDriverList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam("carTypeId") String carTypeId,
		@QueryParam("transmissionTypeId") String transmissionTypeId,
		@DefaultValue("") @QueryParam("rentalPackageCarTypeId") String rentalPackageCarTypeId,
		@QueryParam("sourcePlaceLat") String sourcePlaceLat,
		@QueryParam("sourcePlaceLng") String sourcePlaceLng,
		@QueryParam("destinationPlaceLat") String destinationPlaceLat,
		@QueryParam("destinationPlaceLng") String destinationPlaceLng,
		@QueryParam("rideType") String rideType,
		@QueryParam("rideLaterPickupTimeLogs") String rideLaterPickupTimeLogs
		) throws ServletException, IOException {
	//@formatter:on

		String timeZone = TimeZoneUtils.getTimeZone();

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(request, response);
		String userId = userInfo.get("user_id").toString();
		String role = UserModel.getRoleByUserId(userId);
		Map<String, Object> outputMap = new HashMap<String, Object>();

		String longi = "";
		String lati = "";

		if (sourcePlaceLat == null && sourcePlaceLng == null) {
			longi = ProjectConstants.BASE_LONGITUDE;
			lati = ProjectConstants.BASE_LATITUDE;
		} else {
			longi = sourcePlaceLng;
			lati = sourcePlaceLat;
		}

		String tempCarTypeId = "";

		if (carTypeId.equals("5") || rentalPackageCarTypeId.equals("5")) {
			tempCarTypeId = "5";
		} else {
			transmissionTypeId = null;
		}

		List<Map<String, Object>> driverListMap = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> driverListMapForRideLater = new ArrayList<Map<String, Object>>();

		List<DriverGeoLocationModel> carLocationList = new ArrayList<DriverGeoLocationModel>();

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(sourcePlaceLat, sourcePlaceLng);

		if (multicityCityRegionId == null) {

		} else {

			MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

			double minimumWalletAmount = 0.0;

			if (driverWalletSettingsModel != null) {
				minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
			}

			if (multicityCityRegionModel != null) {

				String maxRadius = adminSettingsModel.getRadiusString();

				EstimateFareModel estimateFareModel = new EstimateFareModel();
				estimateFareModel.setsLatitude(lati);
				estimateFareModel.setsLongitude(longi);
				estimateFareModel.setdLatitude(destinationPlaceLat);
				estimateFareModel.setdLongitude(destinationPlaceLng);
				estimateFareModel.setCarTypeId(carTypeId);

				CitySurgeModel applicableCitySurgeModel = TourUtils.getApplicableRadiusSurge(estimateFareModel, multicityCityRegionId, adminSettingsModel);

				if (applicableCitySurgeModel != null) {
					maxRadius = String.valueOf(applicableCitySurgeModel.getRadius() * adminSettingsModel.getDistanceUnits());
				}

				if (!estimateFareModel.getCarTypeId().equals("5")) {
					Map<String, Object> ac = TourUtils.checkBookingForAirportPickupOrDrop(estimateFareModel.getsLatitude(), estimateFareModel.getsLongitude(), estimateFareModel.getdLatitude(), estimateFareModel.getdLongitude());
					if ((boolean) ac.get("isAirportPickUp") || (boolean) ac.get("isAirportDrop")) {
						AirportRegionModel airportRegionModel = (AirportRegionModel) ac.get("airport");
						Map<String, Double> distanceMatrix = CommonUtils.getDistanceMatrixInbetweenLocations(estimateFareModel.getsLatitude(), estimateFareModel.getsLongitude(), estimateFareModel.getdLatitude(), estimateFareModel.getdLongitude());
						double distanceInMeters = distanceMatrix.get("distanceInMeters");
						if (distanceInMeters <= (airportRegionModel.getAirportDistance() * 1000)) {
							maxRadius = adminSettingsModel.getRadiusString();
						}

					}
				}

//				String constant = "\"WGS 84\"";
//				String distance = "round(CAST(ST_Distance_Spheroid(car_location::geometry, ST_GeomFromText('POINT(" + longi + " " + lati + ")',4326), 'SPHEROID[" + constant + ",6378137,298.257223563]')As numeric),2)";
				String distance = GeoLocationUtil.getDistanceQuery(lati, longi, GeoLocationUtil.CAR_LOCATION);
				String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + longi + " " + lati + ")'),  " + maxRadius + ")";

				AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

				long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

				String newCarTypeId = carTypeId;

				if (carTypeId == null || "".equals(carTypeId) || (ProjectConstants.Rental_Type_ID.equals(carTypeId))) {
					newCarTypeId = null;
				}

				if (UserRoles.VENDOR_ROLE_ID.equals(role)) {
					carLocationList = DriverGeoLocationModel.getNearByCarDriverList(latAndLong, distance, multicityCityRegionModel.getMulticityCityRegionId(), newCarTypeId, transmissionTypeId, timeBeforeDriverIdealTimeInMillis, userId, tempCarTypeId,
								minimumWalletAmount, adminSettingsModel);
				} else {
					carLocationList = DriverGeoLocationModel.getNearByCarDriverList(latAndLong, distance, multicityCityRegionModel.getMulticityCityRegionId(), newCarTypeId, transmissionTypeId, timeBeforeDriverIdealTimeInMillis, null, tempCarTypeId,
								minimumWalletAmount, adminSettingsModel);
				}

				if (carLocationList.size() > 0) {

					outputMap.put("type", "SUCCESS");

					for (DriverGeoLocationModel driverGeoLocationModel : carLocationList) {

						Map<String, Object> innerMap = new HashMap<String, Object>();

						innerMap.put("driverId", driverGeoLocationModel.getDriverId());

						DriverInfoModel driverInfo = DriverInfoModel.getDriverAccountDetailsById(driverGeoLocationModel.getDriverId());

						innerMap.put("driverName", driverInfo.getFirstName() + " " + driverInfo.getLastName());
						innerMap.put("driverEmail", driverInfo.getEmail());

						driverListMap.add(innerMap);
					}

				}

				if (rideType.equalsIgnoreCase(ProjectConstants.RIDE_LATER)) {

					outputMap.put("type", "SUCCESS");

					long pickupTimeInMillis = DateUtils.getDateFromStringForRideLater(rideLaterPickupTimeLogs, timeZone);

					RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

					Map<String, Object> inputMap = new HashMap<String, Object>();
					inputMap.put("carTypeId", newCarTypeId);
					inputMap.put("latAndLong", latAndLong);
					inputMap.put("distance", distance);
					inputMap.put("driverTripBeforeTime", (pickupTimeInMillis - (rideLaterSettingsModel.getDriverAllocateBeforeTime() * ProjectConstants.RIDE_LATER_CRON_JOB_TIME_1MIN)));
					inputMap.put("driverTripAfterTime", (pickupTimeInMillis + (rideLaterSettingsModel.getDriverAllocateAfterTime() * ProjectConstants.RIDE_LATER_CRON_JOB_TIME_1MIN)));
					inputMap.put("tourPickupTime", pickupTimeInMillis);
					inputMap.put("transmissionTypeIdList", DriverGeoLocationModel.getDriveTransmissionListFromType(transmissionTypeId, tempCarTypeId));
					inputMap.put("minimumWalletAmount", minimumWalletAmount);

					List<UserProfileModel> userProfileModelList = new ArrayList<UserProfileModel>();

					VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);

					String vendorId = MultiTenantUtils.getVendorIdByUserId(userId);
					if (UserRoles.VENDOR_ROLE_ID.equals(role)) {
						MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, userId, DateUtils.nowAsGmtMillisec());
						inputMap.put("vendorId", vendorId);
						userProfileModelList = UserProfileModel.getVendorDriverListForManualBookingRideLater(inputMap);
					} else {
						MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, userId, DateUtils.nowAsGmtMillisec());
						inputMap.put("vendorId", vendorId);
						userProfileModelList = UserProfileModel.getDriverListForManualBookingRideLater(inputMap);
					}

					for (UserProfileModel userProfileModel : userProfileModelList) {

						Map<String, Object> innerMapForRideLater = new HashMap<String, Object>();

						innerMapForRideLater.put("driverId", userProfileModel.getUserId());
						innerMapForRideLater.put("driverName", userProfileModel.getFirstName() + " " + userProfileModel.getLastName());
						innerMapForRideLater.put("driverEmail", userProfileModel.getEmail());

						driverListMapForRideLater.add(innerMapForRideLater);
					}
				}
			}
		}

		outputMap.put("driverList", driverListMap);
		outputMap.put("driverListForRideLater", driverListMapForRideLater);

		return sendDataResponse(outputMap);
	}

	@Path("/vendor-car-type")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadDynamicCarList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam("carTypeId") String carTypeId,
		@QueryParam("sourcePlaceLat") String sourcePlaceLat,
		@QueryParam("sourcePlaceLng") String sourcePlaceLng
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(request, response);
		String loggedInUserId = userInfo.get("user_id").toString();
		Map<String, String> outputMap = new HashMap<String, String>();

		String multicityRegionId = MultiCityAction.getMulticityRegionId(sourcePlaceLat, sourcePlaceLng);
		if (multicityRegionId == null) {
			outputMap.put("type", "ERROR");
			return sendDataResponse(outputMap);
		}

		outputMap.put("type", "SUCCESS");

//		if (userProfileModel.getRoleId().equalsIgnoreCase(ProjectConstants.VENDOR_ROLE_ID)) {
		String carTypeOptions = DropDownUtils.getDynamicCarOption(ProjectConstants.Second_Vehicle_ID, loggedInUserId, multicityRegionId, true, true);
		outputMap.put("carTypeListOptions", carTypeOptions);
//		} else {
//			String carTypeOptions = getCarModelOption(ProjectConstants.Second_Vehicle_ID, true, true);
//			outputMap.put("carTypeListOptions", carTypeOptions);
//		}

		return sendDataResponse(outputMap);
	}

	private void assignDriverToRideLaterTour(TourModel tourModel, String driverId) {

		DriverTourRequestUtils.createDriverTourRequest(tourModel.getTourId(), driverId);

		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(driverId);

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		Map<String, Object> outPutMap = new HashMap<String, Object>();
		outPutMap.put("tourId", tourModel.getTourId());
		outPutMap.put("userTourId", tourModel.getUserTourId());
		outPutMap.put("passengerId", tourModel.getPassengerId());
		outPutMap.put("driverId", tourModel.getDriverId());
		outPutMap.put("dateTime", String.valueOf(tourModel.getCreatedAt()));
		outPutMap.put("firstName", tourModel.getpFirstName());
		outPutMap.put("lastName", tourModel.getpLastName());
		outPutMap.put("email", tourModel.getpEmail());
		outPutMap.put("phone", tourModel.getpPhone());
		outPutMap.put("phoneCode", tourModel.getpPhoneCode());
		outPutMap.put("photoUrl", tourModel.getpPhotoUrl());
		outPutMap.put("sourceLatitude", tourModel.getsLatitude());
		outPutMap.put("sourceLongitude", tourModel.getsLongitude());
		outPutMap.put("destinationLatitude", tourModel.getdLatitude());
		outPutMap.put("destinationLongitude", tourModel.getdLongitude());
		outPutMap.put("sourceAddress", tourModel.getSourceAddress());
		outPutMap.put("destinationAddress", tourModel.getDestinationAddress());
		outPutMap.put("status", tourModel.getStatus());

		outPutMap.put("jobExpireTime", rideLaterSettingsModel.getDriverJobRequestTime());

		outPutMap.put("rideLaterPickupTime", tourModel.getRideLaterPickupTime());
		outPutMap.put("isRideLater", tourModel.isRideLater());

		outPutMap.put("isAcknowledged", tourModel.isAcknowledged());

		JSONObject jsonMessage = new JSONObject(outPutMap);

		if (apnsDevice != null) {

			String messge = "TOUR`1`" + apnsDevice.getDeviceToken() + ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.RLNJ_SOCKET + jsonMessage.toString();

			WebSocketClient.sendDriverNotification(messge, driverId, apnsDevice.getApiSessionKey());
		}

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

		String message = BusinessAction.messageForKeyAdmin("successRideLaterNewJobMessage", adminSmsSendingModel.getLanguage());

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(message);
		apnsMessage.setMessageType("socket");
		apnsMessage.setToUserId(driverId);
		apnsMessage.insertPushMessage();
	}

	public boolean hasErrorsForEnglish(String paymentType, String passengerType, String rideType, boolean rentalBooking) {

		boolean hasErrors = false;
		Validator validator = new Validator();

		validator.addValidationMapping("pickUpLocation", "Pickup Location", new RequiredValidationRule());

		if (!rentalBooking) {

			validator.addValidationMapping("destLocation", "Destination Location", new RequiredValidationRule());
		}

		if (rideType.equalsIgnoreCase(ProjectConstants.RIDE_LATER)) {
			validator.addValidationMapping("rideLaterPickupTimeLogs", "Booking date and time", new RequiredValidationRule());
		}

		if (passengerType.equalsIgnoreCase(ProjectConstants.REGISTERED_PASSENGER_ID)) {

		} else {

			validator.addValidationMapping("firstName", messageForKeyAdmin("labelFirstName"), new RequiredValidationRule());
			validator.addValidationMapping("firstName", messageForKeyAdmin("labelFirstName"), new MaxLengthValidationRule(20));
			validator.addValidationMapping("lastName", messageForKeyAdmin("labelLastName"), new RequiredValidationRule());
			validator.addValidationMapping("lastName", messageForKeyAdmin("labelLastName"), new MaxLengthValidationRule(20));
			validator.addValidationMapping("email", messageForKeyAdmin("labelEmail"), new RequiredValidationRule());
			validator.addValidationMapping("email", messageForKeyAdmin("labelEmail"), new EmailValidationRuleEmptyEmail());
			validator.addValidationMapping("email", messageForKeyAdmin("labelEmail"), new MaxLengthValidationRuleEmptyField(50));
			validator.addValidationMapping("phone", messageForKeyAdmin("labelMobile"), new RequiredValidationRule());
			validator.addValidationMapping("phone", messageForKeyAdmin("labelMobile"), new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));
			validator.addValidationMapping("phone", messageForKeyAdmin("labelMobile"), new NumericValidationRule());
		}

		if (paymentType.equalsIgnoreCase(ProjectConstants.CARD_ID)) {
			validator.addValidationMapping("creditCardNo", "Credit Card No.", new RequiredValidationRule());
			validator.addValidationMapping("creditCardNo", "Credit Card No.", new NumericValidationRule());
			validator.addValidationMapping("creditCardNo", "Credit Card No.", new MinMaxLengthValidationRule(13, 19));
			validator.addValidationMapping("month", "Month", new RequiredValidationRule());
			validator.addValidationMapping("year", "Year", new RequiredValidationRule());
			validator.addValidationMapping("cvv", "CVV", new RequiredValidationRule());
			validator.addValidationMapping("cvv", "CVV", new NumericValidationRule());
			validator.addValidationMapping("cvv", "CVV", new MinMaxLengthValidationRule(3, 4));
		}

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}
*/
	public static void sendNotificationToPassenger(String tourId) {

		TourModel tourDetails = TourModel.getTourDetailsByTourId(tourId);
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		DriverGeoLocationModel driverLocation = DriverGeoLocationModel.getCurrentDriverPosition(tourDetails.getDriverId());

		double distance = CommonUtils.distance(Double.parseDouble(tourDetails.getsLatitude()), Double.parseDouble(tourDetails.getsLongitude()), Double.parseDouble(driverLocation.getLatitude()), Double.parseDouble(driverLocation.getLongitude()), 'K');
		double min = CommonUtils.calculateETA(distance, adminSettingsModel.getDistanceUnits());

		String message = String.format(BusinessAction.messageForKeyAdmin("driverAction_1", tourDetails.getLanguage()), df.format(min));
		String templateId = ProjectConstants.SMSConstants.SMS_ARRIVING_TEMPLATE_ID;

		if (df.format(min).equals("0")) {
			message = BusinessAction.messageForKeyAdmin("driverAction_3", tourDetails.getLanguage());
			templateId = ProjectConstants.SMSConstants.SMS_AWAY_TEMPLATE_ID;
		}

		if (tourDetails.getBookingType().equals(ProjectConstants.INDIVIDUAL_BOOKING)) {

			ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetails.getPassengerId());

			ApnsMessageModel apnsMessage = new ApnsMessageModel();
			apnsMessage.setMessage(message);
			apnsMessage.setMessageType("push");
			apnsMessage.setToUserId(tourDetails.getPassengerId());
			apnsMessage.insertPushMessage();

			AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

			if (adminSmsSendingModel.ispAcceptByDriver()) {
				MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourDetails.getpPhoneCode() + tourDetails.getpPhone().trim(), templateId);
			}

			if (apnsDevice != null) {

				if (tourDetails.getBookingType().equalsIgnoreCase(ProjectConstants.INDIVIDUAL_BOOKING)) {
					new PassangerNotificationThread(apnsDevice.getDeviceToken(), tourId, apnsDevice.getApiSessionKey());
				}

				apnsDevice.sendNotification("1", "Push", message, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, WebappPropertyUtils.getWebAppProperty("certificatePath"));
			}
		}
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs/manual-bookings/manual-bookings.js");
		requiredJS.add("new-ui/js/moment-with-locales.min.js");
		requiredJS.add("new-ui/js/bootstrap-material-datetimepicker.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}

	@Override
	protected String[] requiredCss() {

		List<String> requiredCSS = new ArrayList<String>();

		requiredCSS.add("new-ui/css/bootstrap-material-datetimepicker.css");

		return requiredCSS.toArray(new String[requiredCSS.size()]);
	}
}