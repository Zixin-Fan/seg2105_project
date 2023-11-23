package com.example.deliverable1;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.util.Patterns;
import android.os.Bundle;
import android.widget.Button;


import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class DoctorRegistration_activity extends AppCompatActivity {

    TextInputEditText editFirstName, editLastName, editPassword,editPasswordConfirmation, editPhone, editAddress, editEmployeeNumber, editEmail, editSpecialization;
    Button submitButton , accountPasswordButton , specialitiesButton;

    private Database dB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.doctor_form);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editPassword = findViewById(R.id.editPassword);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editAddress = findViewById(R.id.AddressEditText);
        editEmployeeNumber = findViewById(R.id.employeenumberEditText);
        editSpecialization = findViewById(R.id.editspecializations);
        submitButton = findViewById(R.id.submitButton);
        accountPasswordButton = findViewById(R.id.accountpasswordButton);
        specialitiesButton = findViewById(R.id.specialtiesButton);
        editPasswordConfirmation = findViewById(R.id.editPasswordConfirmation);

        //database
        dB=new Database();

        accountPasswordButton.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "The password needs to be a minimum of 8 characters", Toast.LENGTH_SHORT).show());
        specialitiesButton.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Enter each specialization separated by a comma ", Toast.LENGTH_SHORT).show());
        submitButton.setOnClickListener(v -> Validation());


    }

    public void Validation() {

        //Checks for All valid inputs
        if (nameValid() && emailValid() && phoneValid() && passwordValid() && addressValid() && employeeNumberValid()) {
            //collect valid inputs
            String firstname=getStringFromEditText(editFirstName);
            String lastname=getStringFromEditText(editLastName);
            String email=getStringFromEditText(editEmail);
            String password=getStringFromEditText(editPassword);
            String phone=getStringFromEditText(editPhone);
            String address=getStringFromEditText(editAddress);
            String employeeNumber=getStringFromEditText(editEmployeeNumber);
            String specialization=getStringFromEditText(editSpecialization);
            //create doctor Account in Database
            dB.createDoctorAccount(firstname, lastname, email, password, phone, address, employeeNumber, specialization, new Database.DoctorCreationListener() {
                @Override
                public void onDoctorCreationComplete(boolean success) {
                    if (success) {
                        Toast.makeText(getApplicationContext(), "We will process your registration request as soon as possible and you can try to log in later.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorRegistration_activity.this, LogIn_activity.class);
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

    // Check for employee number
    private boolean employeeNumberValid(){
        if (getStringFromEditText(editEmployeeNumber).isEmpty()) {
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


