package com.example.ecommerce_hvpp.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {
    private static FirebaseHelper INSTANCE = null;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    public FirebaseHelper() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
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

    public CollectionReference getCollection(String path) {
        return db.collection(path);
    }
    public FirebaseAuth getAuth() {
        return auth;
    }
    public DatabaseReference getDatabaseReference(String url) {
        return firebaseDatabase.getReference(url);
    }


    public DatabaseReference getDatabaseReference() {
        return firebaseDatabase.getReference();
    }
    public FirebaseDatabase getRealtimeDatabase() {
        return FirebaseDatabase.getInstance();
    }
    public StorageReference getImageStorage() {
        return storageReference;
    }

}
