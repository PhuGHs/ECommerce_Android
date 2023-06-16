package com.example.ecommerce_hvpp.repositories.adminRepositories;

import static com.example.ecommerce_hvpp.util.CustomFormat.dateFormatter;
import static com.example.ecommerce_hvpp.util.CustomFormat.templateDate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Promotion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdminPromotionRepository {
    NavController navController;
    private final FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

    public AdminPromotionRepository() {}

    public void onClickAddPromotion(View view) {
        navController = Navigation.findNavController(view);
        navController.navigate(R.id.adminAddPromotionFragment);
    }

    public View.OnClickListener onClickBackPage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        };
    }

    public void returnBackPage(View view) {
        NavController navController = Navigation.findNavController(view);
        navController.popBackStack();
    }

    public Map<String, Object> convertObjectToMapIsUsed(Promotion promotion) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", promotion.getId());
        data.put("name", promotion.getName());
        data.put("value", promotion.getValue());
        data.put("condition", promotion.getCondition());
        data.put("date_begin", promotion.getDate_begin());
        data.put("date_end", promotion.getDate_end());
        data.put("apply_for", promotion.getApply_for());
        data.put("isUsed", promotion.isUsed());
        return data;
    }

    public Map<String, Object> convertObjectToMap(Promotion promotion) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", promotion.getId());
        data.put("name", promotion.getName());
        data.put("value", promotion.getValue());
        data.put("condition", promotion.getCondition());
        data.put("date_begin", promotion.getDate_begin());
        data.put("date_end", promotion.getDate_end());
        data.put("apply_for", promotion.getApply_for());
        return data;
    }

    public void insertPromotionDatabase(Promotion promotion) {
        CollectionReference promotionsRef = firebaseHelper.getCollection("Voucher");

        // set the data in Firestore
        promotionsRef.document(promotion.getId()).set(convertObjectToMap(promotion), SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FirestoreDemo", "Promotion added/updated with ID: " + promotion.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FirestoreDemo", "Error adding/updating promotion", e);
                    }
                });
    }

    public void insertVoucherForUsers(Promotion promotion) {
        CollectionReference usersRef = firebaseHelper.getCollection("users");

        // set the data in firestore
        usersRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        CollectionReference vouchersRef = document.getReference().collection("vouchers");

                        vouchersRef.document(promotion.getId()).set(convertObjectToMapIsUsed(promotion), SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("VuSubCollection", "name : " + promotion.getName());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("VuError", "" + e);
                                    }
                                });
                    }

                })
                .addOnFailureListener(e -> {
                    Log.e("VuError", "" + e.getMessage());
                });
    }

//    public void insertVoucherForNewUser() {
//        CollectionReference usersRef = firebaseHelper.getCollection("users");
//
//        // set the data in firestore
//        usersRef.get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                        CollectionReference vouchersRef = document.getReference().collection("vouchers");
//
//                        vouchersRef.document(promotion.getId()).set(convertObjectToMapIsUsed(promotion), SetOptions.merge())
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Log.d("VuSubCollection", "name : " + promotion.getName());
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.e("VuError", "" + e);
//                                    }
//                                });
//                    }
//
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("VuError", "" + e.getMessage());
//                });
//    }

    public void getVouchersOfUsers() {
        CollectionReference usersRef = firebaseHelper.getCollection("users");

        usersRef.get()
                .addOnCompleteListener(task -> {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference userRef = document.getReference();

                            CollectionReference vouchersRef = userRef.collection("vouchers");
                            vouchersRef.get()
                                    .addOnCompleteListener(subTask -> {
                                        for (QueryDocumentSnapshot snapshot : subTask.getResult()) {
                                            Promotion promotion = snapshot.toObject(Promotion.class);
                                            Log.e("VuSub", promotion.getName());
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("VuError", "" + e.getMessage());
                                    });

                        }
                })
                .addOnFailureListener(e -> {
                    Log.e("VuError", "" + e.getMessage());
                });

    }

    public void updatePromotionDatabase(Promotion promotion) {
        CollectionReference promotionsRef = firebaseHelper.getCollection("Voucher");

        // update the data in Firestore
        promotionsRef.document(promotion.getId()).update(convertObjectToMap(promotion))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FirestoreDemo", "Promotion added/updated with ID: " + promotion.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FirestoreDemo", "Error adding/updating promotion", e);
                    }
                });
    }

    public void updateVoucherForUsers(Promotion promotion) {
        CollectionReference usersRef = firebaseHelper.getCollection("users");

        // set the data in firestore
        usersRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        CollectionReference vouchersRef = document.getReference().collection("vouchers");

                        vouchersRef.document(promotion.getId()).update(convertObjectToMap(promotion))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("VuSubCollection", "name : " + promotion.getName());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("VuError", "" + e);
                                    }
                                });
                    }

                })
                .addOnFailureListener(e -> {
                    Log.e("VuError", "" + e.getMessage());
                });
    }
    public View.OnClickListener createDatePickerDialog(Context mContext) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleEventDatePickerDialog(mContext, (EditText) view);
            }
        };
    }

    // handle date picker click
    public void handleEventDatePickerDialog(Context mContext, EditText edtDate) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String selectedDate = edtDate.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(selectedDate);
            c.setTime(date);
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int currMonth = monthOfYear + 1;
                        @SuppressLint("DefaultLocale") String formattedDay = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                        @SuppressLint("DefaultLocale") String formattedMonth = String.format(Locale.getDefault(), "%02d", currMonth);
                        edtDate.setText(formattedDay + "/" + formattedMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public boolean isEmpty(EditText mEditText) {
        return mEditText.getText().toString().trim().isEmpty();
    }

    public boolean isValidatedDate(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, dateFormatter);
        LocalDate end = LocalDate.parse(endDate, dateFormatter);
        return start.isBefore(end);
    }
}
