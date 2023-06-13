package com.example.ecommerce_hvpp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminCustomItemCustomerBinding;
import com.example.ecommerce_hvpp.model.Customer;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;

import java.util.ArrayList;
import java.util.List;

public class AdminCustomItemCustomerAdapter extends RecyclerView.Adapter<AdminCustomItemCustomerAdapter.AdminCustomItemCustomerViewHolder> {
    List<User> mListUsers;
    List<User> mListUsersOriginal;
    Context mContext;

    public AdminCustomItemCustomerAdapter(Context context, List<User> listUser) {
        this.mContext = context;
        this.mListUsers = listUser;
        this.mListUsersOriginal = listUser;
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
        User user = mListUsers.get(position);
        if (user == null) {
            return;
        }
        holder.mAdminCustomItemCustomerBinding.adminCustomerManagementComponentNameCustomer.setText(user.getUsername());
        holder.mAdminCustomItemCustomerBinding.adminCustomerManagementComponentEmailCustomer.setText(user.getEmail());

        if (user.getImagePath() == null || user.getImagePath().equals("")) {
            Glide
                    .with(mContext)
                    .load(R.drawable.baseline_no_avatar)
                    .into(holder.mAdminCustomItemCustomerBinding.adminCustomerManagementComponentAvatarCustomer);
        } else {
            // null
            Glide
                    .with(mContext)
                    .load(user.getImagePath())
                    .into(holder.mAdminCustomItemCustomerBinding.adminCustomerManagementComponentAvatarCustomer);
        }
    }

    @Override
    public int getItemCount() {
        return mListUsers.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterUser(String strSearch) {
        if (strSearch.isEmpty()) {
            mListUsers = mListUsersOriginal;
            notifyDataSetChanged();
        } else {
            List<User> listUsers = new ArrayList<>();
            for (User user : mListUsersOriginal) {
                if (user.getUsername().toLowerCase().contains(strSearch.toLowerCase())) {
                    listUsers.add(user);
                }
            }
            mListUsers = listUsers;
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
