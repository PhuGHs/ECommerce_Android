package com.example.ecommerce_hvpp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.RecepInfo;
import com.example.ecommerce_hvpp.repositories.RecepInfoRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecepInfoViewModel extends ViewModel {
    RecepInfoRepository repo = new RecepInfoRepository();
    public LiveData<List<RecepInfo>> showRecepInfoList(){
        return repo.getAllRecepInfos();
    }


}
