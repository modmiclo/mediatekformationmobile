package com.example.mediatekformationmobile.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashSet;
import java.util.Set;

public class FavoritesRepository {
    private final FavoritesDbHelper dbHelper;
    public FavoritesRepository(Context context) {
        this.dbHelper = new FavoritesDbHelper(context.getApplicationContext());
    }

    public Set<Integer> getAllFavoriteIds() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Set<Integer> ids = new HashSet<>();
        Cursor c = null;
        try {
            c = db.query(
                    FavoritesDbHelper.TABLE_FAVORITES,
                    new String[]{FavoritesDbHelper.COL_FORMATION_ID},
                    null, null, null, null, null
            );
            while (c.moveToNext()) {
                ids.add(c.getInt(0));
            }
        } finally {
            if (c != null) c.close();
        }
        return ids;
    }

    public void addFavorite(int formationId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoritesDbHelper.COL_FORMATION_ID, formationId);
        db.insertWithOnConflict(
                FavoritesDbHelper.TABLE_FAVORITES,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public void removeFavorite(int formationId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                FavoritesDbHelper.TABLE_FAVORITES,
                FavoritesDbHelper.COL_FORMATION_ID + "=?",
                new String[]{String.valueOf(formationId)}
        );
    }

    public void cleanupNotIn(Set<Integer> validIds) {
        if (validIds == null || validIds.isEmpty()) {
            return;
        }
        Set<Integer> current = getAllFavoriteIds();
        for (Integer favId : current) {
            if (!validIds.contains(favId)) {
                removeFavorite(favId);
            }
        }
    }
}