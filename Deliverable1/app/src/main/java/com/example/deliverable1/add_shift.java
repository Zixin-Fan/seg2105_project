package com.example.deliverable1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class add_shift extends AppCompatActivity {

    private DatePicker datePicker;
    private Spinner startTimeSpinner;
    private Spinner endTimeSpinner;
    private Button addShiftButton;

    Database schedule = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_shift_page);

        // Initialize views
        datePicker = findViewById(R.id.shiftDatePicker);
        startTimeSpinner = findViewById(R.id.shiftStartTimeSpinner);
        endTimeSpinner = findViewById(R.id.shiftEndTimeSpinner);
        addShiftButton = findViewById(R.id.addShift);

        // Set click listener for the button
        addShiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve selected date from DatePicker
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                // Retrieve selected time from Spinners
                String startTime = startTimeSpinner.getSelectedItem().toString();
                String endTime = endTimeSpinner.getSelectedItem().toString();

                // Create Calendar instances for start and end times
                Calendar startCalendar = Calendar.getInstance();
                Calendar endCalendar = Calendar.getInstance();

                // Set the selected date and time for startCalendar
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, month);
                startCalendar.set(Calendar.DAY_OF_MONTH, day);

                // Parse the start time strings to get hours and minutes
                int startHour = Integer.parseInt(startTime.split(":")[0]);
                int startMinute = Integer.parseInt(startTime.split(":")[1].split(" ")[0]);

                // Set the hours and minutes for startCalendar
                startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
                startCalendar.set(Calendar.MINUTE, startMinute);

                // Set the selected date and time for endCalendar
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, month);
                endCalendar.set(Calendar.DAY_OF_MONTH, day);

                // Parse the end time strings to get hours and minutes
                int endHour = Integer.parseInt(endTime.split(":")[0]);
                int endMinute = Integer.parseInt(endTime.split(":")[1].split(" ")[0]);

                // Set the hours and minutes for endCalendar
                endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
                endCalendar.set(Calendar.MINUTE, endMinute);

                schedule.addShift(startCalendar, endCalendar, addShift -> {
                    if (addShift) {
                        Toast.makeText(getApplicationContext(), "successfully add a shift", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "failed to add a shift", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
