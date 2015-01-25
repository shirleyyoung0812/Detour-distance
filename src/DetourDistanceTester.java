
public class DetourDistanceTester {

	public static void main(String[] args) {
		GeoCoordinate[] driver1 = new 	GeoCoordinate[2];	
		driver1[0] = new GeoCoordinate("37.738805", "-122.423973");
		driver1[1] = new GeoCoordinate("37°25'28\"N","122°05'42\"W");
		GeoCoordinate[] driver2 = new 	GeoCoordinate[2];
		driver2[0] = new GeoCoordinate("N37 47 17", "W122 24");
		driver2[1] = new GeoCoordinate("N37", "122°04'40\"W");
		DetourDistance dd = new DetourDistance();
		dd.shorterDetour(driver1, driver2);
	}

}
