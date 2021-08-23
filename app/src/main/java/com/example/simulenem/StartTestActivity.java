package com.example.simulenem;

import static com.example.simulenem.DbQuery.google_category_list;
import static com.example.simulenem.DbQuery.loadQuestions;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartTestActivity extends AppCompatActivity {

    private TextView category_name, test_number, total_questions, best_score, time;
    private Button start_test_button;
    private ImageView back_button;

    private Dialog progress_dialog;
    private TextView dialog_text;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        init();

        progress_dialog = new Dialog(StartTestActivity.this);
        progress_dialog.setContentView(R.layout.dialog_layout);
        progress_dialog.setCancelable(false);
        progress_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_text = progress_dialog.findViewById(R.id.dialog_text);
        dialog_text.setText("Carregando...");

        progress_dialog.show();

        loadQuestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                setData();

                progress_dialog.dismiss();

            }

            @Override
            public void onFailure() {

                progress_dialog.dismiss();
                Toast.makeText(StartTestActivity.this, "Algo de errado aconteceu! Por favor, tente novamente!",
                        Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void init(){
        category_name = findViewById(R.id.start_category_name);
        test_number = findViewById(R.id.start_test_number);
        total_questions = findViewById(R.id.start_total_questions);
        best_score = findViewById(R.id.start_best_score);
        time = findViewById(R.id.start_time);
        start_test_button = findViewById(R.id.start_test_button);
        back_button = findViewById(R.id.start_back_button);

        back_button.setOnClickListener(view -> StartTestActivity.this.finish());

        start_test_button.setOnClickListener(view -> {
            Intent intent = new Intent(StartTestActivity.this, QuestionsActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @SuppressLint("SetTextI18n")
    private void setData(){

        category_name.setText(google_category_list.get(DbQuery.google_selected_category_index).getName());
        test_number.setText((DbQuery.google_selected_test_index + 1) + "º Simulado");
        total_questions.setText(String.valueOf(DbQuery.google_question_list.size()));
        best_score.setText(String.valueOf(DbQuery.google_test_list.get(DbQuery.google_selected_test_index).getTopScore()));
        time.setText(String.valueOf(DbQuery.google_test_list.get(DbQuery.google_selected_test_index).getTime()));

    }

}