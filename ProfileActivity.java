public class ProfileActivity extends AppCompatActivity {
    private int profileId;
    private Profile currentProfile;
    private ProgressBar[] competenceBars = new ProgressBar[6];
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        profileId = getIntent().getIntExtra("profile_id", 0);
        if (profileId == 0) {
            finish();
            return;
        }
        
        setupUI();
        loadProfileData();
    }
    
    private void setupUI() {
        competenceBars[0] = findViewById(R.id.progressBarKompetenz1);
        competenceBars[1] = findViewById(R.id.progressBarKompetenz2);
	competenceBars[2] = findViewById(R.id.progressBarKompetenz3);
	competenceBars[3] = findViewById(R.id.progressBarKompetenz4);
	competenceBars[4] = findViewById(R.id.progressBarKompetenz5);
	competenceBars[5] = findViewById(R.id.progressBarKompetenz6);
        
        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> 
            startActivity(new Intent(this, QuestionnaireActivity.class)
                .putExtra("profile_id", profileId)));
    }
    
    private void loadProfileData() {
        // Profildaten laden (vom Server oder lokal)
        // Normtabelle laden
        // Ergebnisse berechnen und anzeigen
    }
    private void loadProfileData() {
     Profile profile = ((PersonalityTestApp) getApplication()).getCurrentProfile();
      if (profile != null) {
         int[] scores = profile.getCompetenceScores();
        
         for (int i = 0; i < competenceBars.length; i++) {
             competenceBars[i].setProgress(scores[i] * 20); // 1-5 zu 20-100%
         }
        
        // Optional: TextViews für Score-Anzeige
        //TextView tvScore1 = findViewById(R.id.tvScore1);
        //tvScore1.setText(String.valueOf(scores[0]));
        // ... für alle 6 Kompetenzen
    }
}
}