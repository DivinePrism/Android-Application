package com.example.wang.advanceandroidprograming.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wang.advanceandroidprograming.Database.HomeDbSchema.HomeTable;

public class HomeBaseHelper extends SQLiteOpenHelper {
        private static final int VERSION = 1;
        private static final String DATABASE_NAME = "homeBase.db";

        public HomeBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + HomeTable.ITEM + "(" +
                    " _id integer primary key autoincrement, " + HomeTable.Cols.UUID + ", "
                    + HomeTable.Cols.NAME + ", " +
                    HomeTable.Cols.DATE + ", " +
                    HomeTable.Cols.SERIAL+ ", " +
                    HomeTable.Cols.VALUE +
                    ")"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
}
