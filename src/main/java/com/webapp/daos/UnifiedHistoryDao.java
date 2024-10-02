package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.UnifiedHistoryModel;

public interface UnifiedHistoryDao {

	void insertUnifiedHistory(UnifiedHistoryModel unifiedHistoryModel);

	void deleteEntryByHistoryId(UnifiedHistoryModel unifiedHistoryModel);

	void batchInsertUnifiedHistoryData(@Param("unifiedHistoryList") List<UnifiedHistoryModel> unifiedHistoryList);

	void updateOrderDeliveryStatus(UnifiedHistoryModel unifiedHistoryModel);

	void updateDelieveryManagedByVendorDriver(UnifiedHistoryModel unifiedHistoryModel);

	void updateDriverIdAgainstOrder(UnifiedHistoryModel unifiedHistoryModel);

	void updateCarTypeIdAgainstOrder(UnifiedHistoryModel unifiedHistoryModel);

	void assignTourDriver(UnifiedHistoryModel unifiedHistoryModel);

	void updateDriverVendorId(UnifiedHistoryModel unifiedHistoryModel);

	void updateTourCarIdByTourId(UnifiedHistoryModel unifiedHistoryModel);

	void updateChargesAndDriverAmount(UnifiedHistoryModel unifiedHistoryModel);

	void updatePromoCodeStatus(UnifiedHistoryModel unifiedHistoryModel);

	void updateCharges(UnifiedHistoryModel unifiedHistoryModel);

	void updateTourStatusByTourId(UnifiedHistoryModel unifiedHistoryModel);

	int expireToursBatch(Map<String, Object> inputMap);

	void updateRideLaterTourFlag(UnifiedHistoryModel unifiedHistoryModel);

	void updateTourStatusCritical(UnifiedHistoryModel unifiedHistoryModel);

	void updateTourAsTakeRide(UnifiedHistoryModel unifiedHistoryModel);

	void updateTourAddress(UnifiedHistoryModel unifiedHistoryModel);

	List<UnifiedHistoryModel> getUnifiedHistoryList(@Param("userId") String userId, @Param("start") int start, @Param("length") int length, @Param("roleId") String roleId, @Param("searchKey") String searchKey, @Param("statusList") List<String> statusList,
				@Param("vendorOrderManagement") String vendorOrderManagement);
}
