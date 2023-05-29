package com.example.ecommerce_hvpp.fragments.admin_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminOrderItemAdapter;
import com.example.ecommerce_hvpp.adapter.adapterItemdecorations.DividerItemDecoration;
import com.example.ecommerce_hvpp.adapter.adapterItemdecorations.VerticalItemDecoration;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.util.CurrencyFormat;
import com.example.ecommerce_hvpp.viewmodel.admin.admin_order_management.AdminOrderManagementViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminOrderDetailsFragment extends Fragment {
    //region Variables
    private NavController navController;
    private ImageView btnBack;
    private TextView tvHeader, tvFoundText, tvNumberOfVouchers, tvDeliveryMethod, tvPaymentMethod, tvNote, tvRecipientName, tvPhoneNumber, tvAddress, tvSubtotal, tvDeliveryFee, tvTotal;
    private RecyclerView rclProductItem;
    private AdminOrderItemAdapter adapter;
    private AdminOrderManagementViewModel viewModel;
    private Order order;
    //endregion
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_order_detail, container, false);

        //region Initialization
        //Initialize view
        btnBack = view.findViewById(R.id.btnBackOrderDetail);
        tvHeader = view.findViewById(R.id.header_title);
        tvFoundText = view.findViewById(R.id.tvFoundtext);
        tvNumberOfVouchers = view.findViewById(R.id.tvNumberOfVouchers);
        tvDeliveryMethod = view.findViewById(R.id.tvDeliveryMethod);
        tvPaymentMethod = view.findViewById(R.id.tvPaymentMethod);
        tvNote = view.findViewById(R.id.tvNote);
        tvRecipientName = view.findViewById(R.id.tvRecipientName);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvSubtotal = view.findViewById(R.id.tvSubtotal);
        tvDeliveryFee = view.findViewById(R.id.tvDeliveryFee);
        tvTotal = view.findViewById(R.id.tvTotal);
        rclProductItem = view.findViewById(R.id.RclOrderItemList);

        //initialize viewModel
        viewModel = new ViewModelProvider(this).get(AdminOrderManagementViewModel.class);
        //endregion

        if(getArguments() != null) {
            order = getArguments().getParcelable("orderInfo");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());

        if(order == null) {
            Log.e("AdminOrderDetailsFragment", "Order null");
        } else {
            Log.i("list", String.valueOf(order.getItems().size()));
        }

        //region assign
        rclProductItem.setLayoutManager(new LinearLayoutManager(getContext()));
        rclProductItem.addItemDecoration(new VerticalItemDecoration(20));
        rclProductItem.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
        adapter = new AdminOrderItemAdapter(order.getItems());
        rclProductItem.setAdapter(adapter);

        tvHeader.setText(order.getId());
        tvFoundText.setText("Found " + String.valueOf(order.getItems().size()) + " types of clothes");
        tvNumberOfVouchers.setText(String.valueOf(order.getVoucherList().size()) + " applied");
        tvDeliveryMethod.setText(order.getDeliverMethod());
        tvPaymentMethod.setText(order.getPaymentMethod());
        tvNote.setText(order.getNote());
        tvRecipientName.setText(order.getRecipientName());
        tvPhoneNumber.setText(order.getPhone_number());
        tvAddress.setText(order.getAddress());
        tvSubtotal.setText(CurrencyFormat.getVNDCurrency(order.getSubtotal()));
        tvTotal.setText(CurrencyFormat.getVNDCurrency(order.getSubtotal() - order.getTotalDiscount()));
        //endregion

        //region btn click event handlers
        btnBack.setOnClickListener(v -> {
            navController.popBackStack();
        });
        //endregion


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // pop back stack using nav controller
                navController.popBackStack();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
