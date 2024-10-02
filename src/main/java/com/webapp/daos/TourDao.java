package com.webapp.daos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.TourModel;

public interface TourDao {

	int createTour(TourModel tourModel);

	int createTourV2(TourModel tourModel);

	int createTourRideLater(TourModel tourModel);

	void createCourierOrder(TourModel tourModel);
 
	List<TourModel> getPassengerToursBySpecificDate(@Param("userId") String userId, @Param("startDate") long startDate, @Param("endDate") long endDate);

	TourModel getCurrentTourByDriverId(@Param("userId") String userId);
	
	TourModel getCurrentTourByDriverIdForRideNowTakeBooking(@Param("userId") String userId);

	TourModel getAdminCurrentTourByDriverId(@Param("userId") String userId);

	TourModel getCurrentTourByPassangerId(@Param("userId") String userId);

	List<TourModel> getDriverToursBySpecificDate(Map<String, Object> inputMap);

	TourModel getTourDetailsByTourId(@Param("tourId") String tourId);

	int updateTourStatusByTourId(TourModel tourModel);

	int updateDriverPercentageByTourId(TourModel tourModel);

	int updateTourAddress(TourModel tourModel);

	int assignTourDriver(TourModel tourModel);

	int updateTourStatusByPassenger(TourModel tourModel);

	int getDriverTourListBySearchByDriverIdCount(Map<String, Object> inputMap);

	List<TourModel> getDriverTourListBySearchByDriverId(Map<String, Object> inputMap);

	List<TourModel> getPassengerTourListBySearchByPassengerId(Map<String, Object> inputMap);

	int updateCharges(TourModel tourModel);

	int getTotalBookingsCountByTime(Map<String, Object> inputMap);

	int getAllTourListCount(Map<String, Object> inputMap);

	List<TourModel> getAllTourListBySearch(Map<String, Object> inputMap);

	int getAdminTourListCount(Map<String, Object> inputMap);

	List<TourModel> getAdminTourListBySearch(Map<String, Object> inputMap);

	List<TourModel> getDriverReportTourListByDate(@Param("tourStartDate") long tourStartDate, @Param("tourEndDate") long tourEndDate);

	List<TourModel> getDriverReportTourListByDateByUserIds(@Param("tourStartDate") long tourStartDate, @Param("tourEndDate") long tourEndDate, @Param("userIds") ArrayList<String> userIds);

	int getDriverTourCountByDriverId(Map<String, Object> inputMap);

	int getPassengerTourCountByPassengerId(Map<String, Object> inputMap);

	int updateChargesAndDriverAmount(TourModel tourModel);

	int updatePromoCodeStatus(TourModel tourModel);

	TourModel getCurrentEndedTourByPassangerId(@Param("userId") String userId, @Param("status") String status);

	List<TourModel> getToursByStatus(Map<String, Object> inputMap);

	List<TourModel> getToursByPassengerIdPagination(@Param("userId") String userId, @Param("afterTime") long afterTime, @Param("start") long start, @Param("length") long length);

	List<TourModel> getToursByDriverIdPagination(@Param("userId") String userId, @Param("afterTime") long afterTime, @Param("start") long start, @Param("length") long length);

	int updateTourCarIdByTourId(TourModel tourModel);

	List<TourModel> getCronJobRideLaterTourList(Map<String, Object> inputMap);

	List<TourModel> getCronJobRideLaterTourListForNotification(Map<String, Object> inputMap);

	int updateTourStatusCritical(TourModel tourModel);

	int updateRideLaterTourFlag(TourModel tourModel);

	int updateTourRideLaterLastNotification(TourModel tourModel);

	List<TourModel> getRideLaterToursByPassengerIdPagination(@Param("userId") String userId, @Param("start") long start, @Param("length") long length);

	List<TourModel> getRideLaterToursByDriverIdPagination(@Param("userId") String userId, @Param("start") long start, @Param("length") long length);

	int getRideLaterPassengerDetailsBetweenTimeSlot(@Param("userId") String userId, @Param("beforePickupTime") long beforePickupTime, @Param("afterPickupTime") long afterPickupTime);

	int getRideLaterTourListCount(@Param("startDate") long startDate, @Param("endDate") long endDate, @Param("isTakeRide") boolean isTakeRide, @Param("serviceTypeId") String serviceTypeId);

	List<TourModel> getRideLaterTourListBySearch(@Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("globalSearchString") String globalSearchString, @Param("startDate") long startDate, @Param("endDate") long endDate,
				@Param("isTakeRide") boolean isTakeRide, @Param("serviceTypeId") String serviceTypeId);

	int getCriticalRideLaterTourListCount(@Param("rideLaterVisitedTime") long rideLaterVisitedTime, @Param("startDate") long startDate, @Param("endDate") long endDate);

	List<TourModel> getCriticalRideLaterTourListBySearch(@Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("globalSearchString") String globalSearchString, @Param("rideLaterVisitedTime") long rideLaterVisitedTime,
				@Param("startDate") long startDate, @Param("endDate") long endDate);

	int getDriverAllTourListCount(Map<String, Object> inputMap);

	List<TourModel> getDriverAllTourListBySearch(Map<String, Object> inputMap);

	int updateRideLaterTourAcknowledgeByTourId(TourModel tourModel);

	int updateDistanceLiveByTourId(TourModel tourModel);

	List<TourModel> getTourListForHeatMap(Map<String, Object> inputMap);

	boolean checkDriverIsBusyInRideNowAndLaterTrip(Map<String, Object> inputMap);

	int updateFavouriteLocations(TourModel tourModel);

	boolean checkPassengerCurrentTourAsFirstTour(Map<String, Object> inputMap);

	List<TourModel> getTourListForBookingsExport(Map<String, Object> inputMap);

	int getVendorsTotalBookingsCountByTime(Map<String, Object> inputMap);

	List<TourModel> getVendorsAllTourListBySearch(Map<String, Object> inputMap);

	int getVendorsAllTourListCount(Map<String, Object> inputMap);

	List<TourModel> getVendorRideLaterTourListBySearch(Map<String, Object> inputMap);

	List<TourModel> getVendorDriverReportTourListByDateByUserIds(@Param("tourStartDate") long tourStartDate, @Param("tourEndDate") long tourEndDate, @Param("userIds") ArrayList<String> userIds, @Param("loggedInUserId") String loggedInUserId);

	List<TourModel> getVendorDriverReportTourListByDate(@Param("tourStartDate") long tourStartDate, @Param("tourEndDate") long tourEndDate, @Param("loggedInUserId") String loggedInUserId);

	List<TourModel> getVendorCriticalRideLaterTourListBySearch(@Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("globalSearchString") String globalSearchString, @Param("rideLaterVisitedTime") long rideLaterVisitedTime,
				@Param("startDate") long startDate, @Param("endDate") long endDate, @Param("statusList") List<String> statusList, @Param("userId") String userId, @Param("assignedRegionList") List<String> assignedRegionList);

	int getVendorRideLaterTourListCount(@Param("startDate") long startDate, @Param("endDate") long endDate, @Param("statusList") List<String> statusList, @Param("userId") String userId, @Param("assignedRegionList") List<String> assignedRegionList,
				@Param("isTakeRide") boolean isTakeRide, @Param("serviceTypeId") String serviceTypeId);

	int getVendorCriticalRideLaterTourListCount(@Param("rideLaterVisitedTime") long rideLaterVisitedTime, @Param("startDate") long startDate, @Param("endDate") long endDate, @Param("statusList") List<String> statusList, @Param("userId") String userId,
				@Param("assignedRegionList") List<String> assignedRegionList);

	TourModel getDriverDetailsByTourId(String tourId);

	List<TourModel> getRideLaterTourListToExpire(Map<String, Object> inputMap);

	int expireToursBatch(Map<String, Object> inputMap);

	void updateVendorWiseCarFare(TourModel tourModel);

	void updateDriverVendorId(TourModel tourModel);

	void updateScriptPassengerAndDriverVendorIdByCreatedAt(TourModel tourModel);

	List<TourModel> getScriptTourListByCreatedAt(@Param("tourDate") long tourDate);

	void updateScriptPassengerAndDriverVendorIdByTourId(TourModel tourModel);

	void updateDriverSubscriptionAgainstTour(TourModel tourModel);

	void updateTourDemandSupplierParameters(TourModel tourModel);

	void updateTourAsTakeRide(TourModel tourModel);

	List<TourModel> getCouriersForProcessingCronJob(@Param("courierStatus") List<String> courierStatus, @Param("start") int start, @Param("length") int length, @Param("serviceTypeId") String serviceTypeId, @Param("currentTime") long currentTime,
				@Param("nextOneHour") long nextOneHour);

	List<TourModel> getToursForProcessingCronJob(@Param("tourStatus") List<String> tourStatus, @Param("start") int start, @Param("length") int length, @Param("serviceTypeId") String serviceTypeId, @Param("currentTime") long currentTime);

	List<TourModel> getAllToursDataForMigration(@Param("start") int start, @Param("length") int length);

	List<TourModel> getCouriersHistoryByUserId(@Param("userId") String userId, @Param("roleId") String roleId, @Param("serviceTypeId") String serviceTypeId, @Param("start") int start, @Param("length") int length);

	List<TourModel> getRideLaterToursForTakeBookings(@Param("driverId") String driverId, @Param("regionList") List<String> regionList, @Param("carTypeList") List<String> carTypeList, @Param("maxTime") long maxTime, @Param("start") int start, @Param("length") int length,
				@Param("serviceTypeId") String serviceTypeId);

	List<TourModel> getRideNowToursForTakeBookings(@Param("driverId") String driverId, @Param("regionList") List<String> regionList, @Param("carTypeList") List<String> carTypeList, @Param("start") int start, @Param("length") int length,
				@Param("serviceTypeId") String serviceTypeId, @Param("statusList") List<String> statusList, @Param("latAndLong") String latAndLong);

	int getTakeBookingDriverCurrentCount(@Param("driverId") String driverId, @Param("serviceTypeId") String serviceTypeId);

	void updateTakeBookingByDriverParams(TourModel tourModel);

	int checkForConflictingSlotForRideLater(@Param("tourId") String tourId, @Param("driverId") String driverId, @Param("serviceTypeId") String serviceTypeId, @Param("beforePickupTime") long beforePickupTime, @Param("afterPickupTime") long afterPickupTime);

	void updateDriverProcessingViaCronTime(TourModel tourModel);
	List<TourModel>  getTourdetails(String driverId);
	
	TourModel getCurrentTourByPassangerIdNew(@Param("userId") String userId);
}