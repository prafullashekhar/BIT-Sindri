package com.bitsindri.bit.models;

public class User {

    String name;
    String email;
    String batch;
    String branch;
    String rollNo;
    String regNo;

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
