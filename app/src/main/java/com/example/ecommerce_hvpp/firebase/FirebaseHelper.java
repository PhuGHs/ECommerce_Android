package com.example.ecommerce_hvpp.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    private static FirebaseHelper INSTANCE = null;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public FirebaseHelper() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized FirebaseHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseHelper();
        }
        return INSTANCE;
    }

    public FirebaseFirestore getDb() {
        return db;
    }
    public FirebaseAuth getAuth() {
        return auth;
    }


    public DatabaseReference getDatabaseReference() {
        return firebaseDatabase.getReference();
    }
    public FirebaseDatabase getRealtimeDatabase() {
        return FirebaseDatabase.getInstance();
    }
}
