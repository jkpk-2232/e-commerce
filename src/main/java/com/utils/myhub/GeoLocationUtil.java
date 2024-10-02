package com.utils.myhub;

public class GeoLocationUtil {

	public static String SOURCE_LOCATION = "source_geolocation";
	public static String CAR_LOCATION = "car_location";
	public static String REGION_LOCATION = "region_geolocation";
	public static String STORE_ADDRESS_GEOLOCATION = "vs.store_address_geolocation";

	public static final String productionEnvironment = WebappPropertyUtils.getWebAppProperty("productionEnvironment");

	// car_location
	public static String getDistanceQuery(String lat, String lng, String type) {

		if (productionEnvironment.equalsIgnoreCase("yes")) {
			return "round(CAST(ST_DistanceSphere(" + type + "::geometry, ST_GeomFromText('POINT(" + lng + " " + lat + ")',4326)) As numeric),2)";
		} else {
			String constant = "\"WGS 84\"";
			String distance = "round(CAST(ST_Distance_Spheroid(" + type + "::geometry, ST_GeomFromText('POINT(" + lng + " " + lat + ")',4326), 'SPHEROID[" + constant + ",6378137,298.257223563]')As numeric),2)";
			return distance;
		}
	}

	public static String getLatLngQuery(String lat, String lng, String type, String radius) {
		return "ST_DWithin(" + type + ",ST_GeographyFromText('SRID=4326;POINT(" + lng + " " + lat + ")'),  " + radius + ")";
	}
}
