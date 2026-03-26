package com.tchad.yieldpredictor;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.Interpreter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;

public class MLModel {

    private Interpreter interpreter;
    private float[] scalerMean;
    private float[] scalerScale;

    public MLModel(Context context) {
        try {
            // Charger le modèle TFLite
            interpreter = new Interpreter(loadModelFile(context));

            // Charger les paramètres du scaler
            loadScalerParams(context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Charger le fichier .tflite depuis assets
    private MappedByteBuffer loadModelFile(Context context) throws Exception {
        AssetFileDescriptor fileDescriptor = context.getAssets()
                .openFd("modele_rendement.tflite");
        FileInputStream inputStream = new FileInputStream(
                fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        return fileChannel.map(
                FileChannel.MapMode.READ_ONLY,
                fileDescriptor.getStartOffset(),
                fileDescriptor.getDeclaredLength()
        );
    }

    // Charger les paramètres de normalisation
    private void loadScalerParams(Context context) throws Exception {
        InputStream is = context.getAssets().open("scaler_params.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);

        JSONObject json = new JSONObject(sb.toString());
        JSONArray meanArray  = json.getJSONArray("mean");
        JSONArray scaleArray = json.getJSONArray("scale");

        scalerMean  = new float[meanArray.length()];
        scalerScale = new float[scaleArray.length()];

        for (int i = 0; i < meanArray.length(); i++) {
            scalerMean[i]  = (float) meanArray.getDouble(i);
            scalerScale[i] = (float) scaleArray.getDouble(i);
        }
    }

    // Normaliser les features
    private float[] normalize(float[] features) {
        float[] normalized = new float[features.length];
        for (int i = 0; i < features.length; i++) {
            normalized[i] = (features[i] - scalerMean[i]) / scalerScale[i];
        }
        return normalized;
    }

    // Prédire le rendement
    public float predict(float[] features) {
        float[] normalized = normalize(features);

        // Préparer les tenseurs
        float[][] input  = new float[1][11];
        float[][] output = new float[1][1];
        input[0] = normalized;

        // Inférence
        interpreter.run(input, output);
        return output[0][0];
    }
}