package com.egco428.mupts;

/**
 * Created by Thammarit on 14/12/2559.
 */

public class User {

    /*Class name user that keep user's information*/

    private String username;
    private String password;
    private String email;
    private String birthday;

    public User(){
        /*Blank default constructor essential for Firebase*/
    }

    //Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
