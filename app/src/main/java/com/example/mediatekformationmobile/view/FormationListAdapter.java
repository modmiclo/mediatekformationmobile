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

public class FormationListAdapter extends RecyclerView.Adapter<FormationListAdapter.ViewHolder> {

    public interface OnFormationActionListener {
        void onFormationClicked(Formation formation);
        void onFavoriteClicked(Formation formation);
    }

    private List<Formation> formations;
    private OnFormationActionListener listener;
    public FormationListAdapter(List<Formation> formations, OnFormationActionListener listener){
        this.formations = formations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context parentContext = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parentContext);
        View view = layout.inflate(R.layout.layout_liste_formation, parent, false);
        return new ViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return formations != null ? formations.size() : 0;
    }

    public void setFormations(List<Formation> newFormations) {
        this.formations = newFormations;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageButton btnListFavori;
        public final TextView txtListPublishedAt;
        public final TextView txtListeTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtListeTitle = itemView.findViewById(R.id.txtListTitle);
            txtListPublishedAt = itemView.findViewById(R.id.txtListPublishedAt);
            btnListFavori = itemView.findViewById(R.id.btnListFavori);
        }
    }
}