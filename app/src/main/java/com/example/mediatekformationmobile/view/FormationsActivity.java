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
 * Activity pour afficher la liste des formations
 */
public class FormationsActivity extends AppCompatActivity implements IFormationsView, FormationListAdapter.OnFormationActionListener {
    public static final String EXTRA_ONLY_FAVORITES = "only_favorites";
    private FormationsPresenter presenter;
    private RecyclerView lstFormations;
    private FormationListAdapter adapter;
    private EditText txtFiltre;
    private Button btnFiltrer;
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
     * Traitements nécessaires dès la création de l'activity
     */
    private void init(){
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
     * Affiche une liste (utilisée aussi après filtrage / favoris)
     */
    @Override
    public void afficherListe(List formations) {
        if (formations != null && adapter != null){
            //noinspection unchecked
            adapter.setFormations((List<Formation>) formations);
        }
    }

    /**
     * Navigation vers le détail
     */
    @Override
    public void transfertFormation(Formation formation) {
        Intent intent = new Intent(FormationsActivity.this, UneFormationActivity.class);
        intent.putExtra("formation", formation);
        startActivity(intent);
    }

    @Override
    public void afficherMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFormationClicked(Formation formation) {
        presenter.transfertFormation(formation);
    }

    @Override
    public void onFavoriteClicked(Formation formation) {
        String filtreActuel = txtFiltre.getText() != null ? txtFiltre.getText().toString() : "";
        presenter.toggleFavori(formation, filtreActuel);
    }
}