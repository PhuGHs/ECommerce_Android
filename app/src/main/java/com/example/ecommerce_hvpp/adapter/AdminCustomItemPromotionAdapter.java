package com.example.ecommerce_hvpp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.databinding.AdminCustomItemPromotionBinding;
import com.example.ecommerce_hvpp.model.Promotion;
import com.example.ecommerce_hvpp.model.User;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdminCustomItemPromotionAdapter extends RecyclerView.Adapter<AdminCustomItemPromotionAdapter.AdminCustomItemPromotionViewHolder> {
    Context mContext;
    List<Promotion> mListPromotion;
    SimpleDateFormat templateDate;

    public AdminCustomItemPromotionAdapter(Context context, List<Promotion> listPromotion) {
        this.mContext = context;
        this.mListPromotion = listPromotion;
    }
    @NonNull
    @Override
    public AdminCustomItemPromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdminCustomItemPromotionBinding mAdminCustomItemPromotionBinding =
                AdminCustomItemPromotionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminCustomItemPromotionViewHolder(mAdminCustomItemPromotionBinding);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull AdminCustomItemPromotionViewHolder holder, int position) {
        Promotion promotion = mListPromotion.get(position);
        if (promotion == null) {
            return;
        }

        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentName.setText(promotion.getName());
        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentCode.setText(promotion.getId());
        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentDiscount.setText(String.valueOf(promotion.getValue()));
        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentCondition.setText(String.valueOf(promotion.getCondition()));

        templateDate = new SimpleDateFormat("dd MMM, yyyy");
        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentStartDate.setText(templateDate.format(promotion.getDate_begin()));
        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentEndDate.setText(templateDate.format(promotion.getDate_end()));

        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentApply.setText(promotion.getApply_for());

        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentIcEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Hello", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListPromotion.size();
    }

    public static class AdminCustomItemPromotionViewHolder extends RecyclerView.ViewHolder {
        AdminCustomItemPromotionBinding mAdminCustomItemPromotionBinding;

        public AdminCustomItemPromotionViewHolder(@NonNull AdminCustomItemPromotionBinding itemPromotionBinding) {
            super(itemPromotionBinding.getRoot());
            this.mAdminCustomItemPromotionBinding = itemPromotionBinding;
        }
    }
}
