package com.example.mediatekformationmobile.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper SQLite responsable de la création et de la mise à jour de la base locale.
 * La base contient la table des favoris (ids des formations marquées comme favorites).
 */
public class FavoritesDbHelper extends SQLiteOpenHelper {

    /**
     * Nom du fichier de base SQLite sur l'appareil.
     */
    private static final String DB_NAME = "mediatekformationmobile.db";

    /**
     * Version de la base.
     */
    private static final int DB_VERSION = 1;

    /**
     * Nom de la table contenant les favoris.
     */
    public static final String TABLE_FAVORITES = "favorites";

    /**
     * Nom de la colonne contenant l'id de la formation.
     */
    public static final String COL_FORMATION_ID = "formation_id";

    /**
     * Requête SQL de création de la table des favoris.
     * La colonne {@link #COL_FORMATION_ID} est clé primaire afin de garantir l'unicité par formation.
     */
    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COL_FORMATION_ID + " INTEGER PRIMARY KEY" +
                    ");";

    /**
     * Constructeur.
     * @param context contexte Android (Activity/Application)
     */
    public FavoritesDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Création initiale de la base (appelée au premier accès).
     * @param db base SQLite à initialiser
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    /**
     * Mise à jour du schéma de la base lorsque {@link #DB_VERSION} augmente.
     * @param db         base SQLite
     * @param oldVersion ancienne version
     * @param newVersion nouvelle version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }
}