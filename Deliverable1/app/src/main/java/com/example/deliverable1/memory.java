package com.example.deliverable1;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class memory implements Serializable {
    //used to store username of doctor and patient
    private ArrayList<String> emails;
    private LinkedList<User> info;
    private ArrayList<String> admin_emails;
    private LinkedList<admin> administration ;
    public memory(){
        info=new LinkedList<User>();
        emails=new ArrayList<String>();
        administration=new LinkedList<admin>();
        admin_emails=new ArrayList<String>();

    }
    public void add(User p){
        emails.add(p.getEmailAddress());
        info.add(p);
    }
    public void add(admin a){
        admin_emails.add(a.getEmailAddress());
        administration.add(a);
    }
    //check list using email address and returns corresponding user info
    public User findUser (String email){
        if (emails.contains(email) ){//checks if list have corresponding email
            int ind=emails.indexOf(email);//if yes get corresponding index
            return info.get(ind);//return info ad corresponding index
        }else{return null;}
    }
    public admin findAdmin(String email){
        if (admin_emails.contains(email) ){//checks if list have corresponding email
            int ind=admin_emails.indexOf(email);//if yes get corresponding index
            return administration.get(ind);//return info ad corresponding index
        }else{return null;}
    }

}
