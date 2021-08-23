package com.example.simulenem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<QuestionModel> questions_list;

    public QuestionsAdapter(List<QuestionModel> questions_list) {
        this.questions_list = questions_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);

    }

    @Override
    public int getItemCount() {
        return questions_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView question;
        private Button option_a, option_b, option_c, option_d, option_e, prev_selected_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.tv_question);
            option_a = itemView.findViewById(R.id.option_a);
            option_b = itemView.findViewById(R.id.option_b);
            option_c = itemView.findViewById(R.id.option_c);
            option_d = itemView.findViewById(R.id.option_d);
            option_e = itemView.findViewById(R.id.option_e);

            prev_selected_button = null;
        }

        private void setData(final int position){
            question.setText(questions_list.get(position).getQuestion());
            option_a.setText(questions_list.get(position).getOption_a());
            option_b.setText(questions_list.get(position).getOption_b());
            option_c.setText(questions_list.get(position).getOption_c());
            option_d.setText(questions_list.get(position).getOption_d());
            option_e.setText(questions_list.get(position).getOption_e());

            setOption(option_a, 1, position);
            setOption(option_b, 2, position);
            setOption(option_c, 3, position);
            setOption(option_d, 4, position);
            setOption(option_e, 5, position);

            option_a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(option_a, 1, position);
                }
            });

            option_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(option_b, 2, position);
                }
            });

            option_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(option_c, 3, position);
                }
            });

            option_d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(option_d, 4, position);
                }
            });

            option_e.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(option_e, 5, position);
                }
            });
        }

        private void selectOption(Button button, int option_num, int questionID){

            if (prev_selected_button == null){
                button.setBackgroundResource(R.drawable.selected_button);
                DbQuery.google_question_list.get(questionID).setSelected_ans(option_num);

                prev_selected_button = button;
            }
            else {

                if (prev_selected_button.getId() == button.getId()){
                    button.setBackgroundResource(R.drawable.unselected_button);
                    DbQuery.google_question_list.get(questionID).setSelected_ans(-1);

                    prev_selected_button = null;
                }
                else {
                    prev_selected_button.setBackgroundResource(R.drawable.unselected_button);
                    button.setBackgroundResource(R.drawable.selected_button);

                    DbQuery.google_question_list.get(questionID).setSelected_ans(option_num);

                    prev_selected_button = button;
                }

            }

        }

        private void setOption(Button button, int option_num, int questionID){

            if ( DbQuery.google_question_list.get(questionID).getSelected_ans() == option_num){
                button.setBackgroundResource(R.drawable.selected_button);
            }
            else{
                button.setBackgroundResource(R.drawable.unselected_button);
            }

        }

    }
}
