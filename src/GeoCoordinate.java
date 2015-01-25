/**
 * this class is used to get the coordinates of a point on earth
 * The constructor will create a geoCoordinate object with provided
 * latitude and longitude. 
 * 
 * @author shirleyyoung
 *
 */
import java.util.*;
public class GeoCoordinate {
	private double latitude;
	private double longitude;
	//In order to take into account different coordinate formats,
	//use string as input
	public GeoCoordinate(String latitude, String longitude) {
		this.latitude = convertCoordinate(latitude, true);
		this.longitude = convertCoordinate(longitude, false);
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = convertCoordinate(latitude, true);
		
	}
	public void setLongitude(String longitude) {
		this.longitude = convertCoordinate(longitude, false);
	}
	/**
	 * This method convert the input string to double type 
	 * that will be used to calculate distance
	 * @param coord
	 * @param isLatitude
	 * @return
	 */
	private double convertCoordinate(String coord, boolean isLatitude) {
		String direction = "NESW";
		double coordinate = 0.0;
		if (direction.contains(coord.substring(0, 1)) || direction.contains(coord.substring(coord.length() - 1))) {
			if (isValidDegree(coord, isLatitude))
				coordinate = convertDegree(coord);
			else
				throw new IllegalArgumentException("Invalid input!");
		}
		else {
			if (isValidDecimal(coord))
				coordinate = convertDecimal(coord);
			else
				throw new IllegalArgumentException("Invalid input!");
		}
		return coordinate;
	}
	/**
	 * check if the degree form of input coordinate is valid
	 * Some valid degree form: 
	 * 41 25 01N
	 * 41°25'01"N
	 * N41 25 01
	 * N41°25'01"
	 * 41°25'N
	 * 41°N
	 * N41.092
	 * @param coord
	 * @return
	 */
	private boolean isValidDegree(String coord, boolean isLatitude) {
		if (coord.charAt(0) == 'N' || coord.charAt(0) == 'S' 
			|| coord.charAt(0) == 'E' || coord.charAt(0) == 'W') {
			if (coord.charAt(0) == 'N' || coord.charAt(0) == 'S') {
				if(!isLatitude)
					return false;
			}
			else if (coord.charAt(0) == 'E' || coord.charAt(0) == 'W') {
				if (isLatitude)
					return false;
			}
			coord = coord.substring(1);
		}
		else if (coord.charAt(coord.length() - 1) == 'N' || coord.charAt(coord.length() - 1) == 'S' 
				|| coord.charAt(coord.length() - 1) == 'E' || coord.charAt(coord.length() - 1) == 'W') {
			if (coord.charAt(coord.length() - 1) == 'N' || coord.charAt(coord.length() - 1) == 'S') {
				if(!isLatitude)
					return false;
			}
			else if (coord.charAt(coord.length() - 1) == 'E' || coord.charAt(coord.length() - 1) == 'W') {
				if (isLatitude)
					return false;
			}
			coord = coord.substring(0, coord.length() - 1);
		}
		if (isValidDecimal(coord))
			return true;
		boolean degree = false;
		boolean min = false;
		boolean sec = false; 
		boolean whiteSpace = false;
		for (int i = 0; i < coord.length(); i++) {
			if (coord.charAt(i) == '\u00b0') {
				if (degree || whiteSpace)
					return false;
				degree = true;
			}
			else if (coord.charAt(i) == '\'') {
				if (whiteSpace || !degree || min)
					return false;
				min = true;
			}
			else if (coord.charAt(i) == '\"') {
				if (whiteSpace || !degree || !min || sec)
					return false;
				sec = true;
			}
			else if (Character.isSpaceChar(coord.charAt(i))) {
				if (degree || min || sec)
					return false;
				whiteSpace = true;
			}
			
			else if (Character.isDigit(coord.charAt(i)))
				continue;
			else 
				return false;
		}
		return true;
	}
	
	private double convertDegree(String coord) {
		boolean isNegative = false;
		if (Character.isLetter(coord.charAt(0))) {
			if (coord.charAt(0) == 'S' || coord.charAt(0) == 'W')
				isNegative = true;
			coord = coord.substring(1);
		}
		if (Character.isLetter(coord.charAt(coord.length() - 1))) {
			if (coord.charAt(coord.length() - 1) == 'S' || coord.charAt(coord.length() - 1) == 'W')
				isNegative = true;
			coord = coord.substring(0, coord.length() - 1);
		}
		List<String> num = new ArrayList<String>();
	
		StringTokenizer tok = new StringTokenizer(coord, " \u00b0\'\"");
		while (tok.hasMoreTokens()) {
			num.add(tok.nextToken());
		}
		double coordinate = 0.0;
		double base = 60.0;
		if (num.size() == 1) 
			coordinate = Double.parseDouble(num.get(0));
		else if (num.size() == 2) {
			if (Double.parseDouble(num.get(1)) > 60.0)
				throw new IllegalArgumentException("Invalid input!");
			coordinate = Double.parseDouble(num.get(0)) + Double.parseDouble(num.get(1)) / base;
		}
		else {
			if (Double.parseDouble(num.get(1)) > 60.0 || Double.parseDouble(num.get(2)) > 60.0)
				throw new IllegalArgumentException("Invalid input!");		
			coordinate = Double.parseDouble(num.get(0)) + Double.parseDouble(num.get(1)) / base 
			+ Double.parseDouble(num.get(2)) / Math.pow(base, 2);
		}
		return isNegative ? -coordinate : coordinate;
	}
	
	/**
	 * check if the decimal form is valid
	 * +41.25
	 * -41.25
	 * @param coord
	 * @return
	 */
	private boolean isValidDecimal(String coord) {
		if (coord.charAt(0) == '+' || coord.charAt(0) == '-')
			coord = coord.substring(1);
		boolean dot = false;
		for (int i = 0; i < coord.length(); i++) {
			if (coord.charAt(i) == '.') {
				if (dot)
					return false;
				dot = true;
			}
			else if (Character.isDigit(coord.charAt(i)))
				continue;
			else 
				return false;
		}
		return true;
	}
	private double convertDecimal(String coord) {
		boolean isNegative = false;
		if (coord.charAt(0) == '-') {
			isNegative = true;
			coord = coord.substring(1);
		}
		if (coord.charAt(0) == '+')
			coord = coord.substring(1);
		double coordinate = Double.parseDouble(coord);
		return isNegative ? -coordinate : coordinate;
	}
	
}
