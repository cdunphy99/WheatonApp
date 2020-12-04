package com.wheaton.wheatonapp;

import android.os.Parcel;
import android.os.Parcelable;

public class StickyNoteObject implements Parcelable {
    private String Title;
    private String Msg;
    private String docId;

    public StickyNoteObject(){
        setTitle("");
        setMsg("");
    }
    public StickyNoteObject(String t, String m){
        setTitle(t);
        setMsg(m);
    }

    public StickyNoteObject(String t, String m, String id){
        setTitle(t);
        setMsg(m);
        docId = id;
    }

    protected StickyNoteObject(Parcel in) {
        Title = in.readString();
        Msg = in.readString();
    }

    public static final Creator<StickyNoteObject> CREATOR = new Creator<StickyNoteObject>() {
        @Override
        public StickyNoteObject createFromParcel(Parcel in) {
            return new StickyNoteObject(in);
        }

        @Override
        public StickyNoteObject[] newArray(int size) {
            return new StickyNoteObject[size];
        }
    };

    //getters
    public String getMsg() {
        return Msg;
    }
    public String getTitle() {
        return Title;
    }

    //setters
    public void setMsg(String msg) {
        Msg = msg;
    }

    public void setTitle(String title) {
        Title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Title);
        parcel.writeString(Msg);
    }
}
