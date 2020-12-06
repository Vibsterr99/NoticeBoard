package com.project.noticeboard;
import java.sql.Time;
public class post {

    private String mname;
    private Time mtime;
    private String mnotice;
    private int mimage;

    public post(String name,Time time,String notice,int image){
        mname=name;
        mtime=time;
        mnotice=notice;
        mimage=image;
    }
    public String getName(){
        return mname;
    }
    public Time getTime(){
        return mtime;
    }
    public String getNotice(){
        return mnotice;
    }
    public int getImage(){
        return mimage;
    }
}