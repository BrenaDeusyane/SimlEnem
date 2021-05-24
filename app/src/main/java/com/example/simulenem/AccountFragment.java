package com.example.simulenem;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;

import static com.example.simulenem.R.id.path;
import static com.example.simulenem.R.id.username_update;
import static com.google.firebase.firestore.FieldValue.delete;

/**
 * A simple {@link Fragment} subclass.
 */

public class AccountFragment extends Fragment {

    private EditText name;
    private Button logout_button, update_button, delete_user_button;
    private Dialog progress_dialog;
    private TextView dialog_text;
    private FirebaseUser user;
    private String name_string;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        name = view.findViewById(username_update);
        logout_button = view.findViewById(R.id.logout_button);
        update_button = view.findViewById(R.id.update_button);
        delete_user_button = view.findViewById(R.id.delete_user_button);

        progress_dialog = new Dialog(getContext());
        progress_dialog.setContentView(R.layout.dialog_layout);
        progress_dialog.setCancelable(false);
        progress_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_text = progress_dialog.findViewById(R.id.dialog_text);
        dialog_text.setText("Carregando...");

        user = FirebaseAuth.getInstance().getCurrentUser();

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleClient = GoogleSignIn.getClient(getContext(), gso);

                mGoogleClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        getActivity().finish();

                    }
                });
            }
        });

        delete_user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress_dialog.show();

                DbQuery.deleteData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {

                        progress_dialog.dismiss();

                    }

                    @Override
                    public void onFailure() {

                        progress_dialog.dismiss();

                        Toast.makeText(getContext(), "Algo de errado aconteceu! Por favor, tente novamente!", Toast.LENGTH_SHORT).show();

                    }
                });

                user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getContext(), "Conta apagada com sucesso!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if ( validateData() ) {

                    getUpdateData(name_string);

                }

            }

        });

        return view;

    }

    private boolean validateData(){
        name_string = name.getText().toString().trim();

        if (name_string.isEmpty()){
            name.setError("Insira seu nome");
            return false;
        }

        return true;

    }

    private void getUpdateData(String name_string) {
        progress_dialog.show();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name_string)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getContext(), "Atualização feita com sucesso!", Toast.LENGTH_SHORT).show();
                            DbQuery.updateData(name_string, new MyCompleteListener() {

                                @Override
                                public void onSuccess() {
                                    progress_dialog.dismiss();

                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();

                                }

                                @Override
                                public void onFailure() {

                                    progress_dialog.dismiss();

                                    Toast.makeText(getContext(), "Algo de errado aconteceu! Por favor, tente novamente!", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }

                        else {

                            progress_dialog.dismiss();

                            Toast.makeText(getContext(),
                                    "Algo de errado aconteceu! Por favor, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    }