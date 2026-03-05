package com.example.mediatekformationmobile.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashSet;
import java.util.Set;

/**
 * Dépôt (Repository) chargé de la gestion des formations favorites en base locale SQLite.
 * Cette classe encapsule les opérations CRUD minimales sur la table {@link FavoritesDbHelper#TABLE_FAVORITES}.
 * Elle est utilisée par le Presenter pour persister l'état "favori" d'une formation.
 * {@link #addFavorite(int)} : ajoute/met à jour un favori
 * {@link #removeFavorite(int)} : supprime un favori
 * {@link #getAllFavoriteIds()} : récupère tous les ids favoris
 * {@link #cleanupNotIn(Set)} : supprime les favoris qui ne correspondent plus aux formations de l'API
 */
public class FavoritesRepository {

    /**
     * Helper SQLite responsable de la création/upgrade de la base.
     */
    private final FavoritesDbHelper dbHelper;

    /**
     * Construit le repository et instancie le helper SQLite.
     * Le {@link Context#getApplicationContext()} est utilisé pour éviter les fuites mémoire.
     *
     * @param context contexte Android (Activity/Application)
     */
    public FavoritesRepository(Context context) {
        this.dbHelper = new FavoritesDbHelper(context.getApplicationContext());
    }

    /**
     * Récupère l'ensemble des identifiants de formations enregistrées en favoris.
     *
     * @return ensemble d'identifiants (ids) favoris. Ensemble vide si aucun favori.
     */
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

    /**
     * Ajoute une formation aux favoris.
     * L'insertion se fait avec {@link SQLiteDatabase#CONFLICT_REPLACE} afin d'éviter les doublons.
     * @param formationId identifiant de la formation à ajouter en favori
     */
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

    /**
     * Supprime une formation des favoris
     * @param formationId identifiant de la formation à retirer des favoris
     */
    public void removeFavorite(int formationId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                FavoritesDbHelper.TABLE_FAVORITES,
                FavoritesDbHelper.COL_FORMATION_ID + "=?",
                new String[]{String.valueOf(formationId)}
        );
    }

    /**
     * Nettoie les favoris locaux en supprimant ceux qui ne correspondent plus à une formation existante côté API.
     * @param validIds ensemble des ids valides (présents dans la liste reçue de l'API)
     */
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