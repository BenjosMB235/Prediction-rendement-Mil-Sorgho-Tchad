package com.tchad.yieldpredictor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.tchad.yieldpredictor.database.Prediction;
import com.tchad.yieldpredictor.database.PredictionDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Récupérer les données
        String region     = getIntent().getStringExtra("region");
        float rendement   = getIntent().getFloatExtra("rendement", 0f);
        String evaluation = getIntent().getStringExtra("evaluation");
        float eviMax      = getIntent().getFloatExtra("eviMax", 0f);
        float eviMean     = getIntent().getFloatExtra("eviMean", 0f);
        float pluie       = getIntent().getFloatExtra("pluie", 0f);

        // Date actuelle
        String date = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault()
        ).format(new Date());

        // Remplir les vues
        TextView tvRendement   = findViewById(R.id.tvRendement);
        TextView tvEvaluation  = findViewById(R.id.tvEvaluation);
        TextView tvRegion      = findViewById(R.id.tvRegion);
        TextView tvEvi         = findViewById(R.id.tvEvi);
        TextView tvPluie       = findViewById(R.id.tvPluie);
        TextView tvDate        = findViewById(R.id.tvDate);
        TextView tvComparaison = findViewById(R.id.tvComparaison);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        tvRendement.setText(String.format(Locale.getDefault(),
                "%.3f t/ha", rendement));
        tvEvaluation.setText(evaluation);
        tvRegion.setText(region);
        tvEvi.setText(String.format(Locale.getDefault(), "%.2f", eviMax));
        tvPluie.setText(String.format(Locale.getDefault(), "%.0f mm", pluie));
        tvDate.setText(date);
        tvComparaison.setText(String.format(Locale.getDefault(),
                "Moyenne historique %s : 0.68 t/ha", region));

        // Barre de progression (0 → 1 t/ha = 0 → 100%)
        int progress = Math.min(100, (int)(rendement * 100));
        progressBar.setProgress(progress);

        // Couleur évaluation
        if (rendement >= 0.70f) {
            tvEvaluation.setTextColor(0xFF69F0AE);
        } else if (rendement >= 0.55f) {
            tvEvaluation.setTextColor(0xFFFFD740);
        } else {
            tvEvaluation.setTextColor(0xFFFF5252);
        }

        // Bouton sauvegarder
        Button btnSauvegarder = findViewById(R.id.btnSauvegarder);
        btnSauvegarder.setOnClickListener(v -> {
            Prediction prediction = new Prediction();
            prediction.region         = region;
            prediction.eviMax         = eviMax;
            prediction.eviMean        = eviMean;
            prediction.pluie          = pluie;
            prediction.rendementPredit = rendement;
            prediction.date           = date;
            prediction.evaluation     = evaluation;

            // Sauvegarder en arrière-plan
            Executors.newSingleThreadExecutor().execute(() -> {
                PredictionDatabase.getInstance(this)
                        .predictionDao()
                        .insert(prediction);
                runOnUiThread(() -> {
                    Toast.makeText(this,
                            "✅ Prédiction sauvegardée !",
                            Toast.LENGTH_SHORT).show();
                    btnSauvegarder.setEnabled(false);
                    btnSauvegarder.setText("SAUVEGARDÉ ✓");
                });
            });
        });

        // Bouton nouvelle prédiction
        Button btnNouvelle = findViewById(R.id.btnNouvelle);
        btnNouvelle.setOnClickListener(v -> finish());
    }
}