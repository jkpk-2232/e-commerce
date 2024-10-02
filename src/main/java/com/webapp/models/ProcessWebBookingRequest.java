//package com.webapp.models;
//
//import com.utils.CommonUtils;
//import com.webapp.ProjectConstants;
//
//public class ProcessWebBookingRequest extends Thread {
//
//	private String tourId;
//
//	private String driverId;
//
//	public ProcessWebBookingRequest(String tourId, String driverId) {
//
//		this.tourId = tourId;
//		this.driverId = driverId;
//		this.start();
//	}
//
//	@Override
//	public void run() {
//
//		// assign tour driver
//		TourModel tourModel = new TourModel();
//		tourModel.setTourId(tourId);
//		tourModel.setDriverId(driverId);
//		tourModel.setStatus(ProjectConstants.ASSIGNED_TOUR);
//		tourModel.assignTourDriver(driverId);
//
//		// hire driver
//		DriverTourStatusModel tourStatus = new DriverTourStatusModel();
//		tourStatus.setDriverId(driverId);
//		tourStatus.setStatus(ProjectConstants.DRIVER_HIRED_STATUS);
//		tourStatus.updateDriverTourStatus();
//
//		// assign tour request to driver
//		DriverTourRequestModel tourRequest = new DriverTourRequestModel();
//		tourRequest.setDriverId(driverId);
//		tourRequest.setTourId(tourId);
//		tourRequest.createDriverTourRequest();
//		CommonUtils.sendDriverNotification(driverId);
//
//		try {
//
//			sleep(26000);
//
//			TourModel tour = TourModel.getTourDetailsByTourId(tourId);
//			if (tour.getStatus().equals(ProjectConstants.ASSIGNED_TOUR)) {
//
//				if (tour.getDriverId().equals(driverId)) {
//
//					// free driver
//					tourStatus = new DriverTourStatusModel();
//					tourStatus.setDriverId(driverId);
//					tourStatus.setStatus(ProjectConstants.DRIVER_FREE_STATUS);
//					tourStatus.updateDriverTourStatus();
//
//					tourModel = new TourModel();
//					tourModel.setTourId(this.tourId);
//					tourModel.setStatus(ProjectConstants.PENDING_REQUEST);
//					tourModel.setDriverId("-1");
//					tourModel.assignTourDriver("-1");
//
//				}else{
//					
//					tourModel = new TourModel();
//					tourModel.setTourId(this.tourId);
//					tourModel.setStatus(ProjectConstants.PENDING_REQUEST);
//					tourModel.setDriverId("-1");
//					tourModel.assignTourDriver("-1");
//					
//				}
//
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//	}
//
//}
