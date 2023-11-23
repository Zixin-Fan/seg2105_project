package com.example.deliverable1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private int resourceId;
    public UserAdapter(Context context,
                       int textViewResourceId,
                       List<User> object){
        super(context,textViewResourceId,object);
        resourceId=textViewResourceId;
    }
    public View getview(int position, View covweView, ViewGroup parent){
        User user = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView UserName = (TextView) view.findViewById(R.id.User_name);
        UserName.setText(user.getFirstName()+user.getLastName());
        return view;
    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
