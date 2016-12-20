package com.egco428.mupts;

/**
 * Created by Thammarit on 18/12/2559.
 */

public class LocationPics {

    /*Class name LocationPics that store username,url,latitude and longitude
    (Data of picture that user or admin have uploaded) */

    private String username;
    private String url;
    private String latitude;
    private String longtitude;

    public LocationPics(){

    }



    public LocationPics(String username, String url){
        this.username = username;
        this.url = url;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
}
