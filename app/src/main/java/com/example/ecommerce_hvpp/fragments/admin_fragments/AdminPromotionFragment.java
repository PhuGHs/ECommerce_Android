package com.example.ecommerce_hvpp.fragments.admin_fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminCustomItemPromotionAdapter;
import com.example.ecommerce_hvpp.databinding.AdminFragmentPromotionBinding;
import com.example.ecommerce_hvpp.model.Promotion;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.example.ecommerce_hvpp.viewmodel.admin.admin_promotion.AdminPromotionViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private AdminPromotionFragment mFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentPromotionBinding = AdminFragmentPromotionBinding.inflate(inflater, container, false);

        // init view model
        vmAdminPromotion = new ViewModelProvider(requireActivity()).get(AdminPromotionViewModel.class);
        mAdminFragmentPromotionBinding.setAdminPromotionViewModel(vmAdminPromotion);

        // get and display data
        repo = new AdminProfileRepository();
        mFragment = this;

        observable = repo.getObservablePromotion();
        observer = getObserverPromotion();

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        mAdminFragmentPromotionBinding.adminPromotionSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String strSearch = charSequence.toString();
                observer = getObserverAfterSearch(strSearch);

                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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
                        adapterAdminCustomItemPromotion = new AdminCustomItemPromotionAdapter(getContext(), Objects.requireNonNull(resource.data), mFragment);

                        //set up recyclerview
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        mAdminFragmentPromotionBinding.adminPromotionRcvItemPromotion.setLayoutManager(layoutManager);
                        mAdminFragmentPromotionBinding.adminPromotionRcvItemPromotion.setAdapter(adapterAdminCustomItemPromotion);
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

    private Observer<Resource<List<Promotion>>> getObserverAfterSearch(String strSearch) {
        return new Observer<Resource<List<Promotion>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Resource<List<Promotion>> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        adapterAdminCustomItemPromotion.filterPromotion(strSearch);
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
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }
}
