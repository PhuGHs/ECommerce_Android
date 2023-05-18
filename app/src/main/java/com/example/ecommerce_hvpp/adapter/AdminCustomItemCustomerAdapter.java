package com.example.ecommerce_hvpp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.databinding.AdminCustomItemCustomerBinding;
import com.example.ecommerce_hvpp.model.Customer;
import com.example.ecommerce_hvpp.model.User;

import java.util.List;

public class AdminCustomItemCustomerAdapter extends RecyclerView.Adapter<AdminCustomItemCustomerAdapter.AdminCustomItemCustomerViewHolder> {
    List<Customer> mListUsers;

    public AdminCustomItemCustomerAdapter(List<Customer> listUser) {
        this.mListUsers = listUser;
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
    }

    @Override
    public int getItemCount() {
        return mListUsers.size();
    }

    public static class AdminCustomItemCustomerViewHolder extends RecyclerView.ViewHolder {
        AdminCustomItemCustomerBinding mAdminCustomItemCustomerBinding;

        public AdminCustomItemCustomerViewHolder(@NonNull AdminCustomItemCustomerBinding itemCustomerBinding) {
            super(itemCustomerBinding.getRoot());
            this.mAdminCustomItemCustomerBinding = itemCustomerBinding;
        }
    }
}
