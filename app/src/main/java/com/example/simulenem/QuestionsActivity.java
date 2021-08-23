package com.example.simulenem;

import static com.example.simulenem.DbQuery.google_category_list;
import static com.example.simulenem.DbQuery.google_question_list;
import static com.example.simulenem.DbQuery.google_selected_category_index;
import static com.example.simulenem.DbQuery.google_selected_test_index;
import static com.example.simulenem.DbQuery.google_test_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {

    private RecyclerView questions_view;
    private TextView question_id_tv, timer_tv, category_name_tv;
    private Button submit_button, mark_button, clear_selection_button;
    private ImageButton prev_question_button, next_question_button;
    private ImageView question_list_button;
    private int questionID;
    QuestionsAdapter questions_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        init();

        questions_adapter = new QuestionsAdapter(google_question_list);
        questions_view.setAdapter(questions_adapter);

        LinearLayoutManager linear_layout_manager = new LinearLayoutManager(this);
        linear_layout_manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questions_view.setLayoutManager(linear_layout_manager);

        setSnapHelper();

        setClickListeners();

        startTimer();
    }

    private void init(){
        questions_view = findViewById(R.id.questions_view);
        question_id_tv = findViewById(R.id.tv_question_id);
        timer_tv = findViewById(R.id.tv_timer);
        category_name_tv = findViewById(R.id.qa_category_name);
        submit_button = findViewById(R.id.submit_button);
        mark_button = findViewById(R.id.mark_button);
        clear_selection_button = findViewById(R.id.clear_selection_button);
        prev_question_button = findViewById(R.id.previous_question_button);
        next_question_button = findViewById(R.id.next_question_button);
        question_list_button = findViewById(R.id.questions_list_grid_button);

        questionID = 0;
        question_id_tv.setText("1/" + google_question_list.size());
        category_name_tv.setText(google_category_list.get(google_selected_category_index).getName());
    }

    private void setSnapHelper() {

        SnapHelper snap_helper = new PagerSnapHelper();
        snap_helper.attachToRecyclerView(questions_view);

        questions_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snap_helper.findSnapView(recyclerView.getLayoutManager());
                questionID = recyclerView.getLayoutManager().getPosition(view);

                question_id_tv.setText((questionID + 1) + "/" + google_question_list.size());
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void setClickListeners(){

        prev_question_button.setOnClickListener(view -> {

            if (questionID > 0){
                questions_view.smoothScrollToPosition(questionID - 1);
            }

        });

        next_question_button.setOnClickListener(view -> {

            if (questionID < google_question_list.size() - 1){
                questions_view.smoothScrollToPosition(questionID + 1);
            }

        });

        clear_selection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                google_question_list.get(questionID).setSelected_ans(-1);
                questions_adapter.notifyDataSetChanged();
            }
        });

    }

    private void startTimer(){

        long total_time = (long) google_test_list.get(google_selected_test_index).getTime() * 60 * 1000;

        CountDownTimer timer = new CountDownTimer(total_time + 1000, 1000) {
            @Override
            public void onTick(long remaining_time) {

                String time = String.format("%02d : %02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remaining_time),
                        TimeUnit.MILLISECONDS.toSeconds(remaining_time) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remaining_time))
                );
                timer_tv.setText(time);

            }

            @Override
            public void onFinish() {

            }
        };

        timer.start();

    }
}