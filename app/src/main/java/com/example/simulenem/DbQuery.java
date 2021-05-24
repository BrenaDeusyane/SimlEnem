package com.example.simulenem;

import android.util.ArrayMap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import androidx.annotation.NonNull;


import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore google_firestore;
    public static List<CategoryModel> google_category_list = new ArrayList<>();
    public static List<TestModel> google_test_list = new ArrayList<>();
    public static int google_selected_category_index = 0;
    public static ProfileModel my_profile = new ProfileModel("na", null, null);

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void createUserData(String email, String name, final MyCompleteListener complete_listener) {

        Map<String, Object> user_data = new ArrayMap<>();
        user_data.put("email_id", email);
        user_data.put("name", name);
        user_data.put("total_score", 0);

        DocumentReference user_document = google_firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = google_firestore.batch();
        batch.set(user_document, user_data);

        DocumentReference count_document = google_firestore.collection("users").document("total_users");
        batch.update(count_document, "count", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                    complete_listener.onSuccess();
                })
                .addOnFailureListener((e) -> {
                    complete_listener.onFailure();
                });

    }

    public static void getUserData(MyCompleteListener complete_listener) {

        google_firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        my_profile.setName(documentSnapshot.getString("name"));
                        my_profile.setEmail(documentSnapshot.getString("email_id"));
                        my_profile.setPassword(documentSnapshot.getString("password"));

                        complete_listener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        complete_listener.onFailure();

                    }
                });
    }

    public static void loadCategories(final MyCompleteListener complete_listener) {

        google_category_list.clear();

        google_firestore.collection("questions").get()

                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Map<String, QueryDocumentSnapshot> document_list = new ArrayMap<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            document_list.put(document.getId(), document);
                        }

                        QueryDocumentSnapshot category_list_document = document_list.get("Categories");

                        long category_count = category_list_document.getLong("count");

                        for (int i = 1; i <= category_count; i++) {

                            String category_id = category_list_document.getString("cat" + i + "_id");

                            QueryDocumentSnapshot category_document = document_list.get(category_id);

                            int number_of_test = category_document.getLong("number_of_test").intValue();

                            String category_name = category_document.getString("name");

                            google_category_list.add(new CategoryModel(category_id, category_name, number_of_test));

                        }

                        complete_listener.onSuccess();

                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        complete_listener.onFailure();

                    }
                });

    }

    public static void loadTestData(MyCompleteListener complete_listener) {

        google_test_list.clear();

        google_firestore.collection("questions").document(google_category_list.get(google_selected_category_index).getDocumentID())
                .collection("tests_list").document("tests_info")
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    int number_of_tests = google_category_list.get(google_selected_category_index).getNumOfTests();

                    for (int i = 1; i <= number_of_tests; i++) {

                        google_test_list.add(new TestModel(
                                documentSnapshot.getString("test" + i + "_id"),
                                0,
                                documentSnapshot.getLong("test" + i + "_time").intValue()
                        ));

                    }

                    complete_listener.onSuccess();

                })
                .addOnFailureListener(e -> complete_listener.onFailure());

    }

    public static void loadData(MyCompleteListener complete_listener) {

        loadCategories(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                getUserData(complete_listener);
            }

            @Override
            public void onFailure() {

                complete_listener.onFailure();

            }
        });
    }

    public static void updateData(String name_update, MyCompleteListener complete_listener) {

        DocumentReference user_document = google_firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = google_firestore.batch();

        batch.update(user_document, "name", String.valueOf(name_update));

        batch.commit()
                .addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                    complete_listener.onSuccess();
                })
                .addOnFailureListener((e) -> {
                    complete_listener.onFailure();
                });

    }

    public static void deleteData(MyCompleteListener complete_listener) {

        google_firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).delete()
                .addOnSuccessListener((aVoid) -> complete_listener.onSuccess())
                .addOnFailureListener((e) -> complete_listener.onFailure());

    }

}
