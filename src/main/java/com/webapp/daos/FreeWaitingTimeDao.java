package com.webapp.daos;

import com.webapp.models.FreeWaitingTimeModel;

public interface FreeWaitingTimeDao {
	
	int updateWaitingTime(FreeWaitingTimeModel freeWaitingTimeModel);

	FreeWaitingTimeModel getFreeWaitingTime();

	int updateCancelTime(FreeWaitingTimeModel freeWaitingTimeModel);
}
