package com.webapp.models;

import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.utils.myhub.DriverTourRequestUtils;
import com.utils.myhub.TourSearchDriverUtils;
import com.webapp.ProjectConstants;

public class ProcessBookingRequest extends Thread {

	private static Logger logger = Logger.getLogger(ProcessBookingRequest.class);

	private String tourId;
	private String passangerId;

	public ProcessBookingRequest(String tourId, String passangerId) {
		this.tourId = tourId;
		this.passangerId = passangerId;
		this.start();
	}

	@Override
	public void run() {

		String driverId = "";

		boolean tripAccepted = false;

		TourModel tour = TourModel.getTourDetailsByTourId(this.tourId);
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		for (int i = 0; i < adminSettingsModel.getNoOfCars(); i++) {

			long requestTime = DateUtils.nowAsGmtMillisec();

			driverId = TourSearchDriverUtils.assignDriver(tour, adminSettingsModel);

			logger.info("\n\n\n\t1\t" + driverId);

			if (driverId != null) {

				UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(driverId);

				logger.info("\n\n\n\t2\t" + userProfileModel.getFirstName() + " " + userProfileModel.getLastName());

				TourSearchDriverUtils.sendDriverNotification(driverId, ProjectConstants.TRIP_TYPE_TOUR_ID, null);

				while (true) {

					try {
						sleep(3000);
					} catch (InterruptedException e) {

					}

					tour = TourModel.getTourDetailsByTourId(this.tourId);

					// if trip is accepted by the driver then break both while and for loop
					//@formatter:off
					if (tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST) 
							|| tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR) 
							|| tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.STARTED_TOUR)
							|| tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ENDED_TOUR)) {
					//@formatter:on
						tripAccepted = true;
						break;
					}

					// if trip is rejected by driver break for loop and send request to another
					// driver
					if (tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

						DriverTourRequestModel driverTourRequestModel = DriverTourRequestModel.getTourRequestByDriverIdAndTourId(driverId, tour.getTourId());

						if (driverTourRequestModel != null) {

							if (driverTourRequestModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.REJECTED_TOUR)) {
								break;
							}
						}
					}

					long currentTime = DateUtils.nowAsGmtMillisec();

					if ((currentTime - requestTime) > ProjectConstants.ALGORITHM_EXPIRE_TIME) {

						DriverTourRequestUtils.updateDriverTourRequest(this.tourId, tour.getDriverId(), ProjectConstants.TourStatusConstants.IGNORED_TOUR);

						// free current driver
						if (tour.getDriverId() != null) {
							TourSearchDriverUtils.freeDriver(tour.getDriverId());
						}

						break;
					}
				}

				if (tripAccepted) {
					break;
				}

			} else {

				TourSearchDriverUtils.updateTourStatusAsPending(this.tourId, this.passangerId);
				break;
			}
		}

		// If trip not expired and trip has status ASSIGNED_TOUR
		TourModel currentTour = TourModel.getTourDetailsByTourId(this.tourId);

		if (currentTour != null) {

			if (currentTour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

				// free current driver
				if ((currentTour.getDriverId() != null) && (!"-1".equals(currentTour.getDriverId()))) {
					TourSearchDriverUtils.freeDriver(currentTour.getDriverId());
				}

				// For expire
				TourSearchDriverUtils.updateTourStatusAsPending(this.tourId, this.passangerId);
			}
		}
	}
}