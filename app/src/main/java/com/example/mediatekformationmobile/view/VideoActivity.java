package com.example.mediatekformationmobile.view;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mediatekformationmobile.R;
import com.example.mediatekformationmobile.model.Formation;

/**
 * Activity responsable de l'affichage d'une vidéo YouTube via une {@link WebView}.
 * L'objet {@link Formation} est reçu via Intent (Serializable) depuis {@link UneFormationActivity}.
 * La WebView charge l'URL YouTube basée sur {@link Formation#getVideoId()}.
 */
public class VideoActivity extends AppCompatActivity {

    /**
     * Objet d'affichage de la vidéo YouTube.
     */
    WebView wbvYoutube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    /**
     * Initialise l'affichage de l'activity :
     * récupère la formation et configure la WebView.
     */
    private void init() {
        recupFormation();
    }

    /**
     * Récupère la formation envoyée par {@link UneFormationActivity} et affiche la vidéo.
     * Active JavaScript (nécessaire pour certains contenus), définit un {@link WebViewClient} puis charge l'URL YouTube.
     */
    private void recupFormation() {
        Formation formation = (Formation) getIntent().getSerializableExtra("formation");
        if (formation != null) {
            wbvYoutube = findViewById(R.id.wbvYoutube);
            wbvYoutube.getSettings().setJavaScriptEnabled(true);
            wbvYoutube.setWebViewClient(new WebViewClient());
//            wbvYoutube.loadUrl("https://www.youtube.com/embed/" + formation.getVideoId());
            wbvYoutube.loadUrl("https://www.youtube.com/watch?v=" + formation.getVideoId());
        }
    }
}