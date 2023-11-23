package com.example.deliverable1;

import java.io.Serializable;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private String phNumber;
    private String Address;
    private String status;//'Pending', 'Approved','Rejected'
    public User (String firstName,String lastName,String emailAddress,String password,String phNumber,String address){
        this.firstName = firstName;
        this.lastName= lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.phNumber = phNumber;
        this.Address = address;
        this.status="Pending";
    }


    public String getFirstName(){
        return firstName;
    }

    public void setFirstName (String FirstName){
        this.firstName = FirstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName (String LastName){
        this.lastName = LastName;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setEmailAddress(String EmailAddress){
        this.emailAddress = EmailAddress;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword (String Password){
        this.password = Password;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String PhNumber) {
        this.phNumber = PhNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress (String address){
        this.Address = address;
    }
    public String getStatus(){return status;}
    public void setStatus(String status){
        this.status=status;
    }
}
