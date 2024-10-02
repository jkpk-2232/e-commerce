package com.webapp.daos;

import com.webapp.models.TourTimeModel;

public interface TourTimeDao {

	int createTourTime(TourTimeModel tourTimeModel);

	int updateTourAcceptedTime(TourTimeModel tourTimeModel);

	int updateTourArrivedWaitingTime(TourTimeModel tourTimeModel);

	int updateTourStartTime(TourTimeModel tourTimeModel);

	int updateTourEndTime(TourTimeModel tourTimeModel);
	
	TourTimeModel getTourTimesDetailsByTourId(String tourId);

}
