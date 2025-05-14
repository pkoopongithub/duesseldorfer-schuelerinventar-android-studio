package com.example.personalitytest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalitytest.QuestionnaireActivity;
import com.example.personalitytest.R;

public class QuestionFragment extends Fragment {
    private static final String ARG_QUESTION = "question";
    private static final String ARG_POSITION = "position";
    
    private String question;
    private int position;
    
    public static QuestionFragment newInstance(String question, int position) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getString(ARG_QUESTION);
            position = getArguments().getInt(ARG_POSITION);
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        
        TextView tvQuestion = view.findViewById(R.id.tvQuestion);
        RadioGroup rgAnswers = view.findViewById(R.id.rgAnswers);
        RadioButton rbAlways = view.findViewById(R.id.rbAlways);
        RadioButton rbOften = view.findViewById(R.id.rbOften);
        RadioButton rbRarely = view.findViewById(R.id.rbRarely);
        RadioButton rbNever = view.findViewById(R.id.rbNever);
        
        tvQuestion.setText(question);
        
        // Vorauswahl wiederherstellen
        int answer = ((QuestionnaireActivity) requireActivity()).getCurrentAnswer(position);
        switch (answer) {
            case 4: rbAlways.setChecked(true); break;
            case 3: rbOften.setChecked(true); break;
            case 2: rbRarely.setChecked(true); break;
            case 1: rbNever.setChecked(true); break;
        }
        
        rgAnswers.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedAnswer = 0;
            if (checkedId == R.id.rbAlways) selectedAnswer = 4;
            else if (checkedId == R.id.rbOften) selectedAnswer = 3;
            else if (checkedId == R.id.rbRarely) selectedAnswer = 2;
            else if (checkedId == R.id.rbNever) selectedAnswer = 1;
            
            ((QuestionnaireActivity) requireActivity()).saveAnswer(position, selectedAnswer);
        });
        
        return view;
    }
}