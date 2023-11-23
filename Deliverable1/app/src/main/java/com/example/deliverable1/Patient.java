package com.example.deliverable1;

public class Patient extends User {
    private String healthCardNumber;

    public Patient(String firstName, String lastName, String emailAddress, String password, String phNumber, String Address, String healthCardNumber){
        super(firstName, lastName, emailAddress, password, phNumber,Address);
        this.healthCardNumber = healthCardNumber;
    }

    public String getHealthCardNumber() {

        return healthCardNumber;
    }
    public void setHealthCardNumber(String HCN) {

        this.healthCardNumber= HCN ;
    }
}