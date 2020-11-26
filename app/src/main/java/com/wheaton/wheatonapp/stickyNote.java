package com.wheaton.wheatonapp;

import android.view.View;

public class stickyNote {
    private String title;
    private String text;

    public stickyNote(String t, String e){
        this.title = t;
        this.text = e;
    }
    public stickyNote(){
        this.title = "";
        this.text = "";
    }

    public String getText(){return this.text;}
    public String getTitle(){return this.title;}

    public void setText(String t){this.text = t;}
    public void setTitle(String t){this.title = t;}



}
