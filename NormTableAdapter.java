package com.example.personalitytest.adapters;

import android.text.TextWatcher;
import android.text.Editable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalitytest.models.NormTableItem;
import com.example.personalitytest.R;

import java.util.ArrayList;
import java.util.List;

public class NormTableAdapter extends RecyclerView.Adapter<NormTableAdapter.NormTableViewHolder> {
    private final List<NormTableItem> items;
    private final List<NormTableItem> modifiedItems = new ArrayList<>();
    
    public NormTableAdapter(List<NormTableItem> items) {
        this.items = items;
    }
    
    @NonNull
    @Override
    public NormTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_norm_table, parent, false);
        return new NormTableViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull NormTableViewHolder holder, int position) {
        NormTableItem item = items.get(position);
        holder.bind(item);
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    public List<NormTableItem> getModifiedItems() {
        return modifiedItems;
    }
    
    public void clearModifiedItems() {
        modifiedItems.clear();
        for (NormTableItem item : items) {
            item.setModified(false);
        }
        notifyDataSetChanged();
    }
    
    class NormTableViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCompetenceName;
        private final EditText etP1, etP2, etP3, etP4, etP5;
        
        public NormTableViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCompetenceName = itemView.findViewById(R.id.tvCompetenceName);
            etP1 = itemView.findViewById(R.id.etP1);
            etP2 = itemView.findViewById(R.id.etP2);
            etP3 = itemView.findViewById(R.id.etP3);
            etP4 = itemView.findViewById(R.id.etP4);
            etP5 = itemView.findViewById(R.id.etP5);
        }
        
        public void bind(NormTableItem item) {
            tvCompetenceName.setText(item.getCompetenceName());
            etP1.setText(String.valueOf(item.getP1()));
            etP2.setText(String.valueOf(item.getP2()));
            etP3.setText(String.valueOf(item.getP3()));
            etP4.setText(String.valueOf(item.getP4()));
            etP5.setText(String.valueOf(item.getP5()));
            
            // TextWatcher für Änderungen
            etP1.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        item.setP1(Double.parseDouble(s.toString()));
                        if (!modifiedItems.contains(item)) {
                            modifiedItems.add(item);
                        }
                    } catch (NumberFormatException e) {
                        // Ungültige Eingabe
                    }
                }
            });


            // TextWatcher für Änderungen
            etP2.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        item.setP2(Double.parseDouble(s.toString()));
                        if (!modifiedItems.contains(item)) {
                            modifiedItems.add(item);
                        }
                    } catch (NumberFormatException e) {
                        // Ungültige Eingabe
                    }
                }
            });


            // TextWatcher für Änderungen
            etP3.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        item.setP3(Double.parseDouble(s.toString()));
                        if (!modifiedItems.contains(item)) {
                            modifiedItems.add(item);
                        }
                    } catch (NumberFormatException e) {
                        // Ungültige Eingabe
                    }
                }
            });


            // TextWatcher für Änderungen
            etP4.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        item.setP4(Double.parseDouble(s.toString()));
                        if (!modifiedItems.contains(item)) {
                            modifiedItems.add(item);
                        }
                    } catch (NumberFormatException e) {
                        // Ungültige Eingabe
                    }
                }
            });


            // TextWatcher für Änderungen
            etP5.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        item.setP5(Double.parseDouble(s.toString()));
                        if (!modifiedItems.contains(item)) {
                            modifiedItems.add(item);
                        }
                    } catch (NumberFormatException e) {
                        // Ungültige Eingabe
                    }
                }
            });


        }
    }
    
    abstract class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    }
}