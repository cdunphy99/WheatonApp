package com.wheaton.wheatonapp;

import android.os.Parcel;
import android.os.Parcelable;

public class StickyNoteObject implements Parcelable {
    private String Title;
    private String Msg;
    private String docId;
    private String url;

    public StickyNoteObject() {
        setTitle("");
        setMsg("");
    }

    public StickyNoteObject(String t, String m) {
        setTitle(t);
        setMsg(m);
    }

    public StickyNoteObject(String t, String m, String id, String site) {
        setTitle(t);
        setMsg(m);
        docId = id;
        url = site;
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

    public String getDocId() {
        return docId;
    }

    public String getUrl() {
        return url;
    }

    //setters
    public void setMsg(String msg) {
        Msg = msg;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDocId(String docId) {
        this.docId = docId;
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
