package com.example.mediatekformationmobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mediatekformationmobile.R;

/**
 * Activity représentant l'écran d'accueil (menu principal).
 * Cette vue permet à l'utilisateur de choisir entre :
 * - Afficher toutes les formations
 * - Afficher uniquement les favoris
 * Le choix est transmis à {@link FormationsActivity} via l'extra
 * {@link FormationsActivity#EXTRA_ONLY_FAVORITES}.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Bouton d'accès à la liste complète des formations.
     */
    private ImageButton btnFormations;

    /**
     * Bouton d'accès à la liste des favoris.
     */
    private ImageButton btnFavoris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    /**
     * Initialise l'activity :
     * - charge les composants graphiques
     * - configure les actions de menu
     */
    private void init() {
        chargeObjetsGraphiques();
        creerMenu();
    }

    /**
     * Récupère les références vers les composants de l'interface (ImageButton).
     */
    private void chargeObjetsGraphiques() {
        btnFormations = findViewById(R.id.btnFormations);
        btnFavoris = findViewById(R.id.btnFavoris);
    }

    /**
     * Associe les actions de clic aux boutons du menu.
     * Lance {@link FormationsActivity} en précisant le mode :
     * - {@code false} : toutes les formations
     * - {@code true} : seulement les favoris
     */
    private void creerMenu() {
        btnFormations.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormationsActivity.class);
            intent.putExtra(FormationsActivity.EXTRA_ONLY_FAVORITES, false);
            startActivity(intent);
        });
        btnFavoris.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormationsActivity.class);
            intent.putExtra(FormationsActivity.EXTRA_ONLY_FAVORITES, true);
            startActivity(intent);
        });
    }
}