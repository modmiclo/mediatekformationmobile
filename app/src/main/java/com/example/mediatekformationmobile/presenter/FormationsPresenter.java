package com.example.mediatekformationmobile.presenter;

import android.content.Context;

import com.example.mediatekformationmobile.api.HelperApi;
import com.example.mediatekformationmobile.api.ICallbackApi;
import com.example.mediatekformationmobile.contract.IFormationsView;
import com.example.mediatekformationmobile.data.FavoritesRepository;
import com.example.mediatekformationmobile.model.Formation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Presenter MVP dédié à la vue qui affiche la liste des formations (FormationsActivity).
 * Responsabilités :
 * - Appeler l'API pour charger les formations
 * - Trier les formations par date de publication décroissante
 * - Gérer le mode "toutes" / "favoris uniquement"
 * - Gérer la persistance des favoris en SQLite via {@link FavoritesRepository}
 * - Filtrer la liste par texte (sur le titre)
 * - Transmettre les résultats à la vue via {@link IFormationsView}
 */
public class FormationsPresenter {

    /**
     * Référence vers la vue MVP (Activity).
     */
    private IFormationsView vue;

    /**
     * Liste complète des formations récupérées depuis l'API (non filtrée).
     */
    private List<Formation> allFormations = null;

    /**
     * Liste de base affichable (toutes ou seulement favoris, selon {@link #onlyFavorites}).
     */
    private List<Formation> formations = null;

    /**
     * Repository SQLite pour la persistance des favoris.
     */
    private final FavoritesRepository favoritesRepo;

    /**
     * Ensemble des ids favoris enregistrés localement.
     */
    private Set<Integer> favoriteIds = new HashSet<>();

    /**
     * Mode d'affichage : {@code true} = uniquement favoris.
     */
    private boolean onlyFavorites = false;

    /**
     * Constructeur du presenter.
     * @param vue     vue MVP utilisée pour afficher les données et déclencher la navigation
     * @param context contexte Android (utilisé pour instancier le repository SQLite)
     */
    public FormationsPresenter(IFormationsView vue, Context context) {
        this.vue = vue;
        this.favoritesRepo = new FavoritesRepository(context);
    }

    /**
     * Active ou désactive l'affichage uniquement des favoris.
     * @param onlyFavorites {@code true} pour afficher uniquement les favoris, sinon {@code false}
     */
    public void setOnlyFavorites(boolean onlyFavorites) {
        this.onlyFavorites = onlyFavorites;
    }

    /**
     * Charge les formations depuis l'API distante puis met à jour la vue.
     * Étapes :
     * - Appel API asynchrone
     * - Tri par date de publication décroissante
     * - Chargement des ids favoris depuis SQLite
     * - Nettoyage des favoris obsolètes (ids non présents dans l'API)
     * - Synchronisation de l'état {@code favorite} des objets {@link Formation}
     * - Construction de la liste de base (toutes/favoris)
     * - Affichage via {@link IFormationsView#afficherListe(List)}
     */
    public void chargerFormations() {
        HelperApi.call(HelperApi.getApi().getFormations(), new ICallbackApi<List<Formation>>() {
            @Override
            public void onSuccess(List<Formation> result) {
                if (result != null && !result.isEmpty()) {
                    allFormations = result;

                    Collections.sort(allFormations, (p1, p2) -> p2.getPublishedAt().compareTo(p1.getPublishedAt()));
                    favoriteIds = favoritesRepo.getAllFavoriteIds();
                    Set<Integer> idsApi = new HashSet<>();
                    for (Formation f : allFormations) idsApi.add(f.getId());
                    favoritesRepo.cleanupNotIn(idsApi);
                    favoriteIds = favoritesRepo.getAllFavoriteIds();
                    for (Formation f : allFormations) {
                        f.setFavorite(favoriteIds.contains(f.getId()));
                    }
                    rebuildBaseList();
                    vue.afficherListe(formations);
                } else {
                    vue.afficherMessage("échec chargement formations");
                }
            }

            @Override
            public void onError() {
                vue.afficherMessage("échec chargement formations");
            }
        });
    }

    /**
     * Reconstruit la liste de base {@link #formations} selon le mode :
     * si {@link #onlyFavorites} est false : liste complète {@link #allFormations}
     * si {@link #onlyFavorites} est true : uniquement les formations dont {@link Formation#isFavorite()} est true
     */
    private void rebuildBaseList() {
        if (allFormations == null) {
            formations = Collections.emptyList();
            return;
        }
        if (!onlyFavorites) {
            formations = allFormations;
            return;
        }
        List<Formation> favs = new ArrayList<>();
        for (Formation f : allFormations) {
            if (f.isFavorite()) favs.add(f);
        }
        formations = favs;
    }

    /**
     * Retourne la liste filtrée sur le titre (recherche insensible à la casse).
     * Si {@code filtre} est vide ou null, renvoie la liste de base (toutes/favoris).
     * @param filtre texte saisi par l'utilisateur (peut être null)
     * @return liste filtrée (jamais {@code null})
     */
    public List<Formation> getFormationsFiltrees(String filtre) {
        if (formations == null) {
            return Collections.emptyList();
        }
        if (filtre == null || filtre.trim().isEmpty()) {
            return formations;
        }
        String f = filtre.trim().toLowerCase(Locale.ROOT);
        List<Formation> res = new ArrayList<>();
        for (Formation formation : formations) {
            String title = formation.getTitle();
            if (title != null && title.toLowerCase(Locale.ROOT).contains(f)) {
                res.add(formation);
            }
        }
        return res;
    }

    /**
     * Inverse l'état favori d'une formation et persiste immédiatement la modification en SQLite.
     * Après modification, reconstruit la liste de base (toutes/favoris) puis redemande l'affichage
     * à la vue en respectant le filtre actuel.
     * @param formation     formation à basculer en favori/non favori (si null : aucune action)
     * @param filtreActuel  filtre actuellement saisi (pour rafraîchir l'affichage de manière cohérente)
     */
    public void toggleFavori(Formation formation, String filtreActuel) {
        if (formation == null) return;

        int id = formation.getId();
        boolean newState = !formation.isFavorite();
        formation.setFavorite(newState);

        if (newState) {
            favoritesRepo.addFavorite(id);
            favoriteIds.add(id);
        } else {
            favoritesRepo.removeFavorite(id);
            favoriteIds.remove(id);
        }
        rebuildBaseList();
        vue.afficherListe(getFormationsFiltrees(filtreActuel));
    }

    /**
     * Demande à la vue d'effectuer la navigation vers l'écran de détail,
     * en transmettant l'objet {@link Formation}.
     * @param formation formation sélectionnée (peut être null selon l'appelant)
     */
    public void transfertFormation(Formation formation) {
        vue.transfertFormation(formation);
    }
}