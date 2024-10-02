package com.webapp.daos;

import com.webapp.models.CancellationChargeModel;

public interface CancellationChargeDao {

	int updateAdminCancellationCharges(CancellationChargeModel cancellationChargeModel);

	CancellationChargeModel getAdminCancellationCharges();
}
