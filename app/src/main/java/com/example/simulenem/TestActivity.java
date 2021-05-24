package com.example.simulenem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView test_view;
    private Toolbar toolbar;
    private TestAdapter test_adapter;
    private Dialog progress_dialog;
    private TextView dialog_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle(DbQuery.google_category_list.get(DbQuery.google_selected_category_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        test_view = findViewById(R.id.test_recycler_view);

        progress_dialog = new Dialog(TestActivity.this);
        progress_dialog.setContentView(R.layout.dialog_layout);
        progress_dialog.setCancelable(false);
        progress_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_text = progress_dialog.findViewById(R.id.dialog_text);
        dialog_text.setText("Carregando...");

        progress_dialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        test_view.setLayoutManager(layoutManager);

        DbQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                test_adapter = new TestAdapter(DbQuery.google_test_list);
                test_view.setAdapter(test_adapter);

                progress_dialog.dismiss();

            }

            @Override
            public void onFailure() {

                progress_dialog.dismiss();

                Toast.makeText(TestActivity.this, "Algo de errado aconteceu! Por favor, tente novamente!", Toast.LENGTH_SHORT).show();


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        
        if (item.getItemId() == android.R.id.home){
            TestActivity.this.finish();
        }
        
        return super.onOptionsItemSelected(item);
    }
}