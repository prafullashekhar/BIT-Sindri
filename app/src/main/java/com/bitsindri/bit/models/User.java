package com.bitsindri.bit.models;

public class User {

    String name, rollNo, regNo, email;

    // empty constructor
    public User(){
    }

    public User(String name, String rollNo, String regNo, String email){
        this.name=name;
        this.rollNo=rollNo;
        this.regNo=regNo;
        this.email=email;
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
}
