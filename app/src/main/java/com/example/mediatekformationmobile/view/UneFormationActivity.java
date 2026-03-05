package com.example.mediatekformationmobile.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mediatekformationmobile.R;
import com.example.mediatekformationmobile.model.Formation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Activity affichant le détail d'une {@link Formation}.
 * L'objet {@link Formation} est reçu via Intent (Serializable) depuis {@link FormationsActivity}.
 * L'écran affiche :
 * - date de publication
 * - titre
 * - description
 * - image (miniature YouTube)
 * Un clic sur l'image déclenche l'ouverture de {@link VideoActivity}.
 */
public class UneFormationActivity extends AppCompatActivity {

    /**
     * Champ affichant la date de publication.
     */
    private TextView txtPublishedAt;

    /**
     * Champ affichant le titre.
     */
    private TextView txtTitle;

    /**
     * Champ affichant la description.
     */
    private TextView txtDescription;

    /**
     * Bouton-image affichant la miniature et permettant d'ouvrir la vidéo.
     */
    private ImageButton btnPicture;

    /**
     * Formation affichée à l'écran (reçue via Intent).
     */
    private Formation formation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_une_formation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    /**
     * Traitements nécessaires dès la création de l'activity :
     * - récupère les composants graphiques
     * - déclare les listeners
     * - récupère et affiche la formation reçue
     */
    private void init() {
        chargeObjetsGraphiques();
        btnPicture.setOnClickListener(v -> btnPicture_clic());
        recupFormation();
    }

    /**
     * Récupère les objets graphiques depuis le layout XML.
     */
    private void chargeObjetsGraphiques() {
        txtPublishedAt = (TextView) findViewById(R.id.txtPublishedAt);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        btnPicture = findViewById(R.id.btnPicture);
    }

    /**
     * Traitements réalisés lors du clic sur l'image :
     * ouvre {@link VideoActivity} en transmettant l'objet {@link Formation}.
     */
    private void btnPicture_clic() {
        if (formation != null) {
            Intent intent = new Intent(UneFormationActivity.this, VideoActivity.class);
            intent.putExtra("formation", formation);
            startActivity(intent);
        }
    }

    /**
     * Récupère la formation envoyée par {@link FormationsActivity} via l'Intent,
     * puis affiche ses informations dans l'interface.
     */
    private void recupFormation() {
        formation = (Formation) getIntent().getSerializableExtra("formation");
        if (formation != null) {
            Date datePublication = formation.getPublishedAt();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dateFormatee = sdf.format(datePublication);
            txtPublishedAt.setText(dateFormatee);
            txtTitle.setText(formation.getTitle());
            txtDescription.setText(formation.getDescription());
            loadMapPreview(btnPicture, formation.getPicture());
        }
    }

    /**
     * Charge une image depuis une URL et l'affiche dans un {@link ImageButton}.
     * Le téléchargement se fait dans un thread de fond, puis l'affichage est réalisé sur le thread UI
     * via {@link android.view.View#post(Runnable)}.
     * @param img composant cible qui affichera l'image
     * @param url URL de l'image à télécharger
     */
    private void loadMapPreview(ImageButton img, String url) {
        //start a background thread for networking
        new Thread(new Runnable() {
            public void run() {
                try {
                    //download the drawable
                    final Drawable drawable = Drawable.createFromStream((InputStream) new URL(url).getContent(), "src");
                    //edit the view in the UI thread
                    img.post(new Runnable() {
                        public void run() {
                            img.setImageDrawable(drawable);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}