package com.example.simulenem;

import android.content.Intent;
import android.graphics.Typeface;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView appName = findViewById(R.id.app_name);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        appName.setTypeface(typeface);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myanim);
        appName.setAnimation(anim);

        mAuth = FirebaseAuth.getInstance();

        DbQuery.google_firestore = FirebaseFirestore.getInstance();

        new Thread(){

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run(){

                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mAuth.getCurrentUser() != null){

                    DbQuery.loadData(new MyCompleteListener() {

                        @Override
                        public void onSuccess() {

                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                            
                        }

                        @Override
                        public void onFailure() {

                            Toast.makeText(SplashActivity.this, "Algo de errado aconteceu! Por favor, tente novamente!", Toast.LENGTH_SHORT).show();

                        }

                    });

                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

            }

        }.start();

    }

}