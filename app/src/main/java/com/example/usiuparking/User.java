package com.example.usiuparking;

import java.util.Date;

public class User {

    String UserID;
    String UserAbility;
    String ParkingLocation;
    Date sessionExpiryDate;

    public void setUserID(String user)
    {
        this.UserID = user;
    }

    public void setUserAbility(String user_ability)
    {
        this.UserAbility = user_ability;
    }

    public void setParkingLocation(String parking_location)
    {
        this.ParkingLocation = parking_location;
    }

    public void setSessionExpiryDate(Date expiry)
    {
        this.sessionExpiryDate = expiry;
    }

    public String getUserID()
    {
        return UserID;
    }

    public String getUserAbility()
    {
        return UserAbility;
    }

    public String getParkingLocation() {
        return ParkingLocation;
    }

    public Date getSessionExpiryDate()
    {
        return sessionExpiryDate;
    }

}
