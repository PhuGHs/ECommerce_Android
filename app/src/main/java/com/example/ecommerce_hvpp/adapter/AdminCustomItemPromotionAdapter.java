package com.example.ecommerce_hvpp.adapter;

import static com.example.ecommerce_hvpp.util.CustomFormat.dateFormatter;
import static com.example.ecommerce_hvpp.util.constant.KEY_INTENT_PROMOTION;
import static com.example.ecommerce_hvpp.util.CustomFormat.templateDate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminCustomItemPromotionBinding;
import com.example.ecommerce_hvpp.fragments.admin_fragments.AdminPromotionFragment;
import com.example.ecommerce_hvpp.model.Promotion;
import com.example.ecommerce_hvpp.viewmodel.admin.admin_promotion.AdminCustomItemPromotionViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminCustomItemPromotionAdapter extends RecyclerView.Adapter<AdminCustomItemPromotionAdapter.AdminCustomItemPromotionViewHolder> {
    Context mContext;
    List<Promotion> mListPromotion;
    List<Promotion> mListPromotionOriginal;
    AdminPromotionFragment parent;
    LocalDate currentDate = LocalDate.now();

    public AdminCustomItemPromotionAdapter(Context context, List<Promotion> listPromotion, AdminPromotionFragment parent) {
        this.mContext = context;
        this.mListPromotion = listPromotion;
        this.mListPromotionOriginal = listPromotion;
        this.parent = parent;
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
        boolean isExpired = isExpired(templateDate.format(promotion.getDate_end()));
        holder.mAdminCustomItemPromotionBinding.setVMItemPromotion(new AdminCustomItemPromotionViewModel(isExpired));

        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentName.setText(promotion.getName());
        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentCode.setText(promotion.getId());
        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentDiscount.setText(String.valueOf(promotion.getValue()));
        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentCondition.setText(String.valueOf(promotion.getCondition()));

        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentStartDate.setText(templateDate.format(promotion.getDate_begin()));
        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentEndDate.setText(templateDate.format(promotion.getDate_end()));

        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentApply.setText(promotion.getApply_for());

        holder.mAdminCustomItemPromotionBinding.adminPromotionComponentIcEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_INTENT_PROMOTION, promotion);
                NavHostFragment.findNavController(parent).navigate(R.id.adminEditPromotionFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListPromotion.size();
    }

    private boolean isExpired(String strEndDate) {
        LocalDate endDate = LocalDate.parse(strEndDate, dateFormatter);
        LocalDate endDateUpdate = endDate.plusDays(1);
        return !endDateUpdate.isAfter(currentDate);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterPromotion(String strSearch) {
        if (strSearch.isEmpty()) {
            mListPromotion = mListPromotionOriginal;
            notifyDataSetChanged();
        } else {
            List<Promotion> listOrderHistory = new ArrayList<>();
            for (Promotion promotion : mListPromotionOriginal) {
                if (String.valueOf(promotion.getId()).toLowerCase().contains(strSearch.toLowerCase()) ||
                        promotion.getName().toLowerCase().contains(strSearch.toLowerCase())) {
                    listOrderHistory.add(promotion);
                }
            }
            mListPromotion = listOrderHistory;
            notifyDataSetChanged();
        }
    }

    public static class AdminCustomItemPromotionViewHolder extends RecyclerView.ViewHolder {
        AdminCustomItemPromotionBinding mAdminCustomItemPromotionBinding;

        public AdminCustomItemPromotionViewHolder(@NonNull AdminCustomItemPromotionBinding itemPromotionBinding) {
            super(itemPromotionBinding.getRoot());
            this.mAdminCustomItemPromotionBinding = itemPromotionBinding;
        }
    }
}
