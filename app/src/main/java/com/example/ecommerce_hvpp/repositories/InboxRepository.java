package com.example.ecommerce_hvpp.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Inbox;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InboxRepository {
    private FirebaseHelper fbHelper;
    private MutableLiveData<Resource<List<Inbox>>> _mldInboxList;
    public InboxRepository() {
        _mldInboxList = new MutableLiveData<>();
        fbHelper = FirebaseHelper.getInstance();
    }

    public LiveData<Resource<List<Inbox>>> getInboxList() {
        _mldInboxList.setValue(Resource.loading(null));
        fbHelper.getDatabaseReference().child("users").child(getCurrentUserUID()).child("inbox")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Inbox> list = new ArrayList<>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Inbox ib = dataSnapshot.getValue(Inbox.class);
                            list.add(ib);
                        }
                        _mldInboxList.setValue(Resource.success(list));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return _mldInboxList;
    }

    public String getCurrentUserUID() {
        return fbHelper.getAuth().getCurrentUser().getUid();
    }
}
