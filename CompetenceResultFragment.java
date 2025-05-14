package com.example.personalitytest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalitytest.R;

public class CompetenceResultFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_SCORE = "score";
    
    private String title;
    private int score;
    
    public static CompetenceResultFragment newInstance(String title, int score) {
        CompetenceResultFragment fragment = new CompetenceResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_SCORE, score);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            score = getArguments().getInt(ARG_SCORE);
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competence_result, container, false);
        
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView tvScore = view.findViewById(R.id.tvScore);
        
        tvTitle.setText(title);
        progressBar.setProgress(score * 20); // 1-5 zu 20-100%
        tvScore.setText(String.valueOf(score));
        
        return view;
    }
}