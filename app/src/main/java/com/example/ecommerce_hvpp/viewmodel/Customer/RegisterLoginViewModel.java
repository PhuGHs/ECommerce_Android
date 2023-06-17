package com.example.ecommerce_hvpp.viewmodel.Customer;

import static com.example.ecommerce_hvpp.util.CustomFormat.dateFormatter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Promotion;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;
import com.example.ecommerce_hvpp.repositories.customerRepositories.UserRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterLoginViewModel extends ViewModel {
    private final static String TAG = "RegisterLoginViewModel";
    private FirebaseAuth firebaseAuth;
    private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserRepository repo = new UserRepository();
    private MutableLiveData<Resource<User>> _register = new MutableLiveData<>();
    private MutableLiveData<Resource<Void>> resetPasswordResult = new MutableLiveData<>();
    private MutableLiveData<Resource<User>> _mldUser = new MutableLiveData<>();
    public LiveData<Resource<Void>> getResetPasswordResult() {
        return resetPasswordResult;
    }
    public String UID;
    private boolean isNew = true;

    public LiveData<Resource<User>> registerUser(String email, String password, String username) {
        _register.setValue(Resource.loading(null));
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        UID = user.getUid();
                        createUser(UID, new User(UID, email, password, username));
                    } else {
                        String errMessage = task.getException().getMessage();
                        _register.setValue(Resource.error(errMessage, null));
                    }
                });
        return _register;
    }

    public void createUser(String userUID, User user) {
        db.collection("users")
                .document(userUID)
                .set(user)
                .addOnSuccessListener(value -> {
                    _register.setValue(Resource.success(user));
                })
                .addOnFailureListener(ex -> {
                    _register.setValue(Resource.error(ex.getMessage(), null));
                });
    }

    public LiveData<Resource<User>> loginUser(String email, String password) {
        _register.setValue(Resource.loading(null));
        firebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
                        _register.setValue(Resource.success(repo.getUser(fbUser.getUid()).getValue().data));
                    } else {
                        _register.setValue(Resource.error(task.getException().getMessage(), null));
                    }
                });
        return _register;
    }

    public LiveData<Resource<Void>> resetUserPassword(String email) {
        resetPasswordResult.setValue(Resource.loading(null));
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener(authResult -> {
                    resetPasswordResult.setValue(Resource.success(null));
                })
                .addOnFailureListener(error -> {
                    resetPasswordResult.setValue(Resource.error(error.getMessage(), null));
                });
        return resetPasswordResult;
    }

    public void insertVoucherForNewUser() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference userRef = firebaseHelper.getCollection("users").document(fbUser.getUid());

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        isNew = document.getBoolean("new");
                        // Use the value of "new" field
                        if (isNew) {
                            firebaseHelper
                                    .getCollection("Voucher")
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                            Timestamp end = snapshot.getTimestamp("date_end");
                                            String endDate = getDate(end.getSeconds()*1000);
                                            if (!isExpired(endDate)) {
                                                Promotion promotion = snapshot.toObject(Promotion.class);
                                                userRef
                                                        .collection("vouchers").document(promotion.getId()).set(convertObjectToMapIsUsed(promotion), SetOptions.merge())
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
                                        }

                                        isNew = false;
                                        userRef.update(
                                                "new", false
                                        );

                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("VuError", "" + e.getMessage());
                                    })
                                    .addOnCompleteListener(tasks -> {
                                        // Notify observers that the data is ready
//                    _mldUser.postValue(_mldUser.getValue());
                                    });
                        } else {
                            return;
                        }
                    } else {
                        // Document does not exist
                        Log.e("VuError", "error");
                    }
                } else {
                    // Error occurred while retrieving document
                    Log.e("VuError", "error");
                }
            }
        });
    }

    private boolean isExpired(String strEndDate) {
        LocalDate currentDate = LocalDate.now();
        AdminStatisticsRepository repo = new AdminStatisticsRepository();
        LocalDate endDate = LocalDate.parse(strEndDate, dateFormatter);
        LocalDate endDateUpdate = endDate.plusDays(1);
        return !endDateUpdate.isAfter(currentDate);
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

    public String getDate(long timeStamp){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedTime = dateFormat.format(new Date(timeStamp));

        return formattedTime;
    }

}
