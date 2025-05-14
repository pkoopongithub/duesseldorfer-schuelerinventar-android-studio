package com.example.personalitytest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalitytest.R;
import com.example.personalitytest.models.Profile;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    public interface OnProfileClickListener {
        void onProfileClick(Profile profile);
        void onProfileDelete(Profile profile);
    }
    
    private final List<Profile> profiles;
    private final OnProfileClickListener listener;
    
    public ProfileAdapter(List<Profile> profiles, OnProfileClickListener listener) {
        this.profiles = profiles;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profiles.get(position);
        holder.bind(profile, listener);
    }
    
    @Override
    public int getItemCount() {
        return profiles.size();
    }
    
    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvProfileName, tvCreatedAt;
        private final ImageButton btnDelete;
        
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProfileName = itemView.findViewById(R.id.tvProfileName);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            btnDelete = itemView.findViewById(R.id.btnDeleteProfile);
        }
        
        public void bind(Profile profile, OnProfileClickListener listener) {
            tvProfileName.setText(profile.getName() != null ? profile.getName() : "Unbenanntes Profil");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            tvCreatedAt.setText(dateFormat.format(profile.getCreatedAt()));
            
            itemView.setOnClickListener(v -> listener.onProfileClick(profile));
            btnDelete.setOnClickListener(v -> listener.onProfileDelete(profile));
        }
    }
}