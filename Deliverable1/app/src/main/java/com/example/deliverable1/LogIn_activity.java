package com.example.deliverable1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogIn_activity extends AppCompatActivity {
    EditText editTextTextEmailAddress2, editTextTextPassword;

    private FirebaseAuth mAuth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        editTextTextEmailAddress2 = findViewById(R.id.editTextTextEmailAddress2);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    //Check this
    public void onClickLogin(View view) {
        String email = editTextTextEmailAddress2.getText().toString();
        String password = editTextTextPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (task.isSuccessful()) {

                    if(currentUser!=null) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegistrationRequests");

                        //Patients
                        databaseReference.child("Patients").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String status = dataSnapshot.child("status").getValue(String.class);
                                    if (status.equals("Pending")) {
                                        Toast.makeText(getApplicationContext(), "Your registration request is Pending", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (status.equals("Rejected")) {
                                        Toast.makeText(getApplicationContext(), "Your registration request is rejected", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(status.equals("Approved")){
                                        //add some coding to transfer the user's information to the the user-patient nodes
                                        //and delete the registration request in the registration request-patient nodes
                                        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("Users").child("Patients");
                                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                                        String emailAddress = dataSnapshot.child("emailAddress").getValue(String.class);
                                        String password = dataSnapshot.child("password").getValue(String.class);
                                        String phNumber = dataSnapshot.child("phNumber").getValue(String.class);
                                        String address = dataSnapshot.child("address").getValue(String.class);
                                        String healthCardNumber = dataSnapshot.child("healthCardNumber").getValue(String.class);
                                        Patient patient = new Patient(firstName,lastName,emailAddress,password,phNumber,address,healthCardNumber);
                                        patientsRef.child(currentUser.getUid()).setValue(patient);
                                        dataSnapshot.getRef().removeValue();
                                        Toast.makeText(getApplicationContext(), "log in successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LogIn_activity.this, LogOff_activity.class);
                                        startActivity(intent);
                                    }
                                }
                                else{
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                    // Check if the user is in the "Users-Patients" node
                                    databaseReference.child("Users").child("Patients").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Toast.makeText(getApplicationContext(), "log in successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LogIn_activity.this, LogOff_activity.class);
                                                startActivity(intent);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Handle errors
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Handle errors
                            }
                        });

                        //Doctors
                        databaseReference.child("Doctors").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String status = dataSnapshot.child("status").getValue(String.class);
                                    if (status.equals("Pending")) {
                                        Toast.makeText(getApplicationContext(), "Your registration request is Pending", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (status.equals("Rejected")) {
                                        Toast.makeText(getApplicationContext(), "Your registration request is rejected", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(status.equals("Approved")){

                                        DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference("Users").child("Doctors").child(currentUser.getUid());

                                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                                        String emailAddress = dataSnapshot.child("emailAddress").getValue(String.class);
                                        String password = dataSnapshot.child("password").getValue(String.class);
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

                                        doctorsRef.child("firstName").setValue(firstName);
                                        doctorsRef.child("lastName").setValue(lastName);
                                        doctorsRef.child("emailAddress").setValue(emailAddress);
                                        doctorsRef.child("password").setValue(password);
                                        doctorsRef.child("phNumber").setValue(phNumber);
                                        doctorsRef.child("address").setValue(address);
                                        doctorsRef.child("employeeNumber").setValue(employeeNumber);
                                        doctorsRef.child("specialization").setValue(specializationList);
                                        dataSnapshot.getRef().removeValue();

                                        Toast.makeText(getApplicationContext(), "log in successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LogIn_activity.this, LogOff_activity.class);
                                        startActivity(intent);
                                    }
                                }
                                else{
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                    // Check if the user is in the "Users-Doctors" node
                                    databaseReference.child("Users").child("Doctors").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Toast.makeText(getApplicationContext(), "log in successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LogIn_activity.this, LogOff_activity.class);
                                                startActivity(intent);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Handle errors
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                            }
                        });

                        //Toast.makeText(getApplicationContext(), currentUser.getUid(), Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference("Users").child("Administrators").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Toast.makeText(getApplicationContext(), "log in successfully as an admin", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LogIn_activity.this, admin_activity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }
                else{
                    if(email.equals("Jane.doe@gmail.com") && password.equals("1234567890")){ //the admin
                        createAdminAccount();
                        Intent intent = new Intent(LogIn_activity.this, admin_activity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Authentication failed: your may enter the wrong email or password.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void createAdminAccount() {
        mAuth.createUserWithEmailAndPassword("Jane.doe@gmail.com", "1234567890")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Successfully created an admin account
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                String adminUid = currentUser.getUid();
                                DatabaseReference administratorRef = FirebaseDatabase.getInstance().getReference("Users").child("Administrators");

                                administratorRef.child(adminUid).child("email").setValue("Jane.doe@gmail.com");
                                administratorRef.child(adminUid).child("password").setValue("1234567890");
                                Toast.makeText(getApplicationContext(), "log in successfully as an admin", Toast.LENGTH_SHORT).show();

                                //Toast.makeText(getApplicationContext(), "Admin account created successfully", Toast.LENGTH_SHORT).show();
                                // Redirect to admin page or perform other actions
                            }
                        } else {
                            // Handle account creation failure
                            Toast.makeText(getApplicationContext(), "Admin account creation failed: your may enter the wrong email or password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
