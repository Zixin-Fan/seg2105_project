package com.example.deliverable1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class admin_activity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
    }

    public void onClickInbox(View view){
        Intent intent = new Intent (this, InboxPageActivity.class);
        startActivity(intent);
    }


    //switch to LogIn_activity
    public void onClickRejection(View view){
        Intent intent = new Intent (this, rejectItemActivity.class);
        startActivity(intent);
    }

    public void onClickLogoff(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
