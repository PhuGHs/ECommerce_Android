package com.example.ecommerce_hvpp.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminCustomItemOrderHistoryAdapter;
import com.example.ecommerce_hvpp.adapter.AdminCustomItemPromotionAdapter;
import com.example.ecommerce_hvpp.databinding.AdminFragmentPromotionBinding;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.model.Promotion;
import com.example.ecommerce_hvpp.repositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.repositories.AdminPromotionRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.example.ecommerce_hvpp.viewmodel.admin_promotion.AdminPromotionViewModel;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminPromotionFragment extends Fragment {
    AdminFragmentPromotionBinding mAdminFragmentPromotionBinding;
    AdminPromotionViewModel vmAdminPromotion;
    AdminCustomItemPromotionAdapter adapterAdminCustomItemPromotion;
    AdminProfileRepository repo;
    Observable<Resource<List<Promotion>>> observable;
    Observer<Resource<List<Promotion>>> observer;
    private Disposable disposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentPromotionBinding = AdminFragmentPromotionBinding.inflate(inflater, container, false);

        // init view model
        vmAdminPromotion = new ViewModelProvider(requireActivity()).get(AdminPromotionViewModel.class);
        mAdminFragmentPromotionBinding.setAdminPromotionViewModel(vmAdminPromotion);

        // get and display data
        repo = new AdminProfileRepository();

        observable = repo.getObservablePromotion();
        observer = getObserverPromotion();
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        // on click btn add promotion


        // on click back page
        mAdminFragmentPromotionBinding.adminPromotionHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentPromotionBinding.getRoot();
    }

    private Observer<Resource<List<Promotion>>> getObserverPromotion() {
        return new Observer<Resource<List<Promotion>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                Log.e("Vucoder", "onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Resource<List<Promotion>> resource) {
                Log.e("Vucoder", "onNext");
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        adapterAdminCustomItemPromotion = new AdminCustomItemPromotionAdapter(getContext(), Objects.requireNonNull(resource.data));

                        //set up recyclerview
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        mAdminFragmentPromotionBinding.adminPromotionRcvItemPromotion.setLayoutManager(layoutManager);
                        mAdminFragmentPromotionBinding.adminPromotionRcvItemPromotion.setAdapter(adapterAdminCustomItemPromotion);

                        layoutManager.scrollToPositionWithOffset(adapterAdminCustomItemPromotion.getItemCount() - 1, 0);
                        break;
                    case ERROR:
                        Log.i("VuError", resource.message);
                        break;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                // Handle error state if needed
            }

            @Override
            public void onComplete() {
                // Handle completion if needed
                Log.e("Vucoder", "onComplete");
            }
        };
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

}
