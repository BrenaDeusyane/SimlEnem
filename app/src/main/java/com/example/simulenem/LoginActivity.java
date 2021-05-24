package com.example.simulenem;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login_button;
    private TextView forgot_password_button, sign_up_button, dialog_text;
    private String email_string, password_string;
    private FirebaseAuth mAuth;
    private Dialog progress_dialog;
    private RelativeLayout sign_up_google_button;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 104;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login_button = findViewById(R.id.login_button);
        forgot_password_button = findViewById(R.id.forgot_password);
        sign_up_button = findViewById(R.id.sign_up_button);
        sign_up_google_button = findViewById(R.id.google_sign_up_button);

        progress_dialog = new Dialog(LoginActivity.this);
        progress_dialog.setContentView(R.layout.dialog_layout);
        progress_dialog.setCancelable(false);
        progress_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_text = progress_dialog.findViewById(R.id.dialog_text);
        dialog_text.setText("Acessando...");

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        login_button.setOnClickListener(view -> {
            if(validateData()){
                login();
            }
        });

        sign_up_button.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        sign_up_google_button.setOnClickListener(view -> googleSignUp());

    }

    private boolean validateData() {

        if (email.getText().toString().isEmpty()){
            email.setError("Insira um E-mail");
            return false;
        }

        if (password.getText().toString().isEmpty()){
            password.setError("Insira uma senha");
            return false;
        }

        return true;
    }

    private void login() {

        progress_dialog.show();

        email_string = email.getText().toString().trim();
        password_string = password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email_string, password_string)
                .addOnCompleteListener(this, (task) -> {
                    if (task.isSuccessful()) {

                       Toast.makeText(LoginActivity.this, "Login feito com Sucesso!", Toast.LENGTH_SHORT).show();

                       DbQuery.loadData(new MyCompleteListener() {
                           @Override
                           public void onSuccess() {

                               progress_dialog.dismiss();

                               Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                               startActivity(intent);
                               finish();

                           }

                           @Override
                           public void onFailure() {

                               progress_dialog.dismiss();

                               Toast.makeText(LoginActivity.this, "Algo de errado aconteceu! Por favor, tente novamente!", Toast.LENGTH_SHORT).show();

                           }
                       });

                    } else {
                        progress_dialog.dismiss();
                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void googleSignUp() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void firebaseAuthWithGoogle(String idToken) {

        progress_dialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, (task) -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this, "Login Google feito com sucesso!", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();

                        if(task.getResult().getAdditionalUserInfo().isNewUser()){

                            DbQuery.createUserData(user.getEmail(), user.getDisplayName(), new MyCompleteListener() {
                                @Override
                                public void onSuccess() {

                                    DbQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {

                                            progress_dialog.dismiss();

                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            LoginActivity.this.finish();

                                        }

                                        @Override
                                        public void onFailure() {

                                            progress_dialog.dismiss();

                                            Toast.makeText(LoginActivity.this,
                                                    "Algo de errado aconteceu! Por favor, tente novamente!", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }

                                @Override
                                public void onFailure() {

                                    progress_dialog.dismiss();

                                    Toast.makeText(LoginActivity.this,
                                            "Algo de errado aconteceu! Por favor, tente novamente!", Toast.LENGTH_SHORT).show();


                                }
                            });

                        } else {

                            DbQuery.loadData(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    progress_dialog.dismiss();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure() {

                                    progress_dialog.dismiss();

                                    Toast.makeText(LoginActivity.this,
                                            "Algo de errado aconteceu! Por favor, tente novamente!", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }

                    } else {

                        progress_dialog.dismiss();

                        Toast.makeText(LoginActivity.this,
                                Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();

                    }

                });
    }

}