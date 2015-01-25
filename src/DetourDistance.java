import java.text.DecimalFormat;


public class DetourDistance {
	
	/**
	 * this method uses Haversine formula to calculate the distance
	 * between two points on earth given latitude and longitude
	 * reference: http://en.wikipedia.org/wiki/Haversine_formula
	 * @param A
	 * @param B
	 * @return
	 */
	public double getDistance(GeoCoordinate A, GeoCoordinate B) {
		double lati1 = Math.toRadians(A.getLatitude());
		double longi1 = Math.toRadians(A.getLongitude());
		double lati2 = Math.toRadians(B.getLatitude());
		double longi2 = Math.toRadians(B.getLongitude());
		//latitudes difference 
		double d_lati = lati2 - lati1;
		double d_longi = longi2 - longi1;
		//Haversine formula
		double haversine = Math.pow(Math.sin(d_lati / 2.0), 2) + 
				Math.cos(lati1) * Math.cos(lati2) * Math.pow(Math.sin(d_longi / 2.0), 2);
		double distance = 2 * earthRadius(lati1, lati2) * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));
		return distance;
	}
	/**
	 * determine which detour would be shorter
	 * input arrays are arrays with length 2, with element at index 0 the start point
	 * and index 1 the destination
	 * No matter which driver pick up and drop off the other one, he or she has to go 
	 * from his or her start point to the other driver's start point, e.g., A -> C, or C -> A
	 * as well as the other driver's destination to his or her destination
	 * e.g., D -> B, or B -> D
	 * So we only need to compare the distance between A -> B and C -> D, if A -> B is shorter, 
	 * then driver2 should pick up and drop off driver1, and vice versa. 
	 * @param driver1
	 * @param driver2
	 */
	public void shorterDetour(GeoCoordinate[] driver1, GeoCoordinate[] driver2) {
		double dist1 = getDistance(driver1[0], driver1[1]);
		double dist2 = getDistance(driver2[0], driver2[1]);
		System.out.println("The initial distance for driver1 is: " + new DecimalFormat("#0.000").format(dist1) + "km");
		System.out.println("The initial distance for driver2 is: " + new DecimalFormat("#0.000").format(dist2) + "km");
		if (dist1 > dist2)
			System.out.println("Driver1 should pick up driver2! ");
		else if (dist1 < dist2)
			System.out.println("Driver2 should pick up driver1!");
		else
			System.out.println("Either driver can pick up the other one!");
	}
	/**
	 * Getting the earth radius at each latitude
	 *reference: http://en.wikipedia.org/wiki/Earth_radius#Radius_at_a_given_geodetic_latitude
	 * @param latitude
	 * @return
	 */
	private double getEarthRadius (double latitude) {
		final double equatorialRadius = 6378.1370; //km
		final double polarRadius = 6356.7523; //km
		double dividend = Math.pow(Math.pow(equatorialRadius, 2) * Math.cos(latitude), 2) 
				+ Math.pow(Math.pow(polarRadius, 2) * Math.sin(latitude), 2);
		double divisor = Math.pow(equatorialRadius, 2) * Math.cos(latitude) 
				+ Math.pow(polarRadius, 2) * Math.sin(latitude);
		double radius = Math.sqrt(dividend / divisor);
		return radius;
	}
	private double earthRadius (double latitude1, double latitude2) {
		if (latitude1 == latitude2)
			return getEarthRadius(latitude1);
		double r1 = getEarthRadius(latitude1);
		double r2 = getEarthRadius(latitude2);
		if (r1 > r2) {
			return (2.0 * r1 + r2) / 3.0;
		}
		return (2.0 * r2 + r1) / 3.0;
	}
	

}
