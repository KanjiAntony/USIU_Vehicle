package com.example.usiuparking;

import java.util.Date;

public class User {

    String UserID;
    String UserAbility;
    Date sessionExpiryDate;

    public void setUserID(String user)
    {
        this.UserID = user;
    }

    public void setUserAbility(String user_ability)
    {
        this.UserAbility = user_ability;
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

    public Date getSessionExpiryDate()
    {
        return sessionExpiryDate;
    }

}
