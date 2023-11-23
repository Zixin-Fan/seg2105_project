package com.example.deliverable1;

import java.io.Serializable;

public class admin implements Serializable {
    //instance variable
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String status;//'Pending', 'Approved','Rejected'
    public  admin(String firstname, String lastname, String email,String password){
        this.firstname=firstname;
        this.lastname=lastname;
        this.email=email;
        this.password=password;
        this.status="Pending";
    }
    public String getFirstName(){
        return firstname;
    }
    public String getEmailAddress(){return email;}
    public String getPassword(){return password;}
    public String getStatus(){return status;}
    public void setStatus(String status){
        this.status=status;
    }


}
