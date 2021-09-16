package com.bitsindri.bit.models;

public class Club {

    String clubId;
    String clubName;
    String clubLogoUrl;
    String clubBackgroundUrl;

    // default constructor
    public Club(){
    }

    public Club(String clubId, String clubName, String clubLogoUrl, String clubBackgroundUrl){
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubLogoUrl = clubLogoUrl;
        this.clubBackgroundUrl = clubBackgroundUrl;
    }

    /*--------------------getters ans setters----------------------------------*/
    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubLogoUrl() {
        return clubLogoUrl;
    }

    public void setClubLogoUrl(String clubLogoUrl) {
        this.clubLogoUrl = clubLogoUrl;
    }

    public String getClubBackgroundUrl() {
        return clubBackgroundUrl;
    }

    public void setClubBackgroundUrl(String clubBackgroundUrl) {
        this.clubBackgroundUrl = clubBackgroundUrl;
    }
}
