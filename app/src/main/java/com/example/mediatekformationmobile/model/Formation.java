package com.example.mediatekformationmobile.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Modèle métier représentant une formation vidéo.
 * Une formation correspond à un objet récupéré depuis l'API distante.
 * Elle contient les informations nécessaires à l'affichage (titre, description, date)
 * ainsi que l'identifiant vidéo YouTube permettant de générer les miniatures et d'ouvrir la vidéo.
 * L'attribut {@code favorite} est local (non issu de l'API) et sert à gérer l'état favori via stockage SQLite.
 */
public class Formation implements Serializable {

    /**
     * Base d'URL YouTube permettant de construire les miniatures.
     */
    private static final String CHEMINIMAGE = "https://i.ytimg.com/vi/";

    private int id;
    private int playlist_id;
    private Date published_at;
    private String title;
    private String description;
    private String video_id;

    /**
     * Indique si la formation est en favori local.
     * Valeur par défaut : {@code false}.
     */
    private boolean favorite = false;

    /**
     * Constructeur : initialise les propriétés de la formation.
     * @param id           identifiant unique de la formation
     * @param playlist_id  identifiant de playlist associée
     * @param published_at date de publication
     * @param title        titre de la formation
     * @param description  description détaillée
     * @param video_id     identifiant de la vidéo YouTube
     */
    public Formation(int id, int playlist_id, Date published_at, String title, String description, String video_id) {
        this.id = id;
        this.playlist_id = playlist_id;
        this.published_at = published_at;
        this.title = title;
        this.description = description;
        this.video_id = video_id;
    }

    /**
     * Retourne l'identifiant de la formation.
     * @return id de la formation
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne l'identifiant de playlist.
     * @return id de playlist
     */
    public int getPlaylistId() {
        return playlist_id;
    }

    /**
     * Retourne la date de publication.
     * @return date de publication
     */
    public Date getPublishedAt() {
        return published_at;
    }

    /**
     * Retourne le titre de la formation.
     * @return titre (peut être {@code null})
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retourne la description de la formation.
     * @return description (peut être {@code null})
     */
    public String getDescription() {
        return description;
    }

    /**
     * Construit l'URL de la miniature standard YouTube (default.jpg).
     * @return URL de la miniature
     */
    public String getMiniature() {
        return CHEMINIMAGE + video_id + "/default.jpg";
    }

    /**
     * Construit l'URL de l'image de meilleure qualité YouTube (mqdefault.jpg).
     * @return URL de l'image
     */
    public String getPicture() {
        return CHEMINIMAGE + video_id + "/mqdefault.jpg";
    }

    /**
     * Retourne l'identifiant YouTube de la vidéo.
     * @return videoId YouTube
     */
    public String getVideoId() {
        return video_id;
    }

    /**
     * Indique si la formation est actuellement marquée en favori local.
     * @return {@code true} si favori, sinon {@code false}
     */
    public boolean isFavorite() {
        return favorite;
    }

    /**
     * Définit l'état favori local de la formation.
     * @param favorite nouvel état favori
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}