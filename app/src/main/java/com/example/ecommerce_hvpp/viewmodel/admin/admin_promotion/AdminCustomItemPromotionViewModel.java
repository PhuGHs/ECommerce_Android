package com.example.ecommerce_hvpp.viewmodel.admin.admin_promotion;

import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminCustomItemPromotionAdapter;

public class AdminCustomItemPromotionViewModel extends BaseObservable {
    public ObservableField<Boolean> isDisabled = new ObservableField<>();

    public AdminCustomItemPromotionViewModel(boolean status) {
        ObservableField<Boolean> currStatus = new ObservableField<>(status);
        setIsDisabled(currStatus);
    }

    public ObservableField<Boolean> getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(ObservableField<Boolean> isDisabled) {
        this.isDisabled = isDisabled;
    }

}
