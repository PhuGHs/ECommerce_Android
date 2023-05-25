package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminOrderManagementAdapter;
import com.example.ecommerce_hvpp.adapter.adapterItemdecorations.VerticalItemDecoration;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.util.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderedListFragment extends Fragment {
    private RecyclerView rclOrder;
    private AdminOrderManagementAdapter adapter;
    private List<Order> orders;
    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragmnet_orderlist, container, false);

        rclOrder = view.findViewById(R.id.RclOrders);
        rclOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        rclOrder.addItemDecoration(new VerticalItemDecoration(40));
        orders = new ArrayList<>();
        orders.add(new Order("1", "43 Tan Lap, Di An, Binh Duong", "CUS001", "HVPPXpress", "Cash", "Lê Văn Phú", "please", "0814321006", OrderStatus.PENDING, System.currentTimeMillis(), System.currentTimeMillis(), 485.2, null));
        orders.add(new Order("2", "47 Tan Lap, Dong Hoa, Di An, Binh Duong", "CUS002", "GHTK", "VISA DEBIT", "Lê Văn Phi", "please", "0814321006", OrderStatus.DELIVERING, System.currentTimeMillis(), System.currentTimeMillis(), 325.2, null));
        orders.add(new Order("3", "47 Tan Lap, Dong Hoa, Di An, Binh Duong", "CUS002", "GHTK", "VISA DEBIT", "Lê Văn Phi", "please", "0814321006", OrderStatus.CANCELED, System.currentTimeMillis(), System.currentTimeMillis(), 325.2, null));
        orders.add(new Order("3", "47 Tan Lap, Dong Hoa, Di An, Binh Duong", "CUS002", "GHTK", "VISA DEBIT", "Lê Văn Phi", "please", "0814321006", OrderStatus.DELIVERED, System.currentTimeMillis(), System.currentTimeMillis(), 325.2, null));
        adapter = new AdminOrderManagementAdapter(orders, this);
        rclOrder.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireView());
    }

    public NavController getNavController() {
        return navController;
    }

}
