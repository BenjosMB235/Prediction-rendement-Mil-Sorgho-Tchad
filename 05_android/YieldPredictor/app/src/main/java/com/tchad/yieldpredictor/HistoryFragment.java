package com.tchad.yieldpredictor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tchad.yieldpredictor.database.PredictionDatabase;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_history, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        TextView tvEmpty          = view.findViewById(R.id.tvEmpty);

        // Configurer la liste
        PredictionAdapter adapter = new PredictionAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Observer la base de données
        PredictionDatabase.getInstance(requireContext())
                .predictionDao()
                .getAllPredictions()
                .observe(getViewLifecycleOwner(), predictions -> {
                    if (predictions == null || predictions.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);
                        adapter.setPredictions(predictions);
                    }
                });

        return view;
    }
}