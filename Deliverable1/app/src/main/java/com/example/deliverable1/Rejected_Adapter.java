package com.example.deliverable1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Rejected_Adapter extends RecyclerView.Adapter<Rejected_Adapter.MyViewHolder> {

    Context context;
    ArrayList<User> rejectedList;
    OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener ( OnItemClickListener listener){
        this.listener=listener;
    }

    public Rejected_Adapter(Context context, ArrayList<User> rejectedList) {
        this.context = context;
        this.rejectedList=rejectedList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rejection_view,parent,false);

        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user= rejectedList.get(position);
        holder.firstName.setText(user.getFirstName());
        holder.lastName.setText(user.getLastName());
        holder.email.setText(user.getEmailAddress());



    }

    @Override
    public int getItemCount() {
        return rejectedList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView firstName, lastName, email;
        Button acceptButton;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            firstName= itemView.findViewById(R.id.firstName);
            lastName =itemView.findViewById(R.id.lastName);
            email =itemView.findViewById(R.id.email);
            acceptButton= itemView.findViewById(R.id.acceptButton);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(getAdapterPosition());

                }
            });
        }
    }
}
