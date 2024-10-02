//package com.webapp.actions.api.scripts;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.Response;
//
//import com.utils.myhub.UnifiedHistoryUtils;
//import com.webapp.ProjectConstants;
//import com.webapp.actions.BusinessApiAction;
//import com.webapp.models.OrderModel;
//import com.webapp.models.TourModel;
//import com.webapp.models.UnifiedHistoryModel;
//
//@Path("/api/script-migrate-data-to-unified-history")
//public class ScriptMigrateDataToUnifiedHistory extends BusinessApiAction {
//
//	@GET
//	@Produces({ "application/json", "application/xml" })
//	//@formatter:off
//	public Response getCarFare(
//		@Context HttpServletRequest request, 
//		@Context HttpServletResponse response
//		) throws SQLException {
//	//@formatter:on
//
//		/**
//		 * 1. Script to migrate data to the unified history table.
//		 **/
//
//		// Step 1: Get all the tours data
//		int start = 0;
//		int length = ProjectConstants.BATCH_INSERT_SIZE;
//
//		int tourCount = 0;
//		List<TourModel> tourList = new ArrayList<>();
//		List<UnifiedHistoryModel> unifiedHistoryList = new ArrayList<>();
//
//		while (true) {
//
//			tourList = TourModel.getAllToursDataForMigration(start, length);
//
//			if (tourList.isEmpty()) {
//				break;
//			}
//
//			for (TourModel tourModel : tourList) {
//
//				tourCount++;
//
//				unifiedHistoryList.add(UnifiedHistoryUtils.setToursData(tourModel));
//			}
//
//			if (!unifiedHistoryList.isEmpty()) {
//
//				UnifiedHistoryModel.batchInsertUnifiedHistoryData(unifiedHistoryList);
//				unifiedHistoryList.clear();
//			}
//
//			start += ProjectConstants.BATCH_INSERT_SIZE;
//		}
//
//		// Step 2: Get all the orders data
//
//		start = 0;
//		int orderCount = 0;
//		List<OrderModel> orderList = new ArrayList<>();
//
//		while (true) {
//
//			orderList = OrderModel.getAllOrdersDataForMigration(start, length);
//
//			if (orderList.isEmpty()) {
//				break;
//			}
//
//			for (OrderModel orderModel : orderList) {
//
//				orderCount++;
//
//				unifiedHistoryList.add(UnifiedHistoryUtils.setOrdersData(orderModel));
//			}
//
//			if (!unifiedHistoryList.isEmpty()) {
//
//				UnifiedHistoryModel.batchInsertUnifiedHistoryData(unifiedHistoryList);
//				unifiedHistoryList.clear();
//			}
//
//			start += ProjectConstants.BATCH_INSERT_SIZE;
//		}
//
//		return sendSuccessMessage("Script for migration of data to unified history -> tourCount :: " + tourCount + " orderCount :: " + orderCount);
//	}
//}