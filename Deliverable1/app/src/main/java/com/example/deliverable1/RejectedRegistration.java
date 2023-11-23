package com.example.deliverable1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RejectedRegistration extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    Rejected_Adapter adapter;
    ArrayList<User> rejectedList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_registration);

        recyclerView = findViewById(R.id.recyclerview);
        database = FirebaseDatabase.getInstance().getReference("RegistrationRequests");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rejectedList = new ArrayList<>();
        adapter= new Rejected_Adapter(this,rejectedList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new Rejected_Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                rejectedList.get(position).setStatus("Approved");
                adapter.notifyItemChanged(position);

                rejectedList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        database.orderByChild("status").equalTo("Rejected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DataSnapshot patients = dataSnapshot.child("Patients");
                    DataSnapshot doctors = dataSnapshot.child("Doctors");
                    if(patients.exists()){
                        User user = patients.getValue(User.class);
                        rejectedList.add(user);
                    }
                    if (doctors.exists()){
                        User user= doctors.getValue(User.class);
                        rejectedList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}