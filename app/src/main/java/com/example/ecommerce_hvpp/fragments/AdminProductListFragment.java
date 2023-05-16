package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminProductAdapter;
import com.example.ecommerce_hvpp.adapter.adapterItemdecorations.GridItemDecoration;
import com.example.ecommerce_hvpp.viewmodel.admin_product_management.AdminProductManagementViewModel;

public class AdminProductListFragment extends Fragment {
    private SearchView svSearch;
    private RecyclerView rclProductList;
    private AdminProductAdapter adapter;
    private Button btnAdd;
    private AdminProductManagementViewModel viewModel;
    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_product_management, container, false);

        //Initialize view
        svSearch = view.findViewById(R.id.svSearch);
        rclProductList = view.findViewById(R.id.RclProductList);
        btnAdd = view.findViewById(R.id.btnAdd);

        //Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AdminProductManagementViewModel.class);

        //Initialize adapter
        adapter = new AdminProductAdapter(viewModel.getAllProductWithNoCriteria());
        rclProductList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rclProductList.setAdapter(adapter);

        rclProductList.addItemDecoration(new GridItemDecoration(2, 88, false));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //initialize navController
        navController = Navigation.findNavController(view);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.navigate_to_productDetails);
            }
        });
    }
}
