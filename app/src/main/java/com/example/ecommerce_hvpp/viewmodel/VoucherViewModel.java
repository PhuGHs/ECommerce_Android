package com.example.ecommerce_hvpp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.RecepInfo;
import com.example.ecommerce_hvpp.model.Voucher;
import com.example.ecommerce_hvpp.repositories.VoucherRepository;

import java.util.List;

public class VoucherViewModel extends ViewModel {
    VoucherRepository repo = new VoucherRepository();
    public LiveData<List<Voucher>> showVoucherList(){
        return repo.getAllVouchers();
    }
}
