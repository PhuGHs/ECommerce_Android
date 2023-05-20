package com.example.ecommerce_hvpp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.databinding.AdminCustomItemOrderHistoryBinding;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.model.User;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdminCustomItemOrderHistoryAdapter extends RecyclerView.Adapter<AdminCustomItemOrderHistoryAdapter.AdminCustomItemOrderHistoryViewHolder> {
    Context mContext;
    List<OrderHistory> mListOrderHistory;
    SimpleDateFormat templateDate;


    public AdminCustomItemOrderHistoryAdapter(Context context, List<OrderHistory> listOrderHistory) {
        this.mContext = context;
        this.mListOrderHistory = listOrderHistory;

    }
    @NonNull
    @Override
    public AdminCustomItemOrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdminCustomItemOrderHistoryBinding mAdminCustomItemOrderHistoryBinding =
                AdminCustomItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminCustomItemOrderHistoryViewHolder(mAdminCustomItemOrderHistoryBinding);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull AdminCustomItemOrderHistoryViewHolder holder, int position) {
        OrderHistory orderHistory = mListOrderHistory.get(position);
        if (orderHistory == null) {
            return;
        }

        templateDate = new SimpleDateFormat("dd MMM, yyyy");
        holder.mAdminCustomItemCustomerBinding.adminOrderHistoryComponentIdOrder.setText(String.valueOf(orderHistory.getID()));
        holder.mAdminCustomItemCustomerBinding.adminOrderHistoryComponentDate.setText(templateDate.format(orderHistory.getTimeCreate()));
    }

    @Override
    public int getItemCount() {
        return mListOrderHistory.size();
    }

    public static class AdminCustomItemOrderHistoryViewHolder extends RecyclerView.ViewHolder {
        AdminCustomItemOrderHistoryBinding mAdminCustomItemCustomerBinding;

        public AdminCustomItemOrderHistoryViewHolder(@NonNull AdminCustomItemOrderHistoryBinding itemOrderHistoryBinding) {
            super(itemOrderHistoryBinding.getRoot());
            this.mAdminCustomItemCustomerBinding = itemOrderHistoryBinding;
        }
    }
}
