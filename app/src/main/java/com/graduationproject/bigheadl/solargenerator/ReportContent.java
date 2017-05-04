package com.graduationproject.bigheadl.solargenerator;

import android.graphics.Picture;

import java.io.File;

/**
 * Created by lshbi on 5/3/2017.
 */

public class ReportContent {
    private String username;
    private String device;
    private String content;
    private int drawable;

    public void setUsername(String user){
        username = user;
    }

    public void setDevice(String dev){
        device = dev;
    }

    public void setContent(String con){
        content = con;
    }

    public void setDrawable(int draw){
        drawable = draw;
    }

    public String getUsername(){
        return username;
    }

    public String getDevice(){
        return device;
    }

    public String getContent(){
        return content;
    }

    public int getDrawable(){
        return drawable;
    }

    public ReportContent(String username,String device,String content,int drawable){
        this.username = username;
        this.device = device;
        this.content = content;
        this.drawable = drawable;
    }

}
