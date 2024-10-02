package com.webapp.daos;

import java.util.List;
import java.util.Map;

import com.webapp.models.TourReferrerBenefitModel;

public interface TourReferrerBenefitDao {

	int addTourReferrerBenefit(TourReferrerBenefitModel tourReferrerBenefitModel);

	TourReferrerBenefitModel getTourReferrerBenefitByTourId(Map<String, Object> inputMap);

	int getTotalTourReferrerBenefitCountForSearch(Map<String, Object> inputMap);

	List<TourReferrerBenefitModel> getTourReferrerBenefitListForSearch(Map<String, Object> inputMap);

	double getTotalDriverBenefitByDriverId(Map<String, Object> inputMap);

	List<TourReferrerBenefitModel> getDriverReferrerlListByDriverId(Map<String, Object> inputMap); 

}