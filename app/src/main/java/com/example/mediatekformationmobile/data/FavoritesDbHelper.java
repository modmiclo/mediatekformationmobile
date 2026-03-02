package com.example.mediatekformationmobile.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mediatekformationmobile.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COL_FORMATION_ID = "formation_id";
    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COL_FORMATION_ID + " INTEGER PRIMARY KEY" +
                    ");";

    public FavoritesDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }
}