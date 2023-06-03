package com.example.ecommerce_hvpp.fragments.admin_fragments;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.mListDataStatistics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticsBinding;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    Disposable disposable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticsBinding = AdminFragmentStatisticsBinding.inflate(inflater, container, false);

        // init view model
        vmAdminStatistics = new ViewModelProvider(requireActivity()).get(AdminStatisticsViewModel.class);
        mAdminFragmentStatisticsBinding.setAdminStatisticsViewModel(vmAdminStatistics);

        // init repo
        repo = new AdminStatisticsRepository();

        // init observable
        observable = repo.getObservableStatisticsOrders();
        observer = getObserverData();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        // on click back page
        mAdminFragmentStatisticsBinding.adminStatisticsHeaderBack.setOnClickListener(onClickBackPage());

        return mAdminFragmentStatisticsBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.test_bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }


    private View.OnClickListener onClickBackPage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        };
    }

    private Observer<Resource<Map<String, Integer>>> getObserverData() {
        return new Observer<Resource<Map<String, Integer>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Resource<Map<String, Integer>> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        // handle data here from resource
                        mListDataStatistics = resource.data;
                        assert mListDataStatistics != null;

                        break;
                    case ERROR:
                        Log.e("VuError", resource.message);
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
}
