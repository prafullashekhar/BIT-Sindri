package com.bitsindri.bit.models;

public class User {

    String name;
    String email;
    String batch;
    String branch;
    String rollNo;
    String regNo;
    String profilePic;
    String dob;
    String club;

    String codechefUrl, linkedInUrl, facebookUrl, instaUrl, githubUrl, codefrocesUrl;
    String about;

    // empty constructor
    public User(){
    }

    public User(String name, String email, String batch, String branch, String rollNo, String regNo){
        this.name=name;
        this.email=email;
        this.batch=batch;
        this.branch=branch;
        this.rollNo=rollNo;
        this.regNo=regNo;

    }

    // complete constructor
    public User(String name, String email, String batch, String branch, String rollNo, String regNo, String about,
                String codechefUrl, String linkedInUrl, String facebookUrl, String instaUrl, String githubUrl, String codefrocesUrl,
                String profilePic, String dob, String club){

        this.name=name;
        this.email=email;
        this.batch=batch;
        this.branch=branch;
        this.rollNo=rollNo;
        this.regNo=regNo;
        this.about=about;
        this.codechefUrl=codechefUrl;
        this.linkedInUrl=linkedInUrl;
        this.facebookUrl=facebookUrl;
        this.instaUrl=instaUrl;
        this.githubUrl=githubUrl;
        this.codefrocesUrl=codefrocesUrl;
        this.profilePic=profilePic;
        this.dob=dob;
        this.club=club;
    }


    // getter and setter of different variables
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getCodechefUrl() {
        return codechefUrl;
    }

    public void setCodechefUrl(String codechefUrl) {
        this.codechefUrl = codechefUrl;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstaUrl() {
        return instaUrl;
    }

    public void setInstaUrl(String instaUrl) {
        this.instaUrl = instaUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getCodefrocesUrl() {
        return codefrocesUrl;
    }

    public void setCodefrocesUrl(String codefrocesUrl) {
        this.codefrocesUrl = codefrocesUrl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
