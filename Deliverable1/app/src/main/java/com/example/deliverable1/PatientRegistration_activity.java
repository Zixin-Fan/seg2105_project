package com.example.deliverable1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Patterns;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


public class PatientRegistration_activity extends AppCompatActivity  {
    TextInputEditText editFirstName, editLastName, editPassword,editPasswordConfirmation, editPhone, editAddress, editHealthCardNumber, editEmail;
    Button submitButton,passwordInfoButton;
    CheckBox passwordcheckBox;
    private Database dB;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_form);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editPassword = findViewById(R.id.editPassword);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editAddress = findViewById(R.id.editAddress);
        editHealthCardNumber = findViewById(R.id.editHealthCardNumber);
        submitButton = findViewById(R.id.submitButton);
        editPasswordConfirmation = findViewById(R.id.editPasswordConfirmation);
        passwordInfoButton = findViewById(R.id.passwordInfoButton);
        passwordcheckBox = findViewById(R.id.passwordcheckBox);

        //database
        dB=new Database();

        submitButton.setOnClickListener(v -> Validation());

        passwordInfoButton.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "The password needs to be a minimum of 8 characters", Toast.LENGTH_SHORT).show());
    }
    public void Validation() {
        //Checks for All valid Inputs
        if (nameValid() && emailValid() && phoneValid() && passwordValid() && addressValid() && healthcareCardValid()) {
            //collect valid inputs
            String firstname=getStringFromEditText(editFirstName);
            String lastname=getStringFromEditText(editLastName);
            String email=getStringFromEditText(editEmail);
            String password=getStringFromEditText(editPassword);
            String phone=getStringFromEditText(editPhone);
            String address=getStringFromEditText(editAddress);
            String HCN=getStringFromEditText(editHealthCardNumber);

            //create patient Account in Database
            dB.createPatientAccount(firstname, lastname, email, password, phone, address, HCN, new Database.PatientCreationListener() {
                @Override
                public void onPatientCreationComplete(boolean success) {
                    if (success) {
                        Toast.makeText(getApplicationContext(), "We will process your registration request as soon as possible and you can try to log in later.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PatientRegistration_activity.this, LogIn_activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "This email has been registered.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    //USED TO CONVERT INPUTS FROM TEXT FIELDS TO STRINGS TAKING INTO ACCOUNT NULL EXCEPTION
    private String getStringFromEditText(TextInputEditText editText) {
        if (editText != null && editText.getText() != null) {
            return editText.getText().toString();
        } else {
            return "";
        }
    }
    // VALID INPUTS
    //checks for valid name
    private boolean nameValid(){
        // Check for a valid first name.
        if (getStringFromEditText(editFirstName).isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your first name", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check for a valid last name.
        if (getStringFromEditText(editLastName).isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your last name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // Check for a valid address.
    private boolean addressValid(){

        if (getStringFromEditText(editAddress).isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
            return false;
        } else  {
            return true;
        }
    }
    // Check for a valid phone number.
    private boolean phoneValid(){
        if (getStringFromEditText(editPhone).isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
            return false;
        } else  {
            return true;
        }
    }

    // Check for valid HealthCardNumber
    private boolean healthcareCardValid(){
        if (getStringFromEditText(editHealthCardNumber).isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your health card number", Toast.LENGTH_SHORT).show();
            return false;
        } else  {
            return true;
        }
    }

    // Check for a valid email address.
    private boolean emailValid(){
        if (getStringFromEditText(editEmail).isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(getStringFromEditText(editEmail)).matches()) {
            Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        } else  {
            return true;
        }
    }

    // Check for a valid password.
    private boolean passwordValid(){
        if (getStringFromEditText(editPassword).isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (getStringFromEditText(editPassword).length() < 8) {
            Toast.makeText(getApplicationContext(), "Please enter a minimum of 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!getStringFromEditText(editPassword).equals(getStringFromEditText(editPasswordConfirmation))) {
            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        } else  {
            return true;
        }
    }

}