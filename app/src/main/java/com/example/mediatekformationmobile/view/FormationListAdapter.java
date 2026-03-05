package com.example.mediatekformationmobile.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediatekformationmobile.R;
import com.example.mediatekformationmobile.model.Formation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter RecyclerView chargé d'afficher une liste de {@link Formation}.
 * Cette classe gère :
 * - l'affichage du titre
 * - l'affichage de la date
 * - l'icône de favori (rouge/gris)
 * - les callbacks de clic sur une formation et sur le bouton favori
 */
public class FormationListAdapter extends RecyclerView.Adapter<FormationListAdapter.ViewHolder> {

    /**
     * Interface de callback vers la vue (activity) pour gérer les actions utilisateur.
     */
    public interface OnFormationActionListener {
        /**
         * Déclenché lors du clic sur une formation (ligne).
         * @param formation formation sélectionnée
         */
        void onFormationClicked(Formation formation);

        /**
         * Déclenché lors du clic sur l'icône favori.
         * @param formation formation dont l'état favori doit être modifié
         */
        void onFavoriteClicked(Formation formation);
    }

    /**
     * Liste des formations à afficher.
     */
    private List<Formation> formations;

    /**
     * Listener chargé de recevoir les actions utilisateur.
     */
    private OnFormationActionListener listener;

    /**
     * Constructeur de l'adapter.

     * @param formations liste initiale de formations
     * @param listener   listener recevant les actions utilisateur
     */
    public FormationListAdapter(List<Formation> formations, OnFormationActionListener listener) {
        this.formations = formations;
        this.listener = listener;
    }

    /**
     * Crée un ViewHolder à partir du layout XML d'une ligne.
     * @param parent   parent RecyclerView
     * @param viewType type de vue (non utilisé ici)
     * @return ViewHolder initialisé
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context parentContext = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parentContext);
        View view = layout.inflate(R.layout.layout_liste_formation, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Associe les données d'une formation à une ligne de la RecyclerView.
     * @param holder   ViewHolder cible
     * @param position index de l'élément dans la liste
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Formation formation = formations.get(position);
        holder.txtListeTitle.setText(formation.getTitle());
        Date publishedAt = formation.getPublishedAt();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.txtListPublishedAt.setText(sdf.format(publishedAt));
        holder.btnListFavori.setImageResource(
                formation.isFavorite() ? R.drawable.coeur_rouge : R.drawable.coeur_gris
        );
        holder.btnListFavori.setOnClickListener(v -> {
            if (listener != null) listener.onFavoriteClicked(formation);
        });
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onFormationClicked(formation);
        });
    }

    /**
     * Retourne le nombre d'éléments à afficher.
     * @return taille de la liste, ou 0 si la liste est null
     */
    @Override
    public int getItemCount() {
        return formations != null ? formations.size() : 0;
    }

    /**
     * Met à jour la liste des formations affichées et rafraîchit la RecyclerView.
     * @param newFormations nouvelle liste à afficher
     */
    public void setFormations(List<Formation> newFormations) {
        this.formations = newFormations;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder représentant une ligne de formation.
     * Contient les références vers les composants UI de la ligne.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Bouton de favoris (icône cœur).
         */
        public final ImageButton btnListFavori;

        /**
         * Texte de la date de publication.
         */
        public final TextView txtListPublishedAt;

        /**
         * Texte du titre.
         */
        public final TextView txtListeTitle;

        /**
         * Constructeur du ViewHolder.
         * @param itemView vue représentant la ligne (layout inflate)
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtListeTitle = itemView.findViewById(R.id.txtListTitle);
            txtListPublishedAt = itemView.findViewById(R.id.txtListPublishedAt);
            btnListFavori = itemView.findViewById(R.id.btnListFavori);
        }
    }
}