package com.bitsindri.bit.models;

import android.app.DownloadManager;
import android.net.Uri;

public class User {

    String Name;
    String Email;
    String Batch;
    String Branch;
    String Roll;
    String RegNo;
    String ProfilePic;
    String DOB;
    String Club;
    String Uid;

    String Codechef, LinkedIn, Facebook, Instagram, Github, Codefroces;
    String About;

    // empty constructor
    public User(){
    }

    public User(String name, String email, String batch, String branch, String rollNo, String regNo){
        this.Name=name;
        this.Email=email;
        this.Batch=batch;
        this.Branch=branch;
        this.Roll=rollNo;
        this.RegNo=regNo;

    }

    // complete constructor
    public User(String name, String email, String batch, String branch, String rollNo, String regNo, String about,
                String codechefUrl, String linkedInUrl, String facebookUrl, String instaUrl, String githubUrl, String codefrocesUrl,
                String profilePic, String dob, String club){

        this.Name=name;
        this.Email=email;
        this.Batch=batch;
        this.Branch=branch;
        this.Roll=rollNo;
        this.RegNo=regNo;
        this.About=about;
        this.Codechef=codechefUrl;
        this.LinkedIn=linkedInUrl;
        this.Facebook=facebookUrl;
        this.Instagram=instaUrl;
        this.Github=githubUrl;
        this.Codefroces=codefrocesUrl;
        this.ProfilePic=profilePic;
        this.DOB=dob;
        this.Club=club;
    }


    // getter and setter of different variables
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getRollNo() {
        return Roll;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public void setRollNo(String rollNo) {
        this.Roll = rollNo;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        this.ProfilePic = profilePic;
    }

    public String getDob() {
        return DOB;
    }

    public void setDob(String dob) {
        this.DOB = dob;
    }

    public String getClub() {
        return Club;
    }

    public void setClub(String club) {
        this.Club = club;
    }

    public String getCodechefUrl() {
        return Codechef;
    }

    public void setCodechefUrl(String codechefUrl) {
        this.Codechef = codechefUrl;
    }

    public String getLinkedInUrl() {
        return LinkedIn;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.LinkedIn = linkedInUrl;
    }

    public String getFacebookUrl() {
        return Facebook;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.Facebook = facebookUrl;
    }

    public String getInstaUrl() {
        return Instagram;
    }

    public void setInstaUrl(String instaUrl) {
        this.Instagram = instaUrl;
    }

    public String getGithubUrl() {
        return Github;
    }

    public void setGithubUrl(String githubUrl) {
        this.Github = githubUrl;
    }

    public String getCodefrocesUrl() {
        return Codefroces;
    }

    public void setCodefrocesUrl(String codefrocesUrl) {
        this.Codefroces = codefrocesUrl;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        this.About = about;
    }

    public String getRegNo() {
        return RegNo;
    }

    public void setRegNo(String regNo) {
        this.RegNo = regNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getBatch() {
        return Batch;
    }

    public void setBatch(String batch) {
        this.Batch = batch;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        this.Branch = branch;
    }
}
