package com.example.deliverable1;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class LogOff_activity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_off);

        TextView welcome = findViewById(R.id.PatienttextView);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser!=null){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child("Patients").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        // The user exists in the "patients" path, indicating they are a patient.
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        welcome.setText("Hello "+firstName+", you are Logged in as patient");
                        welcome.setVisibility(View.VISIBLE);
                    }
                    else {
                        // The user doesn't exist in the "patients" path, so they might be a doctor.
                        databaseReference.child("Doctors").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // The user exists in the "doctors" path, indicating they are a doctor.
                                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                                    welcome.setText("Hello " + firstName + ", you are Logged in as doctor");
                                    welcome.setVisibility(View.VISIBLE);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error){}
                        });
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }

    //
    public void onClickLogoff(View view){
        Intent intent;
        intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }

}
