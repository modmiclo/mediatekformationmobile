package com.example.mediatekformationmobile.presenter;

import com.example.mediatekformationmobile.api.HelperApi;
import com.example.mediatekformationmobile.api.ICallbackApi;
import com.example.mediatekformationmobile.contract.IFormationsView;
import com.example.mediatekformationmobile.model.Formation;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

/**
 * 'presenter dédié' à la vue qui affiche la liste des formations (FormationsActivity)
 */
public class FormationsPresenter {
    private IFormationsView vue;
    private List<Formation> formations = null;
    /**
     * Constructeur : valorise la propriété qui permet d'accéder à la vue
     * @param vue
     */
    public FormationsPresenter(IFormationsView vue){
        this.vue = vue;
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
                    formations = result;
                    Collections.sort(formations, (p1, p2) -> p2.getPublishedAt().compareTo(p1.getPublishedAt()));
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
     * Retourne la liste filtrée sur le titre (sans casse).
     * Si filtre vide => renvoie la liste complète.
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
     * Demnde de transfert de la formation vers une autre activity
     * @param formation
     */
    public void transfertFormation(Formation formation){
        vue.transfertFormation(formation);
    }
}
