package com.example.deliverable1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.cketti.mailto.EmailIntentBuilder;



public class rejectItemInfoActivity extends AppCompatActivity {
    private memory memo;
    TextView firstName, lastName, email, phone, address, healthCardNumber, healthCardTag, employeeNumTag, specializationTag, employeeNumber, specialization, doctorInfo, patientInfo;
    Button approveButton, rejectButton;
    Patient patient;
    Doctor doctor;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reject_information_page);
        String uid = getIntent().getStringExtra("uid");
        String[] uId= uid.split(":");
        userId = uId[1].trim();
        approveButton = findViewById(R.id.approveButton);


        //Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_SHORT).show();

        TextView patientInfo = findViewById(R.id.patientInfo);
        TextView doctorInfo = findViewById(R.id.doctorInfo);
        TextView firstNameTextView = findViewById(R.id.firstName);
        TextView lastNameTextView = findViewById(R.id.lastName);
        TextView phoneTextView = findViewById(R.id.phone);
        TextView emailTextView = findViewById(R.id.email);
        TextView addressTextView = findViewById(R.id.address);
        TextView healthCardNumber = findViewById(R.id.healthCardNumber);
        TextView employeeNumberTextView = findViewById(R.id.employeeNumber);
        TextView specialization = findViewById(R.id.specialization);
        TextView healthCardTag = findViewById(R.id.healthCardTag);
        TextView employeeNumTag = findViewById(R.id.employeeNumberTag);
        TextView specializationTag = findViewById(R.id.specializationTag);

        //Toast.makeText(getApplicationContext(), userId, Toast.LENGTH_SHORT).show();


        DatabaseReference regReference = FirebaseDatabase.getInstance().getReference("RegistrationRequests");
        regReference.child("Patients").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve user data from the snapshot
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String phone = dataSnapshot.child("phNumber").getValue(String.class);
                    String email = dataSnapshot.child("emailAddress").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String hcn = dataSnapshot.child("healthCardNumber").getValue(String.class);

                    patientInfo.setVisibility(View.VISIBLE);
                    healthCardTag.setVisibility(View.VISIBLE);
                    healthCardNumber.setVisibility(View.VISIBLE);
                    firstNameTextView.setText(firstName);
                    lastNameTextView.setText(lastName);
                    phoneTextView.setText(phone);
                    emailTextView.setText(email);
                    addressTextView.setText(address);
                    healthCardNumber.setText(hcn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

        regReference.child("Doctors").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // Retrieve user data from the snapshot
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String emailAddress = dataSnapshot.child("emailAddress").getValue(String.class);
                    String phNumber = dataSnapshot.child("phNumber").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String employeeNumber = dataSnapshot.child("employeeNumber").getValue(String.class);
                    DataSnapshot specializationSnapshot = dataSnapshot.child("specialization");

                    // Retrieve child values
                    List<String> specializationList = new ArrayList<>();
                    for (DataSnapshot childSnapshot : specializationSnapshot.getChildren()) {
                        String specializationValue = childSnapshot.getValue(String.class);
                        specializationList.add(specializationValue);
                    }

                    doctorInfo.setVisibility(View.VISIBLE);
                    employeeNumTag.setVisibility(View.VISIBLE);
                    specializationTag.setVisibility(View.VISIBLE);
                    employeeNumberTextView.setVisibility(View.VISIBLE);
                    specialization.setVisibility(View.VISIBLE);
                    firstNameTextView.setText(firstName);
                    lastNameTextView.setText(lastName);
                    phoneTextView.setText(phNumber);
                    emailTextView.setText(emailAddress);
                    addressTextView.setText(address);
                    employeeNumberTextView.setText(employeeNumber);
                    specialization.setText(specializationList.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });



        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference regReference = FirebaseDatabase.getInstance().getReference("RegistrationRequests");
                regReference.child("Patients").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            dataSnapshot.getRef().child("status").setValue("Approved");
                            Intent intent = new Intent(rejectItemInfoActivity.this, rejectItemActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                regReference.child("Doctors").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            dataSnapshot.getRef().child("status").setValue("Approved");
                            Intent intent = new Intent(rejectItemInfoActivity.this, rejectItemActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error){
                    }
                });

                Toast.makeText(getApplicationContext(), "Approved", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void onClickRejectionList(View view){
        Intent intent = new Intent(rejectItemInfoActivity.this, rejectItemActivity.class);
        startActivity(intent);
    }

}


