package com.example.ecommerce_hvpp.viewmodel.Customer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.Voucher;
import com.example.ecommerce_hvpp.repositories.customerRepositories.VoucherRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class VoucherViewModel extends ViewModel {
    VoucherRepository repo = new VoucherRepository();
    private FirebaseAuth firebaseAuth;
    public LiveData<List<Voucher>> showVoucherList(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getAllVouchers(fbUser.getUid());
    }
    public LiveData<Integer> showNumofVoucher(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getNumofVoucher(fbUser.getUid());
    }
    public void updateVoucher(String voucher_id){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        repo.updateVoucher(fbUser.getUid(), voucher_id);
    }
}
