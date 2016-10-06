package com.example.hva.seriesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bogdan on 3.10.2016.
 */

public class DataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] seriesAllColumns = {MySQLiteHelper.COLUMN_SERIES_ID,
            MySQLiteHelper.COLUMN_SERIES_TITLE, MySQLiteHelper.COLUMN_SERIES_POSTER, MySQLiteHelper.COLUMN_SERIES_DESCRIPTION};

    public DataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
        dbHelper.close();
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createSeries(String series, int imageResource, String description) {
        if (!database.isOpen())
            open();

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SERIES_TITLE, series);
        values.put(MySQLiteHelper.COLUMN_SERIES_POSTER, imageResource);
        values.put(MySQLiteHelper.COLUMN_SERIES_DESCRIPTION, description);
        long insertId = database.insert(MySQLiteHelper.TABLE_SERIES, null, values);

        if (database.isOpen())
            close();


        return insertId;
    }

    public long createWatchedSeries(String series, int imageResource, String description) {
        if (!database.isOpen())
            open();

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SERIES_TITLE, series);
        values.put(MySQLiteHelper.COLUMN_SERIES_POSTER, imageResource);
        values.put(MySQLiteHelper.COLUMN_SERIES_DESCRIPTION, description);
        long insertId = database.insert(MySQLiteHelper.TABLE_SELECTED_SERIES, null, values);

        if (database.isOpen())
            close();


        return insertId;
    }

    public void deleteSeries(Series series) {
        if (!database.isOpen())
            open();

        database.delete(MySQLiteHelper.TABLE_SERIES, MySQLiteHelper.COLUMN_SERIES_ID + " =?",
                new String[] {Long.toString(series.getId())} );

        if (database.isOpen())
            close();
    }

    public void deleteWatchedSeries(Series series) {
        if (!database.isOpen())
            open();

        database.delete(MySQLiteHelper.TABLE_SELECTED_SERIES, MySQLiteHelper.COLUMN_SERIES_ID + " =?",
                new String[] {Long.toString(series.getId())} );

        if (database.isOpen())
            close();
    }


    public void updateSeries(Series series) {
        if (!database.isOpen())
            open();

        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.COLUMN_SERIES_TITLE, series.getTitle());
        database.update(MySQLiteHelper.TABLE_SERIES, args, MySQLiteHelper.COLUMN_SERIES_ID +
        "=?", new String[] {Long.toString(series.getId())});

        if (database.isOpen())
            close();
    }

    public void updateWatchedSeries(Series series) {
        if (!database.isOpen())
            open();

        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.COLUMN_SERIES_DESCRIPTION, series.getDescription());
        database.update(MySQLiteHelper.TABLE_SELECTED_SERIES, args, MySQLiteHelper.COLUMN_SERIES_ID +
                "=?", new String[] {Long.toString(series.getId())});

        if (database.isOpen())
            close();
    }



    public Series getSeries(long columnId) {
        if (!database.isOpen())
            open();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SERIES, seriesAllColumns, MySQLiteHelper.COLUMN_SERIES_ID +
        "=?", new String[] {Long.toString(columnId)}, null, null, null);
        cursor.moveToFirst();
        Series series = cursorToSeries(cursor);
        cursor.close();

        if (database.isOpen())
            close();

        return series;
    }

    public Series getWatchedSeries(long columnId) {
        if (!database.isOpen())
            open();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SELECTED_SERIES, seriesAllColumns, MySQLiteHelper.COLUMN_SERIES_ID +
                "=?", new String[] {Long.toString(columnId)}, null, null, null);
        cursor.moveToFirst();
        Series series = cursorToSeries(cursor);
        cursor.close();

        if (database.isOpen())
            close();

        return series;
    }

    public List<Series> getAllSeries() {
        if (!database.isOpen())
            open();

        List<Series> series = new ArrayList<Series>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SERIES, seriesAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Series specificSeries = cursorToSeries(cursor);
            series.add(specificSeries);
            cursor.moveToNext();
        }
        cursor.close();

        if (database.isOpen())
            close();

        return series;
    }

    public List<Series> getAllWatchedSeries() {
        if (!database.isOpen())
            open();

        List<Series> series = new ArrayList<Series>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SELECTED_SERIES, seriesAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Series specificSeries = cursorToSeries(cursor);
            series.add(specificSeries);
            cursor.moveToNext();
        }
        cursor.close();

        if (database.isOpen())
            close();

        return series;
    }

    public List<Series> searchSeries(String input) {
        if (!database.isOpen())
            open();

        List<Series> matchingSeries = new ArrayList<Series>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_SERIES +
                " WHERE " + MySQLiteHelper.COLUMN_SERIES_TITLE + " LIKE " +
                "'%" + input + "%'", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Series series = cursorToSeries(cursor);
            matchingSeries.add(series);
            cursor.moveToNext();
        }

        System.out.println(cursor.toString());

        cursor.close();

        if (database.isOpen())
            close();

        return matchingSeries;
    }

    private Series cursorToSeries(Cursor cursor) {
        try {
            Series series = new Series();
            series.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_SERIES_ID)));
            series.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_SERIES_TITLE)));
            series.setImageSource(cursor.getInt(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_SERIES_POSTER)));
            series.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_SERIES_DESCRIPTION)));
            return series;
        } catch (CursorIndexOutOfBoundsException exception) {
            exception.printStackTrace();
            return null;
        }
    }

}





































