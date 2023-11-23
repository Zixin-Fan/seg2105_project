package com.example.deliverable1;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Database {

    private final FirebaseAuth mAuth;

    public static FirebaseDatabase database;

    public Database(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public interface AddShiftCallback {
        void onAddShift(boolean addShift);
    }
//before we call the method, we should ensure the input are proper, which is the start time should be
//on the same day of the end time and should before the end time
//and we need shift page which show the shift(we should retrieve the shift from database to fill the listview)

    public void addShift(Calendar start, Calendar end, AddShiftCallback callback) {

        String startString = calendarToString(start);
        String endString = calendarToString(end);

        DatabaseReference shiftsRef = database.getReference("Schedule").child("Shifts");
        shiftsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean addShift = true;

                if (dataSnapshot != null && dataSnapshot.hasChildren()){
                    for (DataSnapshot shiftSnapshot : dataSnapshot.getChildren()) {
                        // Get the time for each shift
                        String retrieveStart = shiftSnapshot.child("startTime").getValue(String.class);
                        String retrieveEnd = shiftSnapshot.child("endTime").getValue(String.class);
                        Calendar retrieveStartCalendar = stringToCalendar(retrieveStart);
                        Calendar retrieveEndCalendar = stringToCalendar(retrieveEnd);

                        // if this shift is added before
                        if (doIntervalsOverlap(start, end, retrieveStartCalendar, retrieveEndCalendar)) { //doIntervalsOverlap(s1,e1,s2,e2)
                            addShift = false;
                        }

                    }
                }

                if(addShift==true){
                    DatabaseReference scheduleRef = database.getReference("Schedule");
                    //add shift under Shifts
                    DatabaseReference shiftRef = scheduleRef.child("Shifts"); //create a node schedule-shifts and get the reference
                    String newShiftEntryKey = shiftRef.push().getKey(); //get a unique key for the new shift like U47yvMaQATPeIE78UTNc581mhOF2
                    shiftRef.child(newShiftEntryKey).child("startTime").setValue(startString); //create a node time under the shift and add time
                    shiftRef.child(newShiftEntryKey).child("endTime").setValue(endString); //create a node time under the shift and add time
                    //shiftRef.child(newShiftEntryKey).child("Available status").setValue(status); //create a node available status and add a new available status

                    //add several pieces(30 minutes each) of the shift to appointments
                    DatabaseReference appointmentsRef = scheduleRef.child("Appointments"); //create a node schedule-shifts and get the reference
                    // Calculate three intervals like 8:00 - 8:30, 8:30 - 9:00, 9:00 - 9:30
                    List<Map<String, String>> intervals = calculateIntervals(start, end);

                    // Store each interval in the database
                    for (int i = 0; i < intervals.size(); i++) {
                        String newAppointmentEntryKey = shiftRef.push().getKey();
                        appointmentsRef.child("Appointments from shifts").child(newAppointmentEntryKey).setValue(intervals.get(i));
                        appointmentsRef.child("Appointments from shifts").child(newAppointmentEntryKey).child("Available status").setValue("True");
                    }
                }
                callback.onAddShift(addShift);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { //we should have this method when we use new ValueEventListener()
            }
        });
    }


    public interface AddAppointmentCallback {
        void onShiftFound(boolean appointmentAdded);
    }

//when the patient click one available appointment on the available appointment page, we can get the string of that item
//and split the string to two calendar as the inputs of this method
//String selectedItem = (String) parent.getItemAtPosition(position);
//the thing is you should retrieve the startTime and endTime from the appointment node if the status is true
//You can refer to the code in the inbox page (almost the same thing)
//we also need to create a page to let the doctor see the upcoming appointments, but he cannot cancel it

    public void addAppointment(Calendar start, Calendar end, AddAppointmentCallback callback) {

        String startString = calendarToString(start);
        String endString = calendarToString(end);

        DatabaseReference appFromShiftsRef = database.getReference("Schedule").child("Appointments").child("Appointments from shifts");
        appFromShiftsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean appointmentAdded = false;

                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {

                    String retrieveStart = appointmentSnapshot.child("startTime").getValue(String.class);
                    String retrieveEnd = appointmentSnapshot.child("endTime").getValue(String.class);


                    if (startString.equals(retrieveStart) && endString.equals(retrieveEnd)) {
                        DatabaseReference appointmentsRef = database.getReference("Schedule").child("Appointments").child("Upcoming appointments"); //create an appointment node under the schedule and get the reference
                        String newAppointmentEntryKey = appointmentsRef.push().getKey(); //get a unique key for the new appointment like U47yvMaQATPeIE78UTNc581mhOF2
                        appointmentsRef.child(newAppointmentEntryKey).child("startTime").setValue(startString);//add the appointment to upcoming appointments
                        appointmentsRef.child(newAppointmentEntryKey).child("endTime").setValue(endString);//add the appointment to upcoming appointments

                        appointmentSnapshot.getRef().child("Available status").setValue("False");//the recent available status becomes false
                        appointmentAdded = true;
                    }
                }

                callback.onShiftFound(appointmentAdded);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { //we should have this method when we use new ValueEventListener()
            }
        });
    }

    public interface CancelAppointmentCallback {
        void onIsCancelled(boolean isCancelled);
    }


//we should create another upcoming appointments page to let the patient know the upcoming appointments
//and we need to get the content of each item including two calendar(start and end) as inputs
//and when the patient click one item (means to cancel the appointment), the available status should be false and it will disappear immediately on the upcoming appointment page

    public void cancelAppointment(Calendar start, Calendar end, CancelAppointmentCallback callback){

        String startString = calendarToString(start);
        String endString = calendarToString(end);

        DatabaseReference AppFromShiftRef = database.getReference("Schedule").child("Appointments").child("Appointments from shifts");

        AppFromShiftRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean isCancelled = false;

                for(DataSnapshot AppSnapShot : snapshot.getChildren()){

                    String retrieveStart = AppSnapShot.child("startTime").getValue(String.class);
                    String retrieveEnd = AppSnapShot.child("endTime").getValue(String.class);
                    String availableStatus = AppSnapShot.child("Available status").getValue(String.class);

                    if(startString.equals(retrieveStart) && endString.equals(retrieveEnd) && availableStatus.equals("False")){
                        AppSnapShot.getRef().child("Available status").setValue("True");
                        isCancelled = true;
                    }
                }
                callback.onIsCancelled(isCancelled);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference upcomingRef = database.getReference("Schedule").child("Appointments").child("Upcoming appointments");

        upcomingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot AppSnapShot : snapshot.getChildren()){

                    String retrieveStart = AppSnapShot.child("startTime").getValue(String.class);
                    String retrieveEnd = AppSnapShot.child("endTime").getValue(String.class);

                    if(startString.equals(retrieveStart) && endString.equals(retrieveEnd)){
                        AppSnapShot.getRef().removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public interface deleteShiftCallback {
        void onShiftDeleted(boolean isDeleted);
    }

    //the doctor can delete the shift on shift page when he click one shift
    public void deleteShift(Calendar start, Calendar end, deleteShiftCallback callback){
        DatabaseReference scheduleRef = database.getReference("Schedule");
        DatabaseReference shiftsRef = scheduleRef.child("Shifts");

        doNotHaveAppointments(start, end, doNOtHaveAppointments -> {
            if(doNOtHaveAppointments){
                //delete shift
                String startString = calendarToString(start);
                String endString = calendarToString(end);
                shiftsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean onShiftDeleted = false;

                        if (dataSnapshot != null){
                            for (DataSnapshot shiftSnapshot : dataSnapshot.getChildren()) {

                                String retrieveStart = shiftSnapshot.child("startTime").getValue(String.class);
                                String retrieveEnd = shiftSnapshot.child("endTime").getValue(String.class);

                                if (startString.equals(retrieveStart) && endString.equals(retrieveEnd)) {
                                    shiftSnapshot.getRef().removeValue();
                                    onShiftDeleted = true;
                                    //delete appointments related to shift
                                    deleteShiftRelatedAppointment(start, end);
                                }
                            }
                        }
                        callback.onShiftDeleted(onShiftDeleted);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { //we should have this method when we use new ValueEventListener()
                    }
                });
            }
        });
    }

    public void deleteShiftRelatedAppointment(Calendar start, Calendar end){
        DatabaseReference scheduleRef = database.getReference("Schedule");
        DatabaseReference appointmentsRef = scheduleRef.child("Appointments").child("Appointments from shifts");
        List<Map<String, String>> intervals = calculateIntervals(start, end);

        for (int i = 0; i < intervals.size(); i++) {
            String startTime = intervals.get(i).get("startTime");
            String endTime = intervals.get(i).get("endTime");

            appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot != null && snapshot.hasChildren()){
                        for(DataSnapshot appFromShiftSnapshot : snapshot.getChildren()){
                            String retrieveStart = appFromShiftSnapshot.child("startTime").getValue(String.class);
                            String retrieveEnd = appFromShiftSnapshot.child("endTime").getValue(String.class);

                            if(startTime.equals(retrieveStart) && endTime.equals(retrieveEnd)){ //find the time and check whether there is an appointment at that time by the status
                                appFromShiftSnapshot.getRef().removeValue();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }



    //do not have time to change this method, will work on this after 3.30 pm today***********************************************
    //run this method whenever we open the schedule page or any other relevant pages to update the past appointments info
    public interface PastAppointmentCallback {
        void onUpdatePastAppointment(boolean isUpdated);
    }
    public void pastAppointment(PastAppointmentCallback callback){

        DatabaseReference upcomingAppointmentRef = database.getReference("Schedule").child("Appointments").child("Upcoming appointments");

        upcomingAppointmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean isUpdated = false;

                for(DataSnapshot upcomingAppointmentDS : snapshot.getChildren()){

                    String time = upcomingAppointmentDS.child("Time").getValue(String.class);

                    if(Integer.parseInt(time)<=20){ // 20 will be replaced with the realtime
                        DatabaseReference pastAppointmentsRef = database.getReference("Schedule").child("Appointments").child("Past appointments"); //create a past appointment node under the schedule and get the reference
                        String newAppointmentEntryKey = pastAppointmentsRef.push().getKey(); //get a unique key for the new appointment like U47yvMaQATPeIE78UTNc581mhOF2
                        pastAppointmentsRef.child(newAppointmentEntryKey).child("Time").setValue(time);//add the appointment to past appointments
                        upcomingAppointmentDS.getRef().removeValue();
                        isUpdated = true;
                    }
                }
                callback.onUpdatePastAppointment(isUpdated);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public interface DoctorCreationListener {
        void onDoctorCreationComplete(boolean success);
    }

    public void createDoctorAccount(String firstName, String lastName, String email, String password, String phNumber, String address, String employeeNumber, String speciality, DoctorCreationListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser doctor = mAuth.getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("RegistrationRequests").child("Doctors");
                String uid = Objects.requireNonNull(doctor).getUid();
                Doctor newDoc = new Doctor(firstName, lastName, email, password, phNumber, address, employeeNumber, speciality);
                usersRef.child(uid).setValue(newDoc);
                listener.onDoctorCreationComplete(true);
            } else {
                listener.onDoctorCreationComplete(false);
            }
        });
    }

    public interface PatientCreationListener {
        void onPatientCreationComplete(boolean success);
    }

    public void createPatientAccount(String firstName, String lastName, String email, String password, String phNumber, String address, String healthCardNumber, PatientCreationListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser patient = mAuth.getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("RegistrationRequests").child("Patients");
                String uid = Objects.requireNonNull(patient).getUid();
                Patient newPat = new Patient(firstName, lastName, email, password, phNumber, address, healthCardNumber);
                usersRef.child(uid).setValue(newPat);
                listener.onPatientCreationComplete(true);
            } else {
                listener.onPatientCreationComplete(false);
            }
        });
    }

    public static boolean doIntervalsOverlap(Calendar start1, Calendar end1, Calendar start2, Calendar end2) {
        return (start1.before(end2) && end1.after(start2)) ||
                (start2.before(end1) && end2.after(start1)) ||
                (start1.before(start2) && end1.after(end2)) ||
                (start2.before(start1) && end2.after(end1));
    }


    private String calendarToString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private Calendar stringToCalendar(String dateString) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            calendar.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
        return calendar;
    }

    private List<Map<String, String>> calculateIntervals(Calendar start, Calendar end) {
        List<Map<String, String>> intervals = new ArrayList<>();

        while (start.before(end)) {
            Calendar intervalEnd = (Calendar) start.clone();
            intervalEnd.add(Calendar.MINUTE, 30);

            Map<String, String> intervalMap = new HashMap<>();
            intervalMap.put("startTime", calendarToString(start));
            intervalMap.put("endTime", calendarToString(intervalEnd));

            intervals.add(intervalMap);

            start = intervalEnd;
        }

        return intervals;
    }

    public interface doNotHaveAppointmentsCallBack{
        void doNotHaveAppointments(boolean doNOtHaveAppointments);
    }

    public void doNotHaveAppointments(Calendar start, Calendar end, doNotHaveAppointmentsCallBack callback){
        DatabaseReference scheduleRef = database.getReference("Schedule");
        DatabaseReference appointmentsRef = scheduleRef.child("Appointments").child("Appointments from shifts");

        List<Map<String, String>> intervals = calculateIntervals(start, end);

        for (int i = 0; i < intervals.size(); i++) {
            String startTime = intervals.get(i).get("startTime");
            String endTime = intervals.get(i).get("endTime");

            appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    boolean doNOtHaveAppointments = true;

                    if (snapshot != null && snapshot.hasChildren()){
                        for(DataSnapshot appFromShiftSnapshot : snapshot.getChildren()){
                            String retrieveStart = appFromShiftSnapshot.child("startTime").getValue(String.class);
                            String retrieveEnd = appFromShiftSnapshot.child("endTime").getValue(String.class);
                            String availableStatus = appFromShiftSnapshot.child("Available status").getValue(String.class);

                            if(startTime.equals(retrieveStart) && endTime.equals(retrieveEnd) && availableStatus.equals("False")){ //find the time and check whether there is an appointment at that time by the status
                                doNOtHaveAppointments = false;
                            }

                        }
                    }
                    callback.doNotHaveAppointments(doNOtHaveAppointments);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}

