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

import java.util.List;

/**
 * Activity pour afficher la liste des formations
 */
public class FormationsActivity extends AppCompatActivity implements IFormationsView {

    private FormationsPresenter presenter;
    private RecyclerView lstFormations;
    private FormationListAdapter adapter;
    private EditText txtFiltre;
    private Button btnFiltrer;

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
        presenter = new FormationsPresenter(this);
        lstFormations = findViewById(R.id.lstFormations);
        txtFiltre = findViewById(R.id.txtFiltre);
        btnFiltrer = findViewById(R.id.btnFiltrer);
        lstFormations.setLayoutManager(new LinearLayoutManager(this));
        btnFiltrer.setOnClickListener(v -> {
            String filtre = txtFiltre.getText().toString();
            List<Formation> listeFiltree = presenter.getFormationsFiltrees(filtre);
            afficherListe(listeFiltree);
        });
        presenter.chargerFormations();
    }

    /**
     * Méthode permettant le transfert de la liste des formations pour affichage
     *
     * @param formations
     */
    @Override
    public void afficherListe(List formations) {
        if (formations != null){
            RecyclerView lstHisto = (RecyclerView) findViewById(R.id.lstFormations);
            FormationListAdapter adapter = new FormationListAdapter(formations, FormationsActivity.this);
            lstHisto.setAdapter(adapter);
            lstHisto.setLayoutManager(new LinearLayoutManager(FormationsActivity.this));
        }
    }

    /**
     * Méthode permettant le transfert d'une formation vers une activity
     *
     * @param formation
     */
    @Override
    public void transfertFormation(Formation formation) {
        Intent intent = new Intent(FormationsActivity.this, UneFormationActivity.class);
        intent.putExtra("formation", formation);
        startActivity(intent);
    }

    /**
     * Méthode permettant d'afficher un message de type Toast
     *
     * @param message
     */
    @Override
    public void afficherMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}