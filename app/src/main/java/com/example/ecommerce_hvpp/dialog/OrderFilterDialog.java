package com.example.ecommerce_hvpp.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommerce_hvpp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFilterDialog extends BottomSheetDialogFragment {
    private Map<String, Boolean> filterOptions;
    public interface OnFilterSelectedListener {
        void onFilterSelected(Map<String, Boolean> filterOptions);
    }
    private OnFilterSelectedListener listener;
    private ChipGroup sortByChipGroup, searchOptionsChipGroup, statusChipGroup;
    private ImageView btnClose;
    private Button btnApply;
    public void setOnFilterSelectedListener(OnFilterSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filters_orders_bottom_sheet_dialog, container, false);

        filterOptions = new HashMap<>();
        sortByChipGroup = view.findViewById(R.id.chip_group_sortBy);
        searchOptionsChipGroup = view.findViewById(R.id.chip_group_search_options);
        statusChipGroup = view.findViewById(R.id.chip_group_status);

        btnApply = view.findViewById(R.id.btnApply);
        btnClose = view.findViewById(R.id.btnClose);

        filterOptions.put("Price", false);
        filterOptions.put("Created Date", false);
        filterOptions.put("All", true);
        filterOptions.put("Pending", false);
        filterOptions.put("Confirmed", false);
        filterOptions.put("Packaged", false);
        filterOptions.put("Delivered", false);
        filterOptions.put("Order Code", false);
        filterOptions.put("Phone Number", false);
        filterOptions.put("Recipient Name", false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sortByChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                for(int i = 0; i < group.getChildCount(); i++) {
                    Chip chip = (Chip) group.getChildAt(i);
                    String key = chip.getText().toString();
                    if(chip.isChecked()) {
                        filterOptions.put(key, true);
                    } else {
                        filterOptions.put(key, false);
                    }
                    Log.i(key, String.valueOf(filterOptions.get(key)));
                }
            }
        });

        searchOptionsChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                for(int i = 0; i < group.getChildCount(); i++) {
                    Chip chip = (Chip) group.getChildAt(i);
                    String key = chip.getText().toString();
                    if(chip.isChecked()) {
                        filterOptions.put(key, true);
                    } else {
                        filterOptions.put(key, false);
                    }
                    Log.i(key, String.valueOf(filterOptions.get(key)));
                }
            }
        });

        statusChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                for(int i = 0; i < group.getChildCount(); i++) {
                    Chip chip = (Chip) group.getChildAt(i);
                    String key = chip.getText().toString();
                    filterOptions.put(key, chip.isChecked());
                    Log.i(key, String.valueOf(filterOptions.get(key)));
                }
            }
        });

        btnClose.setOnClickListener(v -> {
            dismiss();
        });

        btnApply.setOnClickListener(v -> {
            listener.onFilterSelected(filterOptions);
            Snackbar.make(getContext(), requireView(), "Filters are applied!", Snackbar.LENGTH_SHORT).show();
            dismiss();
        });

    }

    public OrderFilterDialog() {

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (listener != null) {
            listener.onFilterSelected(filterOptions); // Pass null to indicate no filter options selected
        }
    }
}
