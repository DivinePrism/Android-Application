package com.example.wang.advanceandroidprograming.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.wang.advanceandroidprograming.Home;
import static com.example.wang.advanceandroidprograming.Database.HomeDbSchema.HomeTable.*;


import java.util.Date;
import java.util.UUID;

public class HomeCursorWrapper extends CursorWrapper {


        public HomeCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Home getHome() {
            String uuidString = getString(getColumnIndex(Cols.UUID));
            String name = getString(getColumnIndex(Cols.NAME));
            long date = getLong(getColumnIndex(Cols.DATE));
            int value = getInt(getColumnIndex(Cols.VALUE));
            String serial =getString(getColumnIndex(Cols.SERIAL));

            Home home = new Home(UUID.fromString(uuidString));
            home.setName(name);
            home.setDate(new Date(date));
            home.setValue(value);
            home.setSerial(serial);


            return home;
        }

}
