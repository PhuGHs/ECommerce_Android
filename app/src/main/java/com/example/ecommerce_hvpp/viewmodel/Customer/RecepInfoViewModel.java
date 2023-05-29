package com.example.ecommerce_hvpp.viewmodel.Customer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.RecepInfo;
import com.example.ecommerce_hvpp.repositories.customerRepositories.RecepInfoRepository;

import java.util.List;

public class RecepInfoViewModel extends ViewModel {
    RecepInfoRepository repo = new RecepInfoRepository();
    public LiveData<List<RecepInfo>> showRecepInfoList(){
        return repo.getAllRecepInfos();
    }


}
