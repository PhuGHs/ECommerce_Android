package com.example.ecommerce_hvpp.fragments.admin_fragments;

import static com.example.ecommerce_hvpp.util.constant.KEY_INTENT_PROMOTION;
import static com.example.ecommerce_hvpp.util.CustomFormat.templateDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminFragmentEditPromotionBinding;
import com.example.ecommerce_hvpp.model.Promotion;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminPromotionRepository;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;

import java.text.ParseException;
import java.util.Date;

public class AdminEditPromotionFragment extends Fragment {
    AdminFragmentEditPromotionBinding mAdminFragmentEditPromotionBinding;
    AdminPromotionRepository repo;
    Promotion currPromotion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentEditPromotionBinding = AdminFragmentEditPromotionBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminPromotionRepository();

        // get data from click edit
        getDataBundle();

        // display data
        displayData();

        // on click save update
        mAdminFragmentEditPromotionBinding.adminEditPromotionButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isValidated()) {
                        CustomToast.ShowToastMessage(requireContext(), 1, "Updated Successfully");
                        handleSaveButton();
                    } else {
                        CustomToast.ShowToastMessage(requireContext(), 2, "Some field is empty");
                        findTextInputEmpty();
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // show date picker dialog
        mAdminFragmentEditPromotionBinding.adminEditPromotionStartDate.setOnClickListener(repo.createDatePickerDialog(getContext()));
        mAdminFragmentEditPromotionBinding.adminEditPromotionEndDate.setOnClickListener(repo.createDatePickerDialog(getContext()));

        // on click back page
        mAdminFragmentEditPromotionBinding.adminEditPromotionButtonCancel.setOnClickListener(repo.onClickBackPage());
        mAdminFragmentEditPromotionBinding.adminEditPromotionHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentEditPromotionBinding.getRoot();
    }

    void getDataBundle() {
        assert getArguments() != null;
        currPromotion = (Promotion) getArguments().getSerializable(KEY_INTENT_PROMOTION);
    }

    void displayData() {
        mAdminFragmentEditPromotionBinding.adminEditPromotionName.setText(currPromotion.getName());
        mAdminFragmentEditPromotionBinding.adminEditPromotionCode.setText(currPromotion.getId());
        mAdminFragmentEditPromotionBinding.adminEditPromotionDiscount.setText(String.valueOf(currPromotion.getValue()));
        mAdminFragmentEditPromotionBinding.adminEditPromotionMinimum.setText(String.valueOf(currPromotion.getCondition()));

        mAdminFragmentEditPromotionBinding.adminEditPromotionStartDate.setText(templateDate.format(currPromotion.getDate_begin()));
        mAdminFragmentEditPromotionBinding.adminEditPromotionEndDate.setText(templateDate.format(currPromotion.getDate_end()));

        mAdminFragmentEditPromotionBinding.adminEditPromotionApply.setText(currPromotion.getApply_for());
    }

    private boolean isValidated() {
        return !repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionName)
                && !repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionCode)
                && !repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionDiscount)
                && !repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionMinimum)
                && !repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionStartDate)
                && !repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionEndDate)
                && !repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionApply);
    }

    private void findTextInputEmpty() {
        if (repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionName)) {
            mAdminFragmentEditPromotionBinding.adminEditPromotionName.setError(String.valueOf(R.string.required_field));
        }
        if (repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionCode)) {
            mAdminFragmentEditPromotionBinding.adminEditPromotionCode.setError(String.valueOf(R.string.required_field));
        }
        if (repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionDiscount)) {
            mAdminFragmentEditPromotionBinding.adminEditPromotionDiscount.setError(String.valueOf(R.string.required_field));
        }
        if (repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionMinimum)) {
            mAdminFragmentEditPromotionBinding.adminEditPromotionMinimum.setError(String.valueOf(R.string.required_field));
        }
        if (repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionStartDate)) {
            mAdminFragmentEditPromotionBinding.adminEditPromotionStartDate.setError(String.valueOf(R.string.required_field));
        }
        if (repo.isEmpty(mAdminFragmentEditPromotionBinding.adminEditPromotionEndDate)) {
            mAdminFragmentEditPromotionBinding.adminEditPromotionEndDate.setError(String.valueOf(R.string.required_field));
        }
    }

    Promotion convertPromotionObject() throws ParseException {
        String name = String.valueOf(mAdminFragmentEditPromotionBinding.adminEditPromotionName.getText());
        String code = String.valueOf(mAdminFragmentEditPromotionBinding.adminEditPromotionCode.getText());
        int discount = Integer.parseInt(String.valueOf(mAdminFragmentEditPromotionBinding.adminEditPromotionDiscount.getText()));
        int minimum = Integer.parseInt(String.valueOf(mAdminFragmentEditPromotionBinding.adminEditPromotionMinimum.getText()));

        String strStartDate = String.valueOf(mAdminFragmentEditPromotionBinding.adminEditPromotionStartDate.getText());
        Date startDate = templateDate.parse(strStartDate);

        String strEndDate = String.valueOf(mAdminFragmentEditPromotionBinding.adminEditPromotionEndDate.getText());
        Date endDate = templateDate.parse(strEndDate);

        String apply = String.valueOf(mAdminFragmentEditPromotionBinding.adminEditPromotionApply.getText());

        return new Promotion(name, code, discount, minimum, startDate, endDate, apply);
    }

    private void handleSaveButton() throws ParseException {
        repo.updatePromotionDatabase(convertPromotionObject());
    }
}
