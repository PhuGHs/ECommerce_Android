package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.databinding.AdminCustomItemPromotionBinding;
import com.example.ecommerce_hvpp.model.User;

import java.util.List;

public class AdminCustomItemPromotionAdapter extends RecyclerView.Adapter<AdminCustomItemPromotionAdapter.AdminCustomItemPromotionViewHolder> {
    Context mContext;
    List<User> mListUsers;

    public AdminCustomItemPromotionAdapter(Context context, List<User> listUser) {
        this.mContext = context;
        this.mListUsers = listUser;
    }
    @NonNull
    @Override
    public AdminCustomItemPromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdminCustomItemPromotionBinding mAdminCustomItemPromotionBinding =
                AdminCustomItemPromotionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminCustomItemPromotionViewHolder(mAdminCustomItemPromotionBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCustomItemPromotionViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mListUsers.size();
    }

    public static class AdminCustomItemPromotionViewHolder extends RecyclerView.ViewHolder {
        AdminCustomItemPromotionBinding mAdminCustomItemPromotionBinding;

        public AdminCustomItemPromotionViewHolder(@NonNull AdminCustomItemPromotionBinding itemPromotionBinding) {
            super(itemPromotionBinding.getRoot());
            this.mAdminCustomItemPromotionBinding = itemPromotionBinding;
        }
    }
}
