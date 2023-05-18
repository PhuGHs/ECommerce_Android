package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.databinding.AdminCustomItemCustomerBinding;
import com.example.ecommerce_hvpp.model.Customer;
import com.example.ecommerce_hvpp.model.User;

import java.util.List;

public class AdminCustomItemCustomerAdapter extends RecyclerView.Adapter<AdminCustomItemCustomerAdapter.AdminCustomItemCustomerViewHolder> {
    List<Customer> mListUsers;
    Context mContext;

    public AdminCustomItemCustomerAdapter(Context context, List<Customer> listUser) {
        this.mContext = context;
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
        Customer customer = mListUsers.get(position);
        if (customer == null) {
            return;
        }
        holder.mAdminCustomItemCustomerBinding.adminCustomerManagementComponentNameCustomer.setText(customer.getName());
        holder.mAdminCustomItemCustomerBinding.adminCustomerManagementComponentEmailCustomer.setText(customer.getEmail());
        Glide.with(mContext).load(customer.getImagePath()).into(holder.mAdminCustomItemCustomerBinding.adminCustomerManagementComponentAvatarCustomer);
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
