package com.mohit.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FavoritesDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "favorites";
    private static final String COLUMN_ID = "id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database version upgrades if necessary
    }

    public boolean addFavorite(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, userId);
        long insert = db.insert(TABLE_NAME, null, cv);
        return insert != -1;
    }

    public boolean removeFavorite(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(userId)}) > 0;
    }

    // Method to check if a user is favorite
    public boolean isFavorite(int userId) {
        // Implement method to query the database and check if the user is a favorite
        return false;
    }
}
