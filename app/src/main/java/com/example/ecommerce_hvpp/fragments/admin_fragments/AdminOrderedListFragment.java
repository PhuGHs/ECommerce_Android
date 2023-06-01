package com.example.ecommerce_hvpp.fragments.admin_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminOrderedListFragment extends Fragment {
    private AdminOrderManagementAdapter adapter;
    private List<Order> orders;
    private NavController navController;
    private AdminOrderManagementViewModel viewModel;
    private RecyclerView rclOrders;
    private ImageView btnFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragmnet_orderlist, container, false);
        rclOrders = view.findViewById(R.id.rclOrders);

        btnFilter = view.findViewById(R.id.btnFilter);
        viewModel = new ViewModelProvider(this).get(AdminOrderManagementViewModel.class);
        rclOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        rclOrders.addItemDecoration(new VerticalItemDecoration(30));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireView());

        btnFilter.setOnClickListener(v -> {
            showBottomSheetDialog();
        });

        viewModel.getOrders().observe(getViewLifecycleOwner(), resource -> {
            switch(resource.status) {
                case LOADING:
                    break;
                case ERROR:
                    CustomToast toast = new CustomToast();
                    toast.ShowToastMessage(getContext(), 2, "error getting orders");
                    break;
                case SUCCESS:
                    orders = new ArrayList<>();
                    if(resource.data != null) {
                        orders.addAll(resource.data);
                        adapter = new AdminOrderManagementAdapter(orders, this);
                        rclOrders.setAdapter(adapter);
                    }
                    break;
            }
        });
    }

    private void showBottomSheetDialog() {
        // Create an instance of the BottomSheetDialogFragment
        OrderFilterDialog orderFilterDialog = new OrderFilterDialog();

        // Set a listener to receive the selected filter options from the dialog
        orderFilterDialog.setOnFilterSelectedListener(new OrderFilterDialog.OnFilterSelectedListener() {
            @Override
            public void onFilterSelected(Map<String, Boolean> options) {
                // Handle the selected filter options here
                // You can perform any necessary operations based on the selected options
            }
        });

        // Show the bottom sheet dialog
        orderFilterDialog.show(getChildFragmentManager(), orderFilterDialog.getTag());
    }

    public NavController getNavController() {
        return navController;
    }

//    vouchers.add(new Voucher("England, France, Germany, Spain", 6, "voucher1", "FES", 4.2, System.currentTimeMillis(), System.currentTimeMillis()));
//        vouchers.add(new Voucher("England, France, Germany, Spain", 6, "voucher1", "FES", 4.2, System.currentTimeMillis(), System.currentTimeMillis()));
//
//    List<OrderDetail> items = new ArrayList<>();
//    String image = "https://firebasestorage.googleapis.com/v0/b/ecommerce-hvpp.appspot.com/o/uploads%2F1684920025553.jpg?alt=media&token=4c680f30-2475-4322-87c2-4190cae39b58";
//        items.add(new OrderDetail("item1", image, "Bayern Munich Away", (float) 35.2, 2, "L"));
//        items.add(new OrderDetail("item2", image, "Bayern Munich Home", (float) 32.2, 2, "XL"));
//        orders.add(new Order("1", "43 Tan Lap, Di An, Binh Duong", "CUS001", "HVPPXpress", "Cash", "Lê Văn Phú", "please", "0814321006", "PENDING", System.currentTimeMillis(), System.currentTimeMillis(), (float) 485.2, vouchers, items));
//        orders.add(new Order("2", "47 Tan Lap, Dong Hoa, Di An, Binh Duong", "CUS002", "GHTK", "VISA DEBIT", "Lê Văn Phi", "please", "0814321006", "DELIVERING", System.currentTimeMillis(), System.currentTimeMillis(), (float) 325.2, vouchers, items));
//        orders.add(new Order("3", "47 Tan Lap, Dong Hoa, Di An, Binh Duong", "CUS002", "GHTK", "VISA DEBIT", "Lê Văn Phi", "please", "0814321006", "CANCELED", System.currentTimeMillis(), System.currentTimeMillis(), (float) 325.2, vouchers, items));
}
