package com.example.mediatekformationmobile.model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class FormationTest {
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

    @Test
    public void isFavorite_shouldBeFalseByDefault() {
        Formation f = new Formation(1, 1, new Date(), "t", "d", "vid");
        assertFalse(f.isFavorite());
    }

    @Test
    public void setFavorite_true_shouldMakeItFavorite() {
        Formation f = new Formation(1, 1, new Date(), "t", "d", "vid");
        f.setFavorite(true);
        assertTrue(f.isFavorite());
    }

    @Test
    public void setFavorite_false_shouldRemoveFavorite() {
        Formation f = new Formation(1, 1, new Date(), "t", "d", "vid");
        f.setFavorite(true);
        assertTrue(f.isFavorite());
        f.setFavorite(false);
        assertFalse(f.isFavorite());
    }

    @Test
    public void getMiniature_shouldBuildDefaultThumbnailUrl() {
        String videoId = "XyZ987";
        Formation f = new Formation(1, 1, new Date(), "t", "d", videoId);
        String url = f.getMiniature();
        assertEquals("https://i.ytimg.com/vi/" + videoId + "/default.jpg", url);
    }

    @Test
    public void getPicture_shouldBuildMqDefaultThumbnailUrl() {
        String videoId = "XyZ987";
        Formation f = new Formation(1, 1, new Date(), "t", "d", videoId);
        String url = f.getPicture();
        assertEquals("https://i.ytimg.com/vi/" + videoId + "/mqdefault.jpg", url);
    }

    @Test
    public void getMiniature_shouldNotBeNullEvenIfVideoIdIsEmpty() {
        Formation f = new Formation(1, 1, new Date(), "t", "d", "");
        String url = f.getMiniature();
        assertNotNull(url);
        assertEquals("https://i.ytimg.com/vi//default.jpg", url);
    }

    @Test
    public void getPicture_shouldNotBeNullEvenIfVideoIdIsEmpty() {
        Formation f = new Formation(1, 1, new Date(), "t", "d", "");
        String url = f.getPicture();
        assertNotNull(url);
        assertEquals("https://i.ytimg.com/vi//mqdefault.jpg", url);
    }

    @Test
    public void getters_shouldReturnNullIfTitleOrDescriptionAreNull() {
        Formation f = new Formation(1, 1, new Date(), null, null, "vid");
        assertNull(f.getTitle());
        assertNull(f.getDescription());
        assertEquals("vid", f.getVideoId());
    }

    @Test
    public void publishedAt_shouldBeSameInstance() {
        Date date = new Date(123456789L);
        Formation f = new Formation(1, 1, date, "t", "d", "vid");
        assertSame(date, f.getPublishedAt());
    }
}