package com.tchad.yieldpredictor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class PredictionFragment extends Fragment {

    private MLModel mlModel;
    private Spinner spinnerRegion;
    private EditText etEviMax, etEviMean, etPluie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prediction, container, false);

        // Initialiser le modèle
        mlModel = new MLModel(requireContext());

        // Lier les vues
        spinnerRegion = view.findViewById(R.id.spinnerRegion);
        etEviMax      = view.findViewById(R.id.etEviMax);
        etEviMean     = view.findViewById(R.id.etEviMean);
        etPluie       = view.findViewById(R.id.etPluie);
        Button btnPredire = view.findViewById(R.id.btnPredire);

        // Remplir le spinner
        String[] regions = {"Mandoul", "Moyen-Chari", "Logone-Occidental"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                regions
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerRegion.setAdapter(adapter);

        // Bouton prédire
        btnPredire.setOnClickListener(v -> predire());

        return view;
    }

    private void predire() {
        try {
            float eviMax  = Float.parseFloat(etEviMax.getText().toString());
            float eviMean = Float.parseFloat(etEviMean.getText().toString());
            float pluie   = Float.parseFloat(etPluie.getText().toString());

            // Valeurs dérivées
            float eviMin      = 0.20f;
            float eviIntegral = eviMean * 6;
            float eviMeanVg   = eviMean * 1.1f;
            float eviMaxFl    = eviMax  * 0.95f;
            float eviMeanSn   = eviMean * 0.85f;

            // Encodage région
            String region    = spinnerRegion.getSelectedItem().toString();
            float regLogone  = region.equals("Logone-Occidental") ? 1f : 0f;
            float regMandoul = region.equals("Mandoul")           ? 1f : 0f;
            float regMoyen   = region.equals("Moyen-Chari")       ? 1f : 0f;

            float[] features = {
                    eviMax, eviMean, eviMin, eviIntegral,
                    eviMeanVg, eviMaxFl, eviMeanSn,
                    pluie, regLogone, regMandoul, regMoyen
            };

            float rendement = mlModel.predict(features);

            // Évaluation
            String evaluation;
            if (rendement >= 0.70f)      evaluation = "BONNE RÉCOLTE";
            else if (rendement >= 0.55f) evaluation = "RÉCOLTE MOYENNE";
            else                         evaluation = "RÉCOLTE FAIBLE";

            // Passer à ResultActivity
            Intent intent = new Intent(requireContext(), ResultActivity.class);
            intent.putExtra("region",     region);
            intent.putExtra("rendement",  rendement);
            intent.putExtra("evaluation", evaluation);
            intent.putExtra("eviMax",     eviMax);
            intent.putExtra("eviMean",    eviMean);
            intent.putExtra("pluie",      pluie);
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(),
                    "Veuillez remplir tous les champs !",
                    Toast.LENGTH_SHORT).show();
        }
    }
}