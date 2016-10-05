package com.example.hva.seriesapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bogdan on 3.10.2016.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    //Database info
    private static final String DATABASE_NAME = "myseriesapp.db";
    private static final int DATABASE_VERSION = 1;

    //Series
    public static final String TABLE_SERIES = "series";
    public static final String TABLE_SELECTED_SERIES = "selected_series";
    public static final String COLUMN_SERIES_ID = "series_id";
    public static final String COLUMN_SERIES_TITLE = "series_title";
    public static final String COLUMN_SERIES_POSTER = "series_poster";


    //create table
    private static final String DATABASE_CREATE_SERIES =
            "CREATE TABLE " + TABLE_SERIES +
                    " ( " +
                    COLUMN_SERIES_ID + " integer primary key autoincrement, " +
                    COLUMN_SERIES_TITLE +  " text not null, " +
                    COLUMN_SERIES_POSTER + " integer " +
                    " );";

    //created table for selected shows
    private static final String DATABASE_CREATE_SELECTED_SERIES =
            "CREATE TABLE " + TABLE_SELECTED_SERIES +
                    " ( " +
                    COLUMN_SERIES_ID + " integer primary key autoincrement, " +
                    COLUMN_SERIES_TITLE +  " text not null, " +
                    COLUMN_SERIES_POSTER + " integer " +
                    " );";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE_SERIES);
        db.execSQL(DATABASE_CREATE_SELECTED_SERIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " +
                oldVersion + " to " + newVersion + ", which will destroy all old data ");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_SERIES);
        onCreate(db);

    }
}
