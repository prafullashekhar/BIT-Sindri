package com.bitsindri.bit.models;

import java.util.ArrayList;
import java.util.List;

public class ClubData {

    long clubId;
    String clubName;
    String clubLogoUrl;
    String clubBackgroundUrl;
    String clubDescription;
    List<String> clubTags;
    String clubAchievements;

    String clubFacebook;
    String clubInstagram;
    String clubLinkedIn;
    String clubTwitter;
    String clubGmail;
    String clubWebsite;



    // default constructor
    public ClubData(){
    }

    public ClubData(long clubId, String clubName, String clubLogoUrl, String clubBackgroundUrl, String clubDescription,
                    ArrayList<String> clubTags, String clubAchievements, String clubFacebook, String clubInstagram,
                    String clubLinkedIn, String clubTwitter, String clubGmail, String clubWebsite) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubLogoUrl = clubLogoUrl;
        this.clubBackgroundUrl = clubBackgroundUrl;
        this.clubDescription = clubDescription;
        this.clubTags = clubTags;
        this.clubAchievements = clubAchievements;
        this.clubFacebook = clubFacebook;
        this.clubInstagram = clubInstagram;
        this.clubLinkedIn = clubLinkedIn;
        this.clubTwitter = clubTwitter;
        this.clubGmail = clubGmail;
        this.clubWebsite = clubWebsite;
    }

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

    public String getClubBackgroundUrl() {
        return clubBackgroundUrl;
    }

    public void setClubBackgroundUrl(String clubBackgroundUrl) {
        this.clubBackgroundUrl = clubBackgroundUrl;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }

    public List<String> getClubTags() {
        return clubTags;
    }

    public void setClubTags(List<String> clubTags) {
        this.clubTags = clubTags;
    }

    public String getClubAchievements() {
        return clubAchievements;
    }

    public void setClubAchievements(String clubAchievements) {
        this.clubAchievements = clubAchievements;
    }

    public String getClubFacebook() {
        return clubFacebook;
    }

    public void setClubFacebook(String clubFacebook) {
        this.clubFacebook = clubFacebook;
    }

    public String getClubInstagram() {
        return clubInstagram;
    }

    public void setClubInstagram(String clubInstagram) {
        this.clubInstagram = clubInstagram;
    }

    public String getClubLinkedIn() {
        return clubLinkedIn;
    }

    public void setClubLinkedIn(String clubLinkedIn) {
        this.clubLinkedIn = clubLinkedIn;
    }

    public String getClubTwitter() {
        return clubTwitter;
    }

    public void setClubTwitter(String clubTwitter) {
        this.clubTwitter = clubTwitter;
    }

    public String getClubGmail() {
        return clubGmail;
    }

    public void setClubGmail(String clubGmail) {
        this.clubGmail = clubGmail;
    }

    public String getClubWebsite() {
        return clubWebsite;
    }

    public void setClubWebsite(String clubWebsite) {
        this.clubWebsite = clubWebsite;
    }
}
