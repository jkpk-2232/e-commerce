package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.TrackLocationTokenModel;



public interface TrackLocationTokenDao {
	
	int insertTrackLocationDetails(TrackLocationTokenModel trackLocationTokenModel);
	
	TrackLocationTokenModel getTrackLocationTokenDetailsById(@Param("trackLocationTokenId") String trackLocationTokenId);

}
