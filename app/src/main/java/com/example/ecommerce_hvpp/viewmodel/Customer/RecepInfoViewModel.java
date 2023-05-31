package com.example.ecommerce_hvpp.viewmodel.Customer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.RecepInfo;
import com.example.ecommerce_hvpp.repositories.customerRepositories.RecepInfoRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RecepInfoViewModel extends ViewModel {
    RecepInfoRepository repo = new RecepInfoRepository();
    private FirebaseAuth firebaseAuth;
    public LiveData<List<RecepInfo>> showRecepInfoList(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getAllRecepInfos(fbUser.getUid());
    }
    public LiveData<RecepInfo> showRecepDetail(String recep_id){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getRecepInfo(fbUser.getUid(), recep_id);
    }
    public void updateRecepDetail(RecepInfo info, String recep_id){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        repo.updateRecepInfo(fbUser.getUid(), recep_id, info.getName(), info.getPhonenumber(), info.getAddress());
    }
    public void updateStatusRecepDetail(String recep_id){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        repo.updateStatusOfRecepInfo(fbUser.getUid(), recep_id);
    }

}
