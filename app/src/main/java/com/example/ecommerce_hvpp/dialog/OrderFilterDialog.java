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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderFilterDialog extends BottomSheetDialogFragment {
    private Map<String, Boolean> filterOptions;
    private List<Chip> chips;
    private List<String> initialList;
    private List<String> list;
    public interface OnFilterSelectedListener {
        void onFilterSelected(List<String> filterOptions);
    }
    private OnFilterSelectedListener listener;
    private ChipGroup sortByChipGroup, searchOptionsChipGroup, statusChipGroup;
    private Chip PriceChip, CreatedDateChip, AllChip, PendingChip, ConfirmedChip, PackagedChip, DeliveredChip, OrderCodeChip, PhoneNumberChip, RecipientChip;
    private ImageView btnClose;
    private Button btnApply;
    public void setOnFilterSelectedListener(OnFilterSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filters_orders_bottom_sheet_dialog, container, false);
        initChipGroup(view);
        initChip(view);
        initOtherElements(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadInitialChipState();
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
                    if(chip.isChecked()) {
                        filterOptions.put(key, true);
                    } else {
                        filterOptions.put(key, false);
                    }
                    Log.i(key, String.valueOf(filterOptions.get(key)));
                }
            }
        });

        btnClose.setOnClickListener(v -> {
            dismiss();
        });

        btnApply.setOnClickListener(v -> {
            list = getKeysFromValue(filterOptions, true);
            listener.onFilterSelected(list);
            dismiss();
        });

    }

    public OrderFilterDialog(List<String> initialList) {
        this.initialList = initialList;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public static <K, V> List<K> getKeysFromValue(Map<K, V> map, V value) {
        List<K> keys = new ArrayList<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    private void initOtherElements(View view) {
        filterOptions = new LinkedHashMap<>();
        list = new ArrayList<>();


        btnApply = view.findViewById(R.id.btnApply);
        btnClose = view.findViewById(R.id.btnClose);

        filterOptions.put("Price", false);
        filterOptions.put("Created Date", true);
        filterOptions.put("All", true);
        filterOptions.put("Pending", false);
        filterOptions.put("Confirmed", false);
        filterOptions.put("Packaged", false);
        filterOptions.put("Delivered", false);
        filterOptions.put("Order Code", false);
        filterOptions.put("Phone Number", true);
        filterOptions.put("Recipient Name", false);
    }

    private void initChipGroup(View view) {
        sortByChipGroup = view.findViewById(R.id.chip_group_sortBy);
        searchOptionsChipGroup = view.findViewById(R.id.chip_group_search_options);
        statusChipGroup = view.findViewById(R.id.chip_group_status);
    }

    private void initChip(View view) {
        PriceChip = view.findViewById(R.id.priceChip);
        CreatedDateChip = view.findViewById(R.id.CreatedDateChip);
        AllChip = view.findViewById(R.id.AllChip);
        PendingChip = view.findViewById(R.id.pendingChip);
        ConfirmedChip = view.findViewById(R.id.confirmedChip);
        PackagedChip = view.findViewById(R.id.packagedChip);
        DeliveredChip = view.findViewById(R.id.deliveredChip);
        OrderCodeChip = view.findViewById(R.id.OrderCodeChip);
        PhoneNumberChip = view.findViewById(R.id.PhoneNumberChip);
        RecipientChip = view.findViewById(R.id.RecipientNameChip);
    }

    private void loadInitialChipState() {
        chips = new ArrayList<>();
        chips.add(PriceChip);
        chips.add(CreatedDateChip);
        chips.add(AllChip);
        chips.add(PendingChip);
        chips.add(ConfirmedChip);
        chips.add(PackagedChip);
        chips.add(DeliveredChip);
        chips.add(OrderCodeChip);
        chips.add(PhoneNumberChip);
        chips.add(RecipientChip);
        for(String string : initialList) {
            for (Chip chip : chips) {
                if (chip.getText().toString().equals(string)) {
                    if(!chip.isChecked()) {
                        chip.setChecked(true);
                    }
                }
            }
        }
    }

}
