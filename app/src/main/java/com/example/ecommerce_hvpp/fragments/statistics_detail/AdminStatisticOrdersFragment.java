package com.example.ecommerce_hvpp.fragments.statistics_detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticOrdersBinding;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class AdminStatisticOrdersFragment extends Fragment {
    AdminFragmentStatisticOrdersBinding mAdminFragmentStatisticOrdersBinding;
    AdminStatisticsRepository repo;
    ArrayList<BarEntry> mBarEntries;
    AdminProfileRepository repoData;
    Observer<Resource<List<OrderHistory>>> observer;
    Observable<Resource<List<OrderHistory>>> observable;
    private Disposable disposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticOrdersBinding = AdminFragmentStatisticOrdersBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();
        repoData = new AdminProfileRepository();

        // init observable
//        observable = repoData.getObservableOrderHistory();
        Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show();
        repo.getQuantityOrders();
        // init chart

        // on click back page
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticOrdersBinding.getRoot();
    }

    private void initBarChart() {
        mBarEntries = new ArrayList<>();
    }

    private Observer<Resource<List<OrderHistory>>> getObserverUsers() {
        return new Observer<Resource<List<OrderHistory>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Resource<List<OrderHistory>> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        // handle data here from resource
                        List<OrderHistory> listOrderHistory = resource.data;
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
