package com.tchad.yieldpredictor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tchad.yieldpredictor.database.Prediction;
import java.util.List;
import java.util.Locale;

public class PredictionAdapter extends
        RecyclerView.Adapter<PredictionAdapter.ViewHolder> {

    private List<Prediction> predictions;

    public PredictionAdapter(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prediction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Prediction p = predictions.get(position);

        holder.tvRegion.setText(p.region);
        holder.tvDate.setText(p.date);
        holder.tvEvaluation.setText(p.evaluation);
        holder.tvRendement.setText(String.format(
                Locale.getDefault(), "%.2f\nt/ha", p.rendementPredit));

        // Couleur indicateur selon évaluation
        int color;
        if (p.rendementPredit >= 0.70f)      color = 0xFF4CAF50;
        else if (p.rendementPredit >= 0.55f) color = 0xFFFFC107;
        else                                 color = 0xFFF44336;

        holder.viewIndicator.setBackgroundColor(color);
        holder.tvEvaluation.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return predictions == null ? 0 : predictions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRegion, tvDate, tvEvaluation, tvRendement;
        View viewIndicator;

        ViewHolder(View itemView) {
            super(itemView);
            tvRegion      = itemView.findViewById(R.id.tvItemRegion);
            tvDate        = itemView.findViewById(R.id.tvItemDate);
            tvEvaluation  = itemView.findViewById(R.id.tvItemEvaluation);
            tvRendement   = itemView.findViewById(R.id.tvItemRendement);
            viewIndicator = itemView.findViewById(R.id.viewIndicator);
        }
    }
}