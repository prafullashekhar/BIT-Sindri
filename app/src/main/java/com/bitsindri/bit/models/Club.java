package com.bitsindri.bit.models;

public class Club {

    long clubId;
    String clubName;
    String clubLogoUrl;
    String clubDescription;

    // default constructor
    public Club(){
    }

    public Club(long clubId, String clubName, String clubLogoUrl, String clubDescription){
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubLogoUrl = clubLogoUrl;
        this.clubDescription = clubDescription;
    }

    /*--------------------getters ans setters----------------------------------*/
    public long getClubId() {
        return clubId;
    }

    public void setClubId(long clubId) {
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

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }
}
