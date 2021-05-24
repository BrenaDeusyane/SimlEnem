package com.example.simulenem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, password, confirm_password;
    private Button sing_up_button;
    private ImageView back_button;
    private FirebaseAuth mAuth;
    private String name_string, email_string, password_string, confirm_password_string;
    private Dialog progress_dialog;
    private TextView dialog_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confim_password);  
        sing_up_button = findViewById(R.id.sign_up_button);
        back_button = findViewById(R.id.back_button);

        progress_dialog = new Dialog(SignUpActivity.this);
        progress_dialog.setContentView(R.layout.dialog_layout);
        progress_dialog.setCancelable(false);
        progress_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_text = progress_dialog.findViewById(R.id.dialog_text);
        dialog_text.setText("Cadastrando usuário...");

        mAuth = FirebaseAuth.getInstance();

        back_button.setOnClickListener(view -> finish());

        sing_up_button.setOnClickListener(view -> {

            if (validate()){
                signUpNewUser();
            }

        });

    }

    private boolean validate() {
        name_string = name.getText().toString().trim();
        email_string = email.getText().toString().trim();
        password_string = password.getText().toString().trim();
        confirm_password_string = confirm_password.getText().toString().trim();

        if (name_string.isEmpty()){
            name.setError("Insira seu nome");
            return false;
        }

        if (email_string.isEmpty()){
            email.setError("Insira um email");
            return false;
        }

        if (password_string.isEmpty()){
            password.setError("Insira uma senha");
            return false;
        }

        if (confirm_password_string.isEmpty()){
            confirm_password.setError("Repita a senha");
            return false;
        }

        if (password_string.compareTo(confirm_password_string) != 0){
            Toast.makeText(SignUpActivity.this, "As senhas estão diferentes!", Toast.LENGTH_SHORT ).show();
            return false;
        }

        return true;
    }

    @SuppressWarnings("Convert2Lambda")
    private void signUpNewUser() {

        progress_dialog.show();

        mAuth.createUserWithEmailAndPassword(email_string, password_string)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(SignUpActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                            DbQuery.createUserData(email_string, name_string
                                    , new MyCompleteListener() {

                                @Override
                                public void onSuccess() {

                                    DbQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {

                                            progress_dialog.dismiss();

                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            SignUpActivity.this.finish();

                                        }

                                        @Override
                                        public void onFailure() {

                                            progress_dialog.dismiss();

                                            Toast.makeText(SignUpActivity.this,
                                                    "Algo de errado aconteceu! Por favor, tente novamente!", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }

                                @Override
                                public void onFailure() {

                                    progress_dialog.dismiss();

                                    Toast.makeText(SignUpActivity.this,
                                            "Algo de errado aconteceu! Por favor, tente novamente!", Toast.LENGTH_SHORT).show();

                                }
                            });

                        } else {

                            progress_dialog.dismiss();

                            Toast.makeText(SignUpActivity.this, "Falha na autentificação", Toast.LENGTH_SHORT).show();

                        }

                    }

                });
    }
}