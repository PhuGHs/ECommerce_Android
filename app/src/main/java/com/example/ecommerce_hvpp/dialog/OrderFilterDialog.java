package com.example.ecommerce_hvpp.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommerce_hvpp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class OrderFilterDialog extends BottomSheetDialogFragment {
    private List<String> filterOptions;
    public interface OnFilterSelectedListener {
        void onFilterSelected(List<String> filterOptions);
    }
    private OnFilterSelectedListener listener;
    public void setOnFilterSelectedListener(OnFilterSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filters_orders_bottom_sheet_dialog, container, false);
        return view;
    }

    public OrderFilterDialog() {

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (listener != null) {
            listener.onFilterSelected(null); // Pass null to indicate no filter options selected
        }
    }
}
