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
 * Presenter dédié à la vue qui affiche la liste des formations (FormationsActivity)
 */
public class FormationsPresenter {
    private IFormationsView vue;
    private List<Formation> allFormations = null;
    private List<Formation> formations = null;
    private final FavoritesRepository favoritesRepo;
    private Set<Integer> favoriteIds = new HashSet<>();
    private boolean onlyFavorites = false;
    public FormationsPresenter(IFormationsView vue, Context context){
        this.vue = vue;
        this.favoritesRepo = new FavoritesRepository(context);
    }

    public void setOnlyFavorites(boolean onlyFavorites) {
        this.onlyFavorites = onlyFavorites;
    }

    /**
     * Récupère les formations de la BDD distante et les envoie à la vue
     * (appelé 1 seule fois)
     */
    public void chargerFormations() {
        HelperApi.call(HelperApi.getApi().getFormations(), new ICallbackApi<List<Formation>>(){
            @Override
            public void onSuccess(List<Formation> result) {
                if(result != null && !result.isEmpty()){
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
     * Reconstruit formations = (toutes) ou (uniquement favoris)
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
     * Retourne la liste filtrée sur le titre (sans casse).
     * Si filtre vide => renvoie la liste complète (selon mode).
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
     * Toggle favori (local uniquement) + refresh de la liste (en respectant le filtre)
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
     * Demande de transfert de la formation vers une autre activity
     */
    public void transfertFormation(Formation formation){
        vue.transfertFormation(formation);
    }
}