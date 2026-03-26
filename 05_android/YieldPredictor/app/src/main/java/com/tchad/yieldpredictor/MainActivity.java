package com.tchad.yieldpredictor;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MLModel mlModel;
    private Spinner spinnerRegion;
    private EditText etEviMax, etEviMean, etPluie;
    private Button btnPredire;
    private TextView tvResultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser le modèle
        mlModel = new MLModel(this);

        // Lier les vues
        spinnerRegion = findViewById(R.id.spinnerRegion);
        etEviMax      = findViewById(R.id.etEviMax);
        etEviMean     = findViewById(R.id.etEviMean);
        etPluie       = findViewById(R.id.etPluie);
        btnPredire    = findViewById(R.id.btnPredire);
        tvResultat    = findViewById(R.id.tvResultat);

        // Remplir le spinner des régions
        String[] regions = {"Mandoul", "Moyen-Chari", "Logone-Occidental"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                regions
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerRegion.setAdapter(adapter);

        // Bouton prédire
        btnPredire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predire();
            }
        });
    }

    private void predire() {
        try {
            // Récupérer les valeurs saisies
            float eviMax  = Float.parseFloat(etEviMax.getText().toString());
            float eviMean = Float.parseFloat(etEviMean.getText().toString());
            float pluie   = Float.parseFloat(etPluie.getText().toString());

            // Valeurs moyennes pour les features non saisies
            float eviMin      = 0.20f;
            float eviIntegral = eviMean * 6;
            float eviMeanVg   = eviMean * 1.1f;
            float eviMaxFl    = eviMax  * 0.95f;
            float eviMeanSn   = eviMean * 0.85f;

            // Encodage région (One-Hot)
            String region = spinnerRegion.getSelectedItem().toString();
            float regLogone  = region.equals("Logone-Occidental") ? 1f : 0f;
            float regMandoul = region.equals("Mandoul")           ? 1f : 0f;
            float regMoyen   = region.equals("Moyen-Chari")       ? 1f : 0f;

            // Construire le vecteur de features
            float[] features = {
                    eviMax, eviMean, eviMin, eviIntegral,
                    eviMeanVg, eviMaxFl, eviMeanSn,
                    pluie,
                    regLogone, regMandoul, regMoyen
            };

            // Prédire
            float rendement = mlModel.predict(features);

            // Afficher le résultat
            tvResultat.setText(String.format(
                    "🌾 Rendement prédit : %.3f t/ha", rendement
            ));

        } catch (NumberFormatException e) {
            tvResultat.setText("⚠️ Veuillez remplir tous les champs !");
        }
    }
}