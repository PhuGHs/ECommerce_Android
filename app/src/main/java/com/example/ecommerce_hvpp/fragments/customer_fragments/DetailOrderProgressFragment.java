package com.example.ecommerce_hvpp.fragments.customer_fragments;

import android.os.Bundle;
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

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.DetailOrderProgressAdapter;
import com.example.ecommerce_hvpp.adapter.OrderHistorySubAdapter;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.example.ecommerce_hvpp.viewmodel.Customer.OrderHistoryViewModel;
import com.example.ecommerce_hvpp.viewmodel.Customer.OrderViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailOrderProgressFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ORDER_ID = "order_id";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailOrderProgressFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static DetailOrderProgressFragment newInstance(String order_id) {
        DetailOrderProgressFragment fragment = new DetailOrderProgressFragment();
        Bundle args = new Bundle();
        args.putString(ORDER_ID, order_id);
        fragment.setArguments(args);
        return fragment;
    }
    private NavController navController;
    private ImageButton back_Account_btn;
    private RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;
    private DetailOrderProgressAdapter adapter;
    private OrderViewModel viewModel;
    private TextView id_tv;
    private TextView address_tv;
    private TextView type_of_delivery_tv;
    private TextView start_date_tv;
    private TextView remaining_day_tv;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_details_orderprogress, container, false);

        id_tv = v.findViewById(R.id.id_order);
        address_tv = v.findViewById(R.id.address_of_order);
        type_of_delivery_tv = v.findViewById(R.id.type_of_delivery);
        start_date_tv = v.findViewById(R.id.start_date);
        remaining_day_tv = v.findViewById(R.id.remaining_day);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview = v.findViewById(R.id.list_order_progress);
        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        //Get data
        String id = getArguments().getString("orderprogress_id");

        id_tv.setText("#" + id);
        viewModel.showOrderProgressInfo(id).observe(requireActivity(), OrderInfo -> {
            address_tv.setText(OrderInfo.getAddress());
            type_of_delivery_tv.setText(OrderInfo.getDeliveryMethod());
            start_date_tv.setText(getDate(OrderInfo.getCreatedDate()));

            if (OrderInfo.getRemaining_day() < 2){
                remaining_day_tv.setText(Integer.toString(OrderInfo.getRemaining_day()) + " day");
            }
            else {
                remaining_day_tv.setText(Integer.toString(OrderInfo.getRemaining_day()) + " days");
            }
        });

        viewModel.showOrderDetail(id).observe(getViewLifecycleOwner(), items -> getItemProgressAndSetItemProgressRecycleView(items));

        return v;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());
        back_Account_btn = (ImageButton) view.findViewById(R.id.back_info);

        back_Account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.OrderProgressFragment);
            }
        });
    }
    public String getDate(long timeStamp){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedTime = dateFormat.format(new Date(timeStamp));

        return formattedTime;
    }
    public void getItemProgressAndSetItemProgressRecycleView(List<OrderHistorySubItem> listItems){
        adapter = new DetailOrderProgressAdapter(getContext(), (ArrayList<OrderHistorySubItem>) listItems);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(linearLayoutManager);
    }

}
