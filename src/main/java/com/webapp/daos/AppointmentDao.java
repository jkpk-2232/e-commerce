package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.AppointmentModel;
import com.webapp.models.StatsModel;

public interface AppointmentDao {

	void insertAppointment(AppointmentModel appointmentModel);

	AppointmentModel getAppointmentLimitedDetailsByAppointmentId(@Param("appointmentId") String appointmentId);

	List<AppointmentModel> getAppointmentsByUserId(@Param("userId") String userId, @Param("start") int start, @Param("length") int length, @Param("appointmentShortId") int appointmentShortId, @Param("statusNotToBeConsidered") List<String> statusNotToBeConsidered);

	AppointmentModel getAppointmentDetailsByAppointmentIdWithOrderItems(@Param("appointmentId") String appointmentId);

	AppointmentModel getAppointmentDetailsByPaymentToken(@Param("paymentToken") String paymentToken);

	AppointmentModel getAppointmentDetailsByAppointmentId(@Param("appointmentId") String appointmentId);

	void updateAppointment(AppointmentModel appointmentModel);

	void updateAppointmentStatus(AppointmentModel appointmentModel);

	void updatePaymentStatus(AppointmentModel appointmentModel);

	List<AppointmentModel> getAppointmentsSearchAPI(@Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("vendorId") String vendorId, @Param("appointmentStatus") List<String> appointmentStatus,
				@Param("appointmentShortIdSearch") int appointmentShortIdSearch, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	int getAppointmentsCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("vendorId") String vendorId, @Param("serviceId") String serviceId, @Param("categoryId") String categoryId,
				@Param("appointmentStatus") List<String> appointmentStatus, @Param("appointmentShortIdSearch") int appointmentShortIdSearch, @Param("appointmentStatusFilter") String appointmentStatusFilter, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	List<AppointmentModel> getAppointmentsSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("displayType") String displayType,
				@Param("vendorId") String vendorId, @Param("serviceId") String serviceId, @Param("categoryId") String categoryId, @Param("appointmentStatus") List<String> appointmentStatus, @Param("appointmentShortIdSearch") int appointmentShortIdSearch,
				@Param("appointmentStatusFilter") String appointmentStatusFilter, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	int getAppointmentsSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("vendorId") String vendorId, @Param("serviceId") String serviceId, @Param("categoryId") String categoryId,
				@Param("appointmentStatus") List<String> appointmentStatus, @Param("appointmentShortIdSearch") int appointmentShortIdSearch, @Param("appointmentStatusFilter") String appointmentStatusFilter, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	List<AppointmentModel> getAppointmentForProcessingCronJob(@Param("appointmentStatus") List<String> appointmentStatus, @Param("start") int start, @Param("length") int length);

	StatsModel getAppointmentStatsByTimeForVendor(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList, @Param("startTime") long startTime, @Param("endTime") long endTime);

	List<StatsModel> getAppointmentListForHighestSpendingCustomers(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList, @Param("startTime") long startTime, @Param("endTime") long endTime, @Param("start") int start,
				@Param("length") int length);
}