package com.example.ecommerce_hvpp.fragments.admin_fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminOrderManagementAdapter;
import com.example.ecommerce_hvpp.adapter.adapterItemdecorations.VerticalItemDecoration;
import com.example.ecommerce_hvpp.dialog.OrderFilterDialog;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.viewmodel.admin.admin_order_management.AdminOrderManagementViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderedListFragment extends Fragment {
    private AdminOrderManagementAdapter adapter;
    private List<Order> orders;
    private NavController navController;
    private AdminOrderManagementViewModel viewModel;
    private RecyclerView rclOrders;
    private ImageView btnFilter;
    private List<String> filterOptions;
    private EditText etSeacrchText;
    private TextView tvFoundText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragmnet_orderlist, container, false);
        initDefaultFilters();
        initView(view);
        initViewModelDataAndAdapter();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireView());
        getData();
        handleEvents();
    }

    private void showBottomSheetDialog() {
        OrderFilterDialog orderFilterDialog = new OrderFilterDialog(filterOptions);

        orderFilterDialog.setOnFilterSelectedListener(new OrderFilterDialog.OnFilterSelectedListener() {
            @Override
            public void onFilterSelected(List<String> options) {
                filterOptions.clear();
                filterOptions.addAll(options);
                Snackbar.make(requireView(), "Filters are applied!", Snackbar.LENGTH_SHORT).show();
                adapter.setTypeAdapter(filterOptions.get(1), filterOptions.get(0));
                if(adapter.getItemCount() > 1) {
                    tvFoundText.setText("Found " + String.valueOf(adapter.getItemCount()) + " results");
                } else if (adapter.getListSize() == 0) {
                    tvFoundText.setText("");
                } else {
                    tvFoundText.setText("Found " + String.valueOf(adapter.getItemCount()) + " result");
                }
            }
        });
        orderFilterDialog.show(getChildFragmentManager(), orderFilterDialog.getTag());
    }

    public void initDefaultFilters() {
        filterOptions = new ArrayList<>();
        filterOptions.add("Created Date");
        filterOptions.add("All");
        filterOptions.add("Phone Number");
    }

    public NavController getNavController() {
        return navController;
    }

    public List<String> getFilterOptions() {
        return filterOptions;
    }
    private void initViewModelDataAndAdapter() {
        viewModel = new ViewModelProvider(this).get(AdminOrderManagementViewModel.class);
        rclOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        rclOrders.addItemDecoration(new VerticalItemDecoration(30));
        orders = new ArrayList<>();
        adapter = new AdminOrderManagementAdapter(orders, this);
        rclOrders.setAdapter(adapter);
    }
    private void initView(View view) {
        rclOrders = view.findViewById(R.id.rclOrders);
        tvFoundText = view.findViewById(R.id.tvFoundText);
        etSeacrchText = view.findViewById(R.id.etSearchText);
        btnFilter = view.findViewById(R.id.btnFilter);

    }
    private void handleEvents() {
        btnFilter.setOnClickListener(v -> {
            showBottomSheetDialog();
        });

        etSeacrchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void getData() {
        viewModel.getOrders().observe(getViewLifecycleOwner(), resource -> {
            switch(resource.status) {
                case LOADING:
                    break;
                case ERROR:
                    CustomToast toast = new CustomToast();
                    toast.ShowToastMessage(getContext(), 2, "error getting orders");
                    break;
                case SUCCESS:
                    if(resource.data != null) {
                        orders.addAll(resource.data);
                        adapter.setBackUpList(resource.data);
                        adapter.notifyDataSetChanged();
                        if(adapter.getItemCount() > 1) {
                            tvFoundText.setText("Found " + adapter.getItemCount() + " results");
                        } else if (adapter.getListSize() == 0) {
                            tvFoundText.setText("");
                        } else {
                            tvFoundText.setText("Found " + adapter.getItemCount() + " result");
                        }
                    }
                    break;
            }
        });
    }

    public TextView getTvFoundText() {
        return tvFoundText;
    }
}
