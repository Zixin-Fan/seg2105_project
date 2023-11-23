package com.example.deliverable1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class DoctorOrPatient_activity extends AppCompatActivity {
    private memory memo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_or_patient);

        Intent intent = getIntent();
        if (intent.getSerializableExtra("memorykey") != null) {
            memo = (memory) intent.getSerializableExtra("memorykey");
        }
    }
    public void onClickPatient(View view) {
        Intent intent = new Intent(this, PatientRegistration_activity.class);
        intent.putExtra("memorykey", (Serializable) memo);//pass to next page
        startActivity(intent);
    }

        public void onClickDoctor(View view){
        Intent intent = new Intent (this, DoctorRegistration_activity.class);
        intent.putExtra("memorykey", (Serializable) memo);//pass to next page
        startActivity(intent);
    }
}
