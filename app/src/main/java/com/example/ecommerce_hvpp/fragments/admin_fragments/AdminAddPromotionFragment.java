package com.example.ecommerce_hvpp.fragments.admin_fragments;

import static com.example.ecommerce_hvpp.util.CustomFormat.templateDate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminFragmentAddPromotionBinding;
import com.example.ecommerce_hvpp.model.Promotion;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminPromotionRepository;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

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

        // test error

        // button add
        mAdminFragmentAddPromotionBinding.adminAddPromotionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isValidated()) {
                        setNotError();
                        if (repo.isValidatedDate(mAdminFragmentAddPromotionBinding.adminAddPromotionStartDate.getText().toString(),
                                mAdminFragmentAddPromotionBinding.adminAddPromotionEndDate.getText().toString())) {
                            CustomToast.ShowToastMessage(requireContext(), 1, "Successfully");
                            handleAddButton();
                            repo.returnBackPage(view);
                        } else {
                            CustomToast.ShowToastMessage(requireContext(), 2, "Start date must be less than the End date");
                            errorDateLogic();
                        }
                    } else {
                        CustomToast.ShowToastMessage(requireContext(), 2, "Some field is empty");
                        findTextInputEmpty();
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // set date
        mAdminFragmentAddPromotionBinding.adminAddPromotionStartDate.setOnClickListener(repo.createDatePickerDialog(getContext()));
        mAdminFragmentAddPromotionBinding.adminAddPromotionEndDate.setOnClickListener(repo.createDatePickerDialog(getContext()));

        // on click back page
        mAdminFragmentAddPromotionBinding.adminAddPromotionButtonCancel.setOnClickListener(repo.onClickBackPage());
        mAdminFragmentAddPromotionBinding.adminAddPromotionHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentAddPromotionBinding.getRoot();
    }

    private boolean isValidated() {
        return !repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionName)
                && !repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionCode)
                && !repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionDiscount)
                && !repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionMinimum)
                && !repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionStartDate)
                && !repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionEndDate)
                && !repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionApply);
    }
    private void setNotError() {
        String noError = "";
        mAdminFragmentAddPromotionBinding.adminAddPromotionDiscountLayout.setError(noError);
        mAdminFragmentAddPromotionBinding.adminAddPromotionMinimumLayout.setError(noError);
        mAdminFragmentAddPromotionBinding.adminAddPromotionStartDateLayout.setError(noError);
        mAdminFragmentAddPromotionBinding.adminAddPromotionEndDateLayout.setError(noError);
    }

    private void findTextInputEmpty() {
        if (repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionName)) {
            mAdminFragmentAddPromotionBinding.adminAddPromotionName.setError(getResources().getString(R.string.required_field));
        }

        if (repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionCode)) {
            mAdminFragmentAddPromotionBinding.adminAddPromotionCode.setError(getResources().getString(R.string.required_field));
        }

        if (repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionDiscount)) {
            mAdminFragmentAddPromotionBinding.adminAddPromotionDiscountLayout.setErrorIconDrawable(null);
            mAdminFragmentAddPromotionBinding.adminAddPromotionDiscountLayout.setError(getResources().getString(R.string.required_field));
        } else {
            mAdminFragmentAddPromotionBinding.adminAddPromotionDiscountLayout.setError(null);
        }

        if (repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionMinimum)) {
            mAdminFragmentAddPromotionBinding.adminAddPromotionMinimumLayout.setErrorIconDrawable(null);
            mAdminFragmentAddPromotionBinding.adminAddPromotionMinimumLayout.setError(getResources().getString(R.string.required_field));
        } else {
            mAdminFragmentAddPromotionBinding.adminAddPromotionMinimumLayout.setError(null);
        }

        if (repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionStartDate)) {
            mAdminFragmentAddPromotionBinding.adminAddPromotionStartDateLayout.setErrorIconDrawable(null);
            mAdminFragmentAddPromotionBinding.adminAddPromotionStartDateLayout.setError(getResources().getString(R.string.required_field));
        } else {
            mAdminFragmentAddPromotionBinding.adminAddPromotionStartDateLayout.setError(null);
        }

        if (repo.isEmpty(mAdminFragmentAddPromotionBinding.adminAddPromotionEndDate)) {
            mAdminFragmentAddPromotionBinding.adminAddPromotionEndDateLayout.setErrorIconDrawable(null);
            mAdminFragmentAddPromotionBinding.adminAddPromotionEndDateLayout.setError(getResources().getString(R.string.required_field));
        } else {
            mAdminFragmentAddPromotionBinding.adminAddPromotionEndDateLayout.setError(null);
        }
    }

    private void errorDateLogic() {
        mAdminFragmentAddPromotionBinding.adminAddPromotionStartDateLayout.setError("Start date must be less than the End date");
        mAdminFragmentAddPromotionBinding.adminAddPromotionEndDateLayout.setError("Start date must be later than the End date");
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

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.test_bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }
}
