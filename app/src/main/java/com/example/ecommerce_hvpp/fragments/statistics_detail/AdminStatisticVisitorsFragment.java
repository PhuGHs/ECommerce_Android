package com.example.ecommerce_hvpp.fragments.statistics_detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce_hvpp.adapter.AdminCustomItemCustomerAdapter;
import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticVisitorsBinding;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.repositories.AdminStatisticsRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class AdminStatisticVisitorsFragment extends Fragment {
    AdminFragmentStatisticVisitorsBinding mAdminFragmentStatisticVisitorsBinding;
    AdminStatisticsRepository repo;
    ArrayList<BarEntry> mBarEntries;
    AdminProfileRepository repoData;
    Observer<Resource<List<User>>> observer;
    Observable<Resource<List<User>>> observable;
    private Disposable disposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticVisitorsBinding = AdminFragmentStatisticVisitorsBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();
        repoData = new AdminProfileRepository();

        // init observable and observer
        observable = repoData.getObservableCustomers();

        // init bar chart
        initBarChart();

        // on click back page
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticVisitorsBinding.getRoot();
    }

    private void initBarChart() {
        mBarEntries = new ArrayList<>();


    }

    private Observer<Resource<List<User>>> getObserverUsers() {
        return new Observer<Resource<List<User>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Resource<List<User>> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        // handle data here from resource
                        List<User> listUsers = resource.data;
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
