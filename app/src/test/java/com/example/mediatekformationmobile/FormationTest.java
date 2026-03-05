package com.example.mediatekformationmobile.model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Tests unitaires JUnit pour la classe {@link Formation}.
 * Ces tests vérifient :
 * - l'initialisation via constructeur
 * - les getters
 * - l'état favori (valeur par défaut et modifications)
 * - la génération des URLs (miniature et image)
 * - le comportement avec des champs null
 */
public class FormationTest {

    /**
     * Vérifie que le constructeur initialise correctement tous les champs.
     */
    @Test
    public void constructor_shouldSetAllFields() {
        int id = 10;
        int playlistId = 5;
        Date publishedAt = new Date(1700000000000L);
        String title = "Titre test";
        String description = "Description test";
        String videoId = "abc123";

        Formation f = new Formation(id, playlistId, publishedAt, title, description, videoId);

        assertEquals(id, f.getId());
        assertEquals(playlistId, f.getPlaylistId());
        assertEquals(publishedAt, f.getPublishedAt());
        assertEquals(title, f.getTitle());
        assertEquals(description, f.getDescription());
        assertEquals(videoId, f.getVideoId());
    }

    /**
     * Vérifie que l'état favori est à false par défaut.
     */
    @Test
    public void isFavorite_shouldBeFalseByDefault() {
        Formation f = new Formation(1, 1, new Date(), "t", "d", "vid");
        assertFalse(f.isFavorite());
    }

    /**
     * Vérifie que {@link Formation#setFavorite(boolean)} met l'état favori à true.
     */
    @Test
    public void setFavorite_true_shouldMakeItFavorite() {
        Formation f = new Formation(1, 1, new Date(), "t", "d", "vid");
        f.setFavorite(true);
        assertTrue(f.isFavorite());
    }

    /**
     * Vérifie que {@link Formation#setFavorite(boolean)} peut remettre l'état favori à false.
     */
    @Test
    public void setFavorite_false_shouldRemoveFavorite() {
        Formation f = new Formation(1, 1, new Date(), "t", "d", "vid");
        f.setFavorite(true);
        assertTrue(f.isFavorite());
        f.setFavorite(false);
        assertFalse(f.isFavorite());
    }

    /**
     * Vérifie la construction de l'URL de miniature standard (default.jpg).
     */
    @Test
    public void getMiniature_shouldBuildDefaultThumbnailUrl() {
        String videoId = "XyZ987";
        Formation f = new Formation(1, 1, new Date(), "t", "d", videoId);
        String url = f.getMiniature();
        assertEquals("https://i.ytimg.com/vi/" + videoId + "/default.jpg", url);
    }

    /**
     * Vérifie la construction de l'URL d'image de meilleure qualité (mqdefault.jpg).
     */
    @Test
    public void getPicture_shouldBuildMqDefaultThumbnailUrl() {
        String videoId = "XyZ987";
        Formation f = new Formation(1, 1, new Date(), "t", "d", videoId);
        String url = f.getPicture();
        assertEquals("https://i.ytimg.com/vi/" + videoId + "/mqdefault.jpg", url);
    }

    /**
     * Vérifie que l'URL miniature n'est pas null même si videoId est vide.
     */
    @Test
    public void getMiniature_shouldNotBeNullEvenIfVideoIdIsEmpty() {
        Formation f = new Formation(1, 1, new Date(), "t", "d", "");
        String url = f.getMiniature();
        assertNotNull(url);
        assertEquals("https://i.ytimg.com/vi//default.jpg", url);
    }

    /**
     * Vérifie que l'URL image n'est pas null même si videoId est vide.
     */
    @Test
    public void getPicture_shouldNotBeNullEvenIfVideoIdIsEmpty() {
        Formation f = new Formation(1, 1, new Date(), "t", "d", "");
        String url = f.getPicture();
        assertNotNull(url);
        assertEquals("https://i.ytimg.com/vi//mqdefault.jpg", url);
    }

    /**
     * Vérifie que les getters peuvent renvoyer null pour titre/description si ces champs sont null.
     */
    @Test
    public void getters_shouldReturnNullIfTitleOrDescriptionAreNull() {
        Formation f = new Formation(1, 1, new Date(), null, null, "vid");
        assertNull(f.getTitle());
        assertNull(f.getDescription());
        assertEquals("vid", f.getVideoId());
    }

    /**
     * Vérifie que l'instance de date retournée est la même (pas de copie).
     */
    @Test
    public void publishedAt_shouldBeSameInstance() {
        Date date = new Date(123456789L);
        Formation f = new Formation(1, 1, date, "t", "d", "vid");
        assertSame(date, f.getPublishedAt());
    }
}