package com.example.deliverable1;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InboxPageActivity extends AppCompatActivity {
    private List<String> userList;
    private ArrayAdapter<String> adapter;

    Button approveButton;

    Button rejectionPageButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_page);
        userList = new ArrayList<>();

        approveButton = findViewById(R.id.approveAllButton);

        rejectionPageButton = findViewById(R.id.rejectionPageButton);

        rejectionPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InboxPageActivity.this, admin_activity.class);
                startActivity(intent);
            }
        });



        // Create a reference to the Firebase Realtime Database
        DatabaseReference resReference = FirebaseDatabase.getInstance().getReference("RegistrationRequests");

        // Query the "Doctors" node to get pending doctors
        DatabaseReference doctorsReference = resReference.child("Doctors");
        doctorsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()) {
                    String doctorId = doctorSnapshot.getKey(); // Get the unique ID of the doctor
                    String status = doctorSnapshot.child("status").getValue(String.class);

                    // Check if the doctor is not approved
                    if ("Pending".equals(status)) {
                        // Add the Doctor object to the userList
                        userList.add("Doctor registration: "+doctorId);
                    }
                }

                // Update the ListView
                updateListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

        // Query the "Patients" node to get pending patients
        DatabaseReference patientsReference = resReference.child("Patients");
        patientsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()) {
                    String patientId = doctorSnapshot.getKey(); // Get the unique ID of the doctor
                    String status = doctorSnapshot.child("status").getValue(String.class);

                    // Check if the doctor is not approved
                    if ("Pending".equals(status)) {
                        // Add the Doctor object to the userList
                        userList.add("Patient registration: "+patientId);
                    }
                }

                // Update the ListView
                updateListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

        Toast.makeText(getApplicationContext(), "You can see details by clicking the items", Toast.LENGTH_SHORT).show();

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("RegistrationRequests");

                userRef.child("Doctors").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot docSnapshot : dataSnapshot.getChildren()) {
                            String aStatus = docSnapshot.child("status").getValue(String.class);
                            if(aStatus.equals("Pending")){
                                docSnapshot.getRef().child("status").setValue("Approved");
                            }
                        }
                        updateListView();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                    }
                });

                userRef.child("Patients").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot patSnapshot : dataSnapshot.getChildren()) {
                            String aStatus = patSnapshot.child("status").getValue(String.class);
                            if(aStatus.equals("Pending")){
                                patSnapshot.getRef().child("status").setValue("Approved");
                            }
                        }
                        updateListView();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                    }
                });

                userList.clear();

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Successfully approve all", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void updateListView() {
        // Assuming you have a custom adapter (UserAdapter) for your ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);

        // Assuming your ListView has an id "list_view"
        ListView listView = findViewById(R.id.list_view);

        // Set the adapter to your ListView
        listView.setAdapter(adapter);

        // Set item click listener
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String uid = userList.get(position);
            //Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_SHORT).show();


            // Create an Intent to start the InformationPageActivity
            Intent intent = new Intent(InboxPageActivity.this, InformationPageActivity.class);

            // Pass the UID to the InformationPageActivity using the intent
            intent.putExtra("uid", uid);

            // Start the InformationPageActivity
            startActivity(intent);
        });
    }
}
