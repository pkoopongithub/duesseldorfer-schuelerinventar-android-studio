package com.example.personalitytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.personalitytest.adapters.QuestionPagerAdapter;
import com.example.personalitytest.models.Profile;

public class QuestionnaireActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private QuestionPagerAdapter adapter;
    private Button btnPrevious, btnNext;
    private TextView tvQuestionCounter;
    private ProgressBar progressBar;
    
    private static final String[] QUESTIONS = {
        	"Zuverlaessigkeit",
        	"Arbeitstempo",
                "Arbeitsplanung",
                "Organisationsfähigkeit",
                "Geschicklichkeit",
                "Ordnung",
                "Sorgfalt",
                "Kreativitaet",
                "Problemlosefaehigkeit",
                "Abstarktionsvermoegen",
                "Selbststaendigkeit",
                "Belastbarkeit",
                "Konzentrationsfaehigkeit",
                "Verantwortungsbewusstsein",
                "Eigeninitiative",
                "Leistungsbereitschaft",
                "Auffassungsgabe",
                "Merkfaehigkeit",
                "Motivationsfaehigkeit",
                "Reflektionsfaehigkeit",
                "Teamfaehigkeit",
                "Hilfsbereitschaft",
                "Kontaktfaehigkeit",
                "RespektvollerUmgang",
                "Kommunikationsfaehigkeit",
                "Einfuehlungsvermoegen",
                "Konfliktfaehigkeit",
                "Kritikfaehigkeit",
                "Schreiben",
                "Lesen",
                "Mathematik",
                "Naturwissenschaften",
                "Fremdsprachen",
                "Praesentationsfaehigkeit",
                "PC-Kenntnisse",
        	"FaecheruebergreifendesDenken"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        
        viewPager = findViewById(R.id.viewPager);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        tvQuestionCounter = findViewById(R.id.tvQuestionCounter);
        progressBar = findViewById(R.id.progressBar);
        
        adapter = new QuestionPagerAdapter(this, QUESTIONS);
        viewPager.setAdapter(adapter);
        
        setupListeners();
        updateUI();
    }

    private void setupListeners() {
        btnPrevious.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() > 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });
        
        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < QUESTIONS.length - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                finishQuestionnaire();
            }
        });
        
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateUI();
            }
        });
    }

    private void updateUI() {
        int current = viewPager.getCurrentItem() + 1;
        int total = QUESTIONS.length;
        
        tvQuestionCounter.setText(String.format("%d/%d", current, total));
        progressBar.setProgress((int) (((float) current / total) * 100));
        
        btnPrevious.setVisibility(current > 1 ? View.VISIBLE : View.INVISIBLE);
        btnNext.setText(current == total ? "Fertigstellen" : "Weiter");
    }

    public void saveAnswer(int questionIndex, int answer) {
        Profile profile = ((PersonalityTestApp) getApplication()).getCurrentProfile();
        if (profile != null) {
            profile.getAnswers()[questionIndex] = answer;
        }
    }

    private void finishQuestionnaire() {
        Profile profile = ((PersonalityTestApp) getApplication()).getCurrentProfile();
        if (profile != null) {
            profile.calculateScores(((PersonalityTestApp) getApplication()).getNormTable());

		new AsyncTask<Void, Void, Boolean>() {
    			protected Boolean doInBackground(Void... params) {
        			return saveProfileToServer(profile);
    			}
    			protected void onPostExecute(Boolean success) {
        			if (success) {
            			startActivity(new Intent(QuestionnaireActivity.this, ProfileActivity.class)
                		.putExtra("profile_id", profile.getId()));
            			finish();
        			}
    			}
		}.execute();
            
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("profile_id", profile.getId());
            startActivity(intent);
            finish();
        }
    }
}