package com.example.personalitytest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalitytest.QuestionnaireActivity;
import com.example.personalitytest.R;

public class QuestionPagerAdapter extends RecyclerView.Adapter<QuestionPagerAdapter.QuestionViewHolder> {
    private final Context context;
    private final String[] questions;
    
    public QuestionPagerAdapter(Context context, String[] questions) {
        this.context = context;
        this.questions = questions;
    }
    
    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.bind(questions[position], position);
    }
    
    @Override
    public int getItemCount() {
        return questions.length;
    }
    
    class QuestionViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvQuestion;
        private final RadioGroup rgAnswers;
        private final RadioButton rbAlways, rbOften, rbRarely, rbNever;
        
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            rgAnswers = itemView.findViewById(R.id.rgAnswers);
            rbAlways = itemView.findViewById(R.id.rbAlways);
            rbOften = itemView.findViewById(R.id.rbOften);
            rbRarely = itemView.findViewById(R.id.rbRarely);
            rbNever = itemView.findViewById(R.id.rbNever);
        }
        
        public void bind(String question, int position) {
            tvQuestion.setText(question);
            
            // Antwort wiederherstellen, falls vorhanden
            int answer = ((QuestionnaireActivity) context).getCurrentAnswer(position);
            switch (answer) {
                case 4: rbAlways.setChecked(true); break;
                case 3: rbOften.setChecked(true); break;
                case 2: rbRarely.setChecked(true); break;
                case 1: rbNever.setChecked(true); break;
                default: rgAnswers.clearCheck();
            }
            
            rgAnswers.setOnCheckedChangeListener((group, checkedId) -> {
                int selectedAnswer = 0;
                if (checkedId == R.id.rbAlways) selectedAnswer = 4;
                else if (checkedId == R.id.rbOften) selectedAnswer = 3;
                else if (checkedId == R.id.rbRarely) selectedAnswer = 2;
                else if (checkedId == R.id.rbNever) selectedAnswer = 1;
                
                ((QuestionnaireActivity) context).saveAnswer(position, selectedAnswer);
            });
        }
    }
}