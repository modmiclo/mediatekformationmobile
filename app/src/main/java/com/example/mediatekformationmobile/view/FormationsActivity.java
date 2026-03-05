package com.example.mediatekformationmobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediatekformationmobile.R;
import com.example.mediatekformationmobile.contract.IFormationsView;
import com.example.mediatekformationmobile.model.Formation;
import com.example.mediatekformationmobile.presenter.FormationsPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Vue (Activity) permettant d'afficher la liste des formations dans une {@link RecyclerView}.
 * Cette activity implémente l'interface MVP {@link IFormationsView} et délègue la logique au
 * {@link FormationsPresenter}.
 * Fonctionnalités :
 * - Chargement initial des formations via API (par le presenter)
 * - Affichage de la liste via {@link FormationListAdapter}
 * - Filtrage par texte sur le titre
 * - Gestion des favoris via clic sur l'icône cœur
 * - Navigation vers le détail
 */
public class FormationsActivity extends AppCompatActivity implements IFormationsView, FormationListAdapter.OnFormationActionListener {

    /**
     * Nom de l'extra Intent indiquant si l'écran doit afficher uniquement les favoris.
     */
    public static final String EXTRA_ONLY_FAVORITES = "only_favorites";

    /**
     * Presenter MVP associé à cette vue.
     */
    private FormationsPresenter presenter;

    /**
     * Liste graphique des formations.
     */
    private RecyclerView lstFormations;

    /**
     * Adapter gérant l'affichage des lignes et les interactions (clic formation / clic favori).
     */
    private FormationListAdapter adapter;

    /**
     * Champ de saisie du filtre.
     */
    private EditText txtFiltre;

    /**
     * Bouton déclenchant le filtrage.
     */
    private Button btnFiltrer;

    /**
     * Mode : uniquement favoris.
     */
    private boolean onlyFavorites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    /**
     * Initialise l'activity :
     * - lit le mode (toutes/favoris) dans l'Intent
     * - instancie le {@link FormationsPresenter}
     * - initialise RecyclerView + adapter
     * - configure l'action de filtrage
     * - déclenche le chargement initial via {@link FormationsPresenter#chargerFormations()}
     */
    private void init() {
        Intent intent = getIntent();
        onlyFavorites = intent != null && intent.getBooleanExtra(EXTRA_ONLY_FAVORITES, false);

        presenter = new FormationsPresenter(this, getApplicationContext());
        presenter.setOnlyFavorites(onlyFavorites);

        lstFormations = findViewById(R.id.lstFormations);
        txtFiltre = findViewById(R.id.txtFiltre);
        btnFiltrer = findViewById(R.id.btnFiltrer);

        lstFormations.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FormationListAdapter(new ArrayList<>(), this);
        lstFormations.setAdapter(adapter);

        btnFiltrer.setOnClickListener(v -> {
            String filtre = txtFiltre.getText().toString();
            List<Formation> listeFiltree = presenter.getFormationsFiltrees(filtre);
            afficherListe(listeFiltree);
        });

        presenter.chargerFormations();
    }

    /**
     * Affiche une liste de formations dans la RecyclerView via l'adapter.
     * @param formations liste de formations à afficher (type brut car signature de l'interface)
     */
    @Override
    public void afficherListe(List formations) {
        if (formations != null && adapter != null) {
            //noinspection unchecked
            adapter.setFormations((List<Formation>) formations);
        }
    }

    /**
     * Déclenche la navigation vers l'écran de détail {@link UneFormationActivity}.
     * L'objet {@link Formation} est transmis via Intent (Serializable).
     * @param formation formation sélectionnée
     */
    @Override
    public void transfertFormation(Formation formation) {
        Intent intent = new Intent(FormationsActivity.this, UneFormationActivity.class);
        intent.putExtra("formation", formation);
        startActivity(intent);
    }

    /**
     * Affiche un message utilisateur (Toast).
     * @param message message à afficher
     */
    @Override
    public void afficherMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Callback déclenché par l'adapter lors du clic sur une ligne formation.
     * Délègue la navigation au presenter.
     * @param formation formation cliquée
     */
    @Override
    public void onFormationClicked(Formation formation) {
        presenter.transfertFormation(formation);
    }

    /**
     * Callback déclenché par l'adapter lors du clic sur l'icône favori.
     * Délègue le toggle au presenter en conservant le filtre actuel.
     * @param formation formation dont l'état favori doit être inversé
     */
    @Override
    public void onFavoriteClicked(Formation formation) {
        String filtreActuel = txtFiltre.getText() != null ? txtFiltre.getText().toString() : "";
        presenter.toggleFavori(formation, filtreActuel);
    }
}