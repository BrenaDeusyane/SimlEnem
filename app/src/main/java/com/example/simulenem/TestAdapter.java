package com.example.simulenem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private List<TestModel> test_list;

    public TestAdapter(List<TestModel> test_list) {
        this.test_list = test_list;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        int progress = test_list.get(position).getTopScore();
        holder.setData(position, progress);
    }

    @Override
    public int getItemCount() {
        return test_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView test_number, top_score;
        private ProgressBar progress_bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            test_number = itemView.findViewById(R.id.test_number);
            top_score = itemView.findViewById(R.id.score_test);
            progress_bar = itemView.findViewById(R.id.test_progress_bar);

        }

        private void setData(int position, int progress){

            test_number.setText("Simulado Nº: " + String.valueOf(position + 1));
            top_score.setText(String.valueOf(progress) + " %");
            progress_bar.setProgress(progress);

        }
    }
}
