package com.example.ecommerce_hvpp.fragments.customer_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.adapter.OrderHistoryAdapter;
import com.example.ecommerce_hvpp.adapter.OrderHistorySubAdapter;
import com.example.ecommerce_hvpp.adapter.ProductAdapter;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderHistoryItem;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.viewmodel.Customer.OrderHistoryViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ORDER_ID = "order_id";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderHistoryDetailFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static OrderHistoryDetailFragment newInstance(String order_id) {
        OrderHistoryDetailFragment fragment = new OrderHistoryDetailFragment();
        Bundle args = new Bundle();
        args.putString(ORDER_ID, order_id);
        fragment.setArguments(args);
        return fragment;
    }
    private NavController navController;
    private ImageButton back_Account_btn;
    private OrderHistoryViewModel viewModel;
    private OrderHistorySubAdapter adapter;
    private RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;
    private TextView type_of_delivery;
    private TextView day_of_delivery;
    private TextView name_of_recep_delivery;
    private TextView phonenumber_of_recep_delivery;
    private TextView address_of_recep_delivery;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_orderhistory_detail, container, false);

        type_of_delivery = v.findViewById(R.id.type_of_delivery);
        day_of_delivery = v.findViewById(R.id.day_of_delivery);
        name_of_recep_delivery = v.findViewById(R.id.name_of_recep_delivery);
        phonenumber_of_recep_delivery = v.findViewById(R.id.numberphone_of_recep_delivery);
        address_of_recep_delivery = v.findViewById(R.id.address_of_recep_delivery);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview = v.findViewById(R.id.orderhistory_detail_list);
        viewModel = new ViewModelProvider(this).get(OrderHistoryViewModel.class);

        //Get data
        String id = getArguments().getString("order_id");

        viewModel.showOrderInfo(id).observe(requireActivity(), OrderInfo -> {
            type_of_delivery.setText(OrderInfo.getDeliveryMethod());
            day_of_delivery.setText("Received Day: " + getDate(OrderInfo.getReceiveDate()));
            name_of_recep_delivery.setText(OrderInfo.getRecipientName());
            phonenumber_of_recep_delivery.setText(OrderInfo.getPhone_number());
            address_of_recep_delivery.setText(OrderInfo.getAddress());
        });
        
        viewModel.showItemsofOrder(id).observe(getViewLifecycleOwner(), items -> getItemAndSetItemRecycleView(items));

        return v;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());
        back_Account_btn = (ImageButton) view.findViewById(R.id.back_info);

        back_Account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.OrderHistoryFragment);
            }
        });

    }
    public String getDate(long timeStamp){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedTime = dateFormat.format(new Date(timeStamp));

        return formattedTime;
    }
    public void getItemAndSetItemRecycleView(List<OrderHistorySubItem> listItems){
        adapter = new OrderHistorySubAdapter(this, (ArrayList<OrderHistorySubItem>) listItems);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(linearLayoutManager);
    }
    public NavController getNavController() {
        return navController;
    }

}
