package com.example.ecommerce_hvpp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.databinding.AdminCustomItemCustomerBinding;
import com.example.ecommerce_hvpp.model.Customer;
import com.example.ecommerce_hvpp.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdminCustomItemCustomerAdapter extends RecyclerView.Adapter<AdminCustomItemCustomerAdapter.AdminCustomItemCustomerViewHolder> {
    List<Customer> mListCustomers;
    List<Customer> mListCustomersOriginal;
    Context mContext;

    public AdminCustomItemCustomerAdapter(Context context, List<Customer> listUser) {
        this.mContext = context;
        this.mListCustomers = listUser;
        this.mListCustomersOriginal = listUser;
    }

    @NonNull
    @Override
    public AdminCustomItemCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdminCustomItemCustomerBinding mAdminCustomItemCustomerBinding =
                AdminCustomItemCustomerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminCustomItemCustomerViewHolder(mAdminCustomItemCustomerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCustomItemCustomerViewHolder holder, int position) {
        Customer customer = mListCustomers.get(position);
        if (customer == null) {
            return;
        }
        holder.mAdminCustomItemCustomerBinding.adminCustomerManagementComponentNameCustomer.setText(customer.getName());
        holder.mAdminCustomItemCustomerBinding.adminCustomerManagementComponentEmailCustomer.setText(customer.getEmail());
        Glide.with(mContext).load(customer.getImagePath()).into(holder.mAdminCustomItemCustomerBinding.adminCustomerManagementComponentAvatarCustomer);
    }

    @Override
    public int getItemCount() {
        return mListCustomers.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterCustomer(String strSearch) {
        if (strSearch.isEmpty()) {
            mListCustomers = mListCustomersOriginal;
            notifyDataSetChanged();
        } else {
            List<Customer> listCustomers = new ArrayList<>();
            for (Customer customer : mListCustomersOriginal) {
                if (customer.getName().toLowerCase().contains(strSearch.toLowerCase()) ||
                    customer.getEmail().toLowerCase().contains(strSearch.toLowerCase())) {
                    listCustomers.add(customer);
                }
            }
            mListCustomers = listCustomers;
            notifyDataSetChanged();
        }
    }

    public static class AdminCustomItemCustomerViewHolder extends RecyclerView.ViewHolder {
        AdminCustomItemCustomerBinding mAdminCustomItemCustomerBinding;

        public AdminCustomItemCustomerViewHolder(@NonNull AdminCustomItemCustomerBinding itemCustomerBinding) {
            super(itemCustomerBinding.getRoot());
            this.mAdminCustomItemCustomerBinding = itemCustomerBinding;
        }
    }
}
