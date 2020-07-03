package com.example.wang.advanceandroidprograming;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wang.advanceandroidprograming.Database.HomeBaseHelper;
import com.example.wang.advanceandroidprograming.Database.HomeCursorWrapper;
import com.example.wang.advanceandroidprograming.Database.HomeDbSchema;
import com.example.wang.advanceandroidprograming.Database.HomeDbSchema.HomeTable;
import static com.example.wang.advanceandroidprograming.Database.HomeDbSchema.HomeTable.Cols.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeStorage {
    private static HomeStorage sHomeStorage;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static HomeStorage get(Context context) {
        if (sHomeStorage == null) {
            sHomeStorage = new HomeStorage(context);
        }
        return sHomeStorage;
    }

    private HomeStorage(Context context) {
        mContext = context.getApplicationContext();
      //  mHomes = new ArrayList<>();
        mDatabase = new HomeBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addItem(Home h) {
        ContentValues values = getContentValues(h);
        mDatabase.insert(HomeTable.ITEM, null, values);
    }
    //public void addItem(Home h){mHomes.add(h);}
/*
    public List<Home> getInfo(){ return mHomes;}
    public Home getInfo(UUID id) {
        for (Home home : mHomes) {
            if (home.getId().equals(id)) {
                return home;
            }
        }
        return null;

    }
*/

    public List<Home> getItem() {
        List<Home> homes = new ArrayList<>();
        HomeCursorWrapper cursor = queryHome(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                homes.add(cursor.getHome());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return homes;
    }
    public Home getItem(UUID id) {
        HomeCursorWrapper cursor = queryHome(
                HomeTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );



        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getHome();
        } finally {
            cursor.close();
        }
    }



    public File getPhotoFile(Home home) {
         File filesDir = mContext.getFilesDir();
        return new File(filesDir, home.getPhotoFilename());

    }


    public void updateItem(Home h) {
        String uuidString = h.getId().toString();
        ContentValues values = getContentValues(h);
        mDatabase.update(HomeTable.ITEM, values,
                HomeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }


    private HomeCursorWrapper queryHome(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
               HomeTable.ITEM,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new HomeCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(Home h) {
        ContentValues values = new ContentValues();
        values.put(UUID, h.getId().toString());
        values.put(NAME, h.getName());
        values.put(DATE, h.getDate().getTime());
        values.put(VALUE,h.getValue());
        values.put(SERIAL,h.getSerial());

        return values;
    }

}
