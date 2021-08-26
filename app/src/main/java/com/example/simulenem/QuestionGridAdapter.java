package com.example.simulenem;

import static com.example.simulenem.DbQuery.ANSWERED;
import static com.example.simulenem.DbQuery.NOT_VISITED;
import static com.example.simulenem.DbQuery.REVIEW;
import static com.example.simulenem.DbQuery.UNANSWERED;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class QuestionGridAdapter extends BaseAdapter {

    private int number_of_questions;
    private Context context;

    public QuestionGridAdapter(Context context, int number_of_questions) {
        this.number_of_questions = number_of_questions;
        this.context = context;
    }

    @Override
    public int getCount() {
        return number_of_questions;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View myview;

        if ( view == null ){
            myview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_grid_item, viewGroup, false);
        }
        else {
            myview = view;
        }

        myview.setOnClickListener(view1 -> {

            if ( context instanceof QuestionsActivity )
                ((QuestionsActivity)context).goToQuestion(i);

        });

        TextView question_text_view = myview.findViewById(R.id.question_number);
        question_text_view.setText( String.valueOf(i + 1) );

        Log.d("LOGGGGGGGGGGGG", String.valueOf(DbQuery.google_question_list.get(i).getStatus()));
        switch (DbQuery.google_question_list.get(i).getStatus()){

            case NOT_VISITED :
                question_text_view.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.gray)));
                break;

            case UNANSWERED :
                question_text_view.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.red)));
                break;

            case ANSWERED :
                question_text_view.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.green)));
                break;

            case REVIEW :
                question_text_view.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.blue)));
                break;

            default:
                break;

        }

        return myview;
    }
}
