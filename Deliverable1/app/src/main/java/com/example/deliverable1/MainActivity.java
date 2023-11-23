package com.example.deliverable1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_signin_or_log_in);

//test database

        Database schedule = new Database();

/* works
    //add shifts and get results

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, 2023);
        calendar1.set(Calendar.MONTH, Calendar.JANUARY);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        calendar1.set(Calendar.HOUR_OF_DAY, 8);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 2023);
        calendar2.set(Calendar.MONTH, Calendar.JANUARY);
        calendar2.set(Calendar.DAY_OF_MONTH, 1);
        calendar2.set(Calendar.HOUR_OF_DAY, 10);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);

        schedule.addShift(calendar1, calendar2, addShift -> {
            if (addShift) {
                Toast.makeText(getApplicationContext(), "successfully add a shift", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "failed to add a shift", Toast.LENGTH_SHORT).show();
            }
        });


 */

/* works
    // add appointments and get the results
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.YEAR, 2023);
        calendar3.set(Calendar.MONTH, Calendar.JANUARY);
        calendar3.set(Calendar.DAY_OF_MONTH, 1);
        calendar3.set(Calendar.HOUR_OF_DAY, 8);
        calendar3.set(Calendar.MINUTE, 0);
        calendar3.set(Calendar.SECOND, 0);

        Calendar calendar4 = Calendar.getInstance();
        calendar4.set(Calendar.YEAR, 2023);
        calendar4.set(Calendar.MONTH, Calendar.JANUARY);
        calendar4.set(Calendar.DAY_OF_MONTH, 1);
        calendar4.set(Calendar.HOUR_OF_DAY, 8);
        calendar4.set(Calendar.MINUTE, 30);
        calendar4.set(Calendar.SECOND, 0);

        schedule.addAppointment(calendar3, calendar4, appointmentAdded -> {

            if (appointmentAdded) {
                Toast.makeText(getApplicationContext(), "successfully add an appointment", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "failed to add an appointment", Toast.LENGTH_SHORT).show();
            }
        });

*/



/* works
    //cancel appointments and get results

        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.YEAR, 2023);
        calendar3.set(Calendar.MONTH, Calendar.JANUARY);
        calendar3.set(Calendar.DAY_OF_MONTH, 1);
        calendar3.set(Calendar.HOUR_OF_DAY, 8);
        calendar3.set(Calendar.MINUTE, 0);
        calendar3.set(Calendar.SECOND, 0);

        Calendar calendar4 = Calendar.getInstance();
        calendar4.set(Calendar.YEAR, 2023);
        calendar4.set(Calendar.MONTH, Calendar.JANUARY);
        calendar4.set(Calendar.DAY_OF_MONTH, 1);
        calendar4.set(Calendar.HOUR_OF_DAY, 8);
        calendar4.set(Calendar.MINUTE, 30);
        calendar4.set(Calendar.SECOND, 0);
        schedule.cancelAppointment(calendar3, calendar4, isCancelled -> {
                if (isCancelled) {
                    Toast.makeText(getApplicationContext(), "successfully cancel", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You don't have an appointment at that time", Toast.LENGTH_SHORT).show();
                }

        });

 */

/* works
    //do not have appointment in the interval
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.YEAR, 2023);
        calendar3.set(Calendar.MONTH, Calendar.JANUARY);
        calendar3.set(Calendar.DAY_OF_MONTH, 1);
        calendar3.set(Calendar.HOUR_OF_DAY, 8);
        calendar3.set(Calendar.MINUTE, 0);
        calendar3.set(Calendar.SECOND, 0);

        Calendar calendar4 = Calendar.getInstance();
        calendar4.set(Calendar.YEAR, 2023);
        calendar4.set(Calendar.MONTH, Calendar.JANUARY);
        calendar4.set(Calendar.DAY_OF_MONTH, 1);
        calendar4.set(Calendar.HOUR_OF_DAY, 8);
        calendar4.set(Calendar.MINUTE, 30);
        calendar4.set(Calendar.SECOND, 0);

    schedule.doNotHaveAppointments(calendar3, calendar4, doNotHaveAppointments -> {
        if(doNotHaveAppointments){
            Toast.makeText(getApplicationContext(), "doNotHaveAppointments", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "haveAppointments", Toast.LENGTH_SHORT).show();
        }
    });

 */

/*works
    //delete a shift and get results

        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.YEAR, 2023);
        calendar3.set(Calendar.MONTH, Calendar.JANUARY);
        calendar3.set(Calendar.DAY_OF_MONTH, 1);
        calendar3.set(Calendar.HOUR_OF_DAY, 8);
        calendar3.set(Calendar.MINUTE, 0);
        calendar3.set(Calendar.SECOND, 0);

        Calendar calendar4 = Calendar.getInstance();
        calendar4.set(Calendar.YEAR, 2023);
        calendar4.set(Calendar.MONTH, Calendar.JANUARY);
        calendar4.set(Calendar.DAY_OF_MONTH, 1);
        calendar4.set(Calendar.HOUR_OF_DAY, 10);
        calendar4.set(Calendar.MINUTE, 0);
        calendar4.set(Calendar.SECOND, 0);

        schedule.deleteShift(calendar3, calendar4, isDeleted -> {
            if(isDeleted){
                Toast.makeText(getApplicationContext(),"successfully delete a shift", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"This shift doesn't exist or has an appointment", Toast.LENGTH_SHORT).show();
            }
        });

 */

//do not have time to change this method, will work on this after 3.30 pm today
    /*
        schedule.pastAppointment(isUpdated -> {
            if(isUpdated){
                Toast.makeText(getApplicationContext(),"past appointments updated", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"the past appointments remain unchanged", Toast.LENGTH_SHORT).show();
            }
        });

     */
    }


    //switch to DoctorOrPatient_activity
    public void onClickRegister(View view){
        Intent intent = new Intent (this, DoctorOrPatient_activity.class);
        startActivity(intent);
    }


    //switch to LogIn_activity
    public void onClickLogIn(View view){
        Intent intent = new Intent (this, LogIn_activity.class);
        startActivity(intent);
    }


}
