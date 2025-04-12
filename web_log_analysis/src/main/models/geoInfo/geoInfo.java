package models.geoInfo;

import com.ip2location.IPResult;

public class geoInfo {
    private String countryShort = null;
    private String countryLong = null;
    private String region = null;
    private String city = null;
    private Float latitude = null;
    private Float longitude;
    private String zipCode;
    private String timeZone;
    public geoInfo(IPResult ipResult) {
        this.countryShort = ipResult.getCountryShort();
        this.countryLong = ipResult.getCountryLong();
        this.region = ipResult.getRegion();
        this.city = ipResult.getCity();
        this.latitude = ipResult.getLatitude();
        this.longitude = ipResult.getLongitude();
        this.zipCode = ipResult.getZipCode();
        this.timeZone = ipResult.getTimeZone();
    }
    public String getCountryShort() {
        return this.countryShort;
    }
    public String getCountryLong() {
        return this.countryLong;
    }
    public String getRegion() {
        return this.region;
    }
    public String getCity() {
        return this.city;
    }
    public Float getLatitude() {
        return this.latitude;
    }
    public Float getLongitude() {
        return this.longitude;
    }
    public String getZipCode() {
        return this.zipCode;
    }
    public String getTimeZone() {
        return this.timeZone;
    }
    @Override
    public String toString() {
        return "[" + "Country Short" + " : " + this.getCountryShort() + ", " +
                "Country Long" + " : " + this.getCountryLong() + ", " +
                "Region" + " : " + this.getRegion() + ", " + 
                "City" + " : " + this.getCity() + ", " + 
                "Latitude" + " : " + this.getLatitude() + ", " + 
                "Longitude" + " : " + this.getLongitude() + ", " + 
                "ZipCode" + " : " + this.getZipCode() + ", " + 
                "TimeZone" + " : " + this.getTimeZone() + "]";
    }
}
