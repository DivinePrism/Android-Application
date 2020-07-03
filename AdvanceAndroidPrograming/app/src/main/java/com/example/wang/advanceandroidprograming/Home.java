package com.example.wang.advanceandroidprograming;

import java.util.Date;
import java.util.UUID;
/*
getters and setters to obtain the name,serial, and value for the item component
 */
public class Home {

    private UUID mId;
    private static Date mDate;
    private String mName;
    private String mSerial;
    private int mValue;

    /*
    Constructer to inalize the name,serial, and value
     */
    public Home(){
      this(UUID.randomUUID());
    }

    public Home(UUID uuid) {
        mId = uuid;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSerial() {
        return mSerial;
    }

    public void setSerial(String serial) {
        mSerial = serial;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        mValue = value;
    }

    public static Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
