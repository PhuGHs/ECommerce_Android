package com.example.ecommerce_hvpp.fragments;

import static com.example.ecommerce_hvpp.util.constant.templateDate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.databinding.AdminFragmentAddPromotionBinding;
import com.example.ecommerce_hvpp.model.Promotion;
import com.example.ecommerce_hvpp.repositories.AdminPromotionRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminAddPromotionFragment extends Fragment {
    AdminFragmentAddPromotionBinding mAdminFragmentAddPromotionBinding;
    AdminPromotionRepository repo;

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentAddPromotionBinding = AdminFragmentAddPromotionBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminPromotionRepository();

        // button add
        mAdminFragmentAddPromotionBinding.adminAddPromotionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    handleAddButton();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // on click back page
        mAdminFragmentAddPromotionBinding.adminAddPromotionButtonCancel.setOnClickListener(repo.onClickBackPage());
        mAdminFragmentAddPromotionBinding.adminAddPromotionHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentAddPromotionBinding.getRoot();
    }

    Promotion convertPromotionObject() throws ParseException {
        String name = String.valueOf(mAdminFragmentAddPromotionBinding.adminAddPromotionName.getText());
        String code = String.valueOf(mAdminFragmentAddPromotionBinding.adminAddPromotionCode.getText());
        int discount = Integer.parseInt(String.valueOf(mAdminFragmentAddPromotionBinding.adminAddPromotionDiscount.getText()));
        int minimum = Integer.parseInt(String.valueOf(mAdminFragmentAddPromotionBinding.adminAddPromotionMinimum.getText()));

        String strStartDate = String.valueOf(mAdminFragmentAddPromotionBinding.adminAddPromotionStartDate.getText());
        Date startDate = templateDate.parse(strStartDate);

        String strEndDate = String.valueOf(mAdminFragmentAddPromotionBinding.adminAddPromotionEndDate.getText());
        Date endDate = templateDate.parse(strEndDate);

        String apply = String.valueOf(mAdminFragmentAddPromotionBinding.adminAddPromotionApply.getText());

        return new Promotion(name, code, discount, minimum, startDate, endDate, apply);
    }

    private void handleAddButton() throws ParseException {
        repo.insertPromotionDatabase(convertPromotionObject());
    }
}
