package com.egco428.mupts;

/**
 * Created by Thammarit on 17/12/2559.
 */

public class Location {

    /*Class name Location to store location and image's location
    * of each place*/

    private String location;
    private int imageLocation;

    public Location(){
        /*Blank default constructor essential for Firebase*/
    }

    public Location(String location, int imageLocation){
        this.location = location;
        this.imageLocation = imageLocation;
    }

    //Getters and Setters

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(int imageLocation) {
        this.imageLocation = imageLocation;
    }

}
