package com.example.ecommerce_hvpp.fragments.admin_fragments;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayOrdersDataStatistics;
import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayProductSoldDataStatistics;
import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayRevenueDataStatistics;
import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayVisitorsDataStatistics;
import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.dataStatisticOrders;
import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.dataStatisticProductSold;
import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.dataStatisticRevenue;
import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.dataStatisticVisitors;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticsBinding;
import com.example.ecommerce_hvpp.fragments.statistics_detail.AdminStatisticOrdersFragment;
import com.example.ecommerce_hvpp.model.DataStatisticDouble;
import com.example.ecommerce_hvpp.model.DataStatisticInt;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminStatisticsFragment extends Fragment {
    AdminFragmentStatisticsBinding mAdminFragmentStatisticsBinding;
    AdminStatisticsViewModel vmAdminStatistics;
    AdminStatisticsRepository repo;
    Observer<Resource<Map<String, Integer>>> observer;
    Observable<Resource<Map<String, Integer>>> observable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticsBinding = AdminFragmentStatisticsBinding.inflate(inflater, container, false);

        // init view model
        vmAdminStatistics = new ViewModelProvider(requireActivity()).get(AdminStatisticsViewModel.class);
        mAdminFragmentStatisticsBinding.setAdminStatisticsViewModel(vmAdminStatistics);

        // init repo
        repo = new AdminStatisticsRepository();

        showDataStatistics();

        // init observable
//        observable = repo.getObservableStatisticsOrders();
//        observer = getObserverData();
//
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);

        // on click back page
        mAdminFragmentStatisticsBinding.adminStatisticsHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticsBinding.getRoot();
    }

    private void showDataStatistics() {
        handleVisitorsComponent();
        handleOrdersComponent();
        handleProductSoldComponent();
        handleRevenueComponent();
    }

    // Visitors - int
    @SuppressLint("SetTextI18n")
    private void handleVisitorsComponent() {
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> result = repo.handleComponentStatistics(dayVisitorsDataStatistics,
                mAdminFragmentStatisticsBinding.adminStatisticsComponentVisitorsQuantity);

        Pair<String, Integer> dayResultAndColor = repo.handleResult(requireContext(), result.first.first);
        Pair<String, Integer> monthResultAndColor = repo.handleResult(requireContext(), result.first.second);

        mAdminFragmentStatisticsBinding.adminStatisticsComponentVisitorsPercent.setText(dayResultAndColor.first);
        mAdminFragmentStatisticsBinding.adminStatisticsComponentVisitorsPercent.setTextColor(dayResultAndColor.second);

        dataStatisticVisitors = new DataStatisticInt(result.second.first, dayResultAndColor.first, dayResultAndColor.second,
                result.second.second, monthResultAndColor.first, monthResultAndColor.second);
    }

    // Orders - int
    @SuppressLint("SetTextI18n")
    private void handleOrdersComponent() {
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> result = repo.handleComponentStatistics(dayOrdersDataStatistics,
                mAdminFragmentStatisticsBinding.adminStatisticsComponentOrdersQuantity);

        Pair<String, Integer> dayResultAndColor = repo.handleResult(requireContext(), result.first.first);
        Pair<String, Integer> monthResultAndColor = repo.handleResult(requireContext(), result.first.second);

        mAdminFragmentStatisticsBinding.adminStatisticsComponentOrdersPercent.setText(dayResultAndColor.first);
        mAdminFragmentStatisticsBinding.adminStatisticsComponentOrdersPercent.setTextColor(dayResultAndColor.second);

        dataStatisticOrders = new DataStatisticInt(result.second.first, dayResultAndColor.first, dayResultAndColor.second,
                result.second.second, monthResultAndColor.first, monthResultAndColor.second);
    }

    // Product Sold - int
    @SuppressLint("SetTextI18n")
    private void handleProductSoldComponent() {
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> result = repo.handleComponentStatistics(dayProductSoldDataStatistics,
                mAdminFragmentStatisticsBinding.adminStatisticsComponentProductSoldQuantity);

        Pair<String, Integer> dayResultAndColor = repo.handleResult(requireContext(), result.first.first);
        Pair<String, Integer> monthResultAndColor = repo.handleResult(requireContext(), result.first.second);

        mAdminFragmentStatisticsBinding.adminStatisticsComponentProductSoldPercent.setText(dayResultAndColor.first);
        mAdminFragmentStatisticsBinding.adminStatisticsComponentProductSoldPercent.setTextColor(dayResultAndColor.second);

        dataStatisticProductSold = new DataStatisticInt(result.second.first, dayResultAndColor.first, dayResultAndColor.second,
                result.second.second, monthResultAndColor.first, monthResultAndColor.second);
    }

    // Revenue - float
    @SuppressLint("SetTextI18n")
    private void handleRevenueComponent() {
        Pair<Pair<Integer, Integer>, Pair<Double, Double>> result = repo.handleComponentStatisticsDouble(dayRevenueDataStatistics,
                mAdminFragmentStatisticsBinding.adminStatisticsComponentRevenueQuantity);

        Pair<String, Integer> dayResultAndColor = repo.handleResult(requireContext(), result.first.first);
        Pair<String, Integer> monthResultAndColor = repo.handleResult(requireContext(), result.first.second);

        mAdminFragmentStatisticsBinding.adminStatisticsComponentRevenuePercent.setText(dayResultAndColor.first);
        mAdminFragmentStatisticsBinding.adminStatisticsComponentRevenuePercent.setTextColor(dayResultAndColor.second);

        dataStatisticRevenue = new DataStatisticDouble(result.second.first, dayResultAndColor.first, dayResultAndColor.second,
                result.second.second, monthResultAndColor.first, monthResultAndColor.second);
    }


    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.test_bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
