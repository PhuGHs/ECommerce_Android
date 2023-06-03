package com.example.ecommerce_hvpp.dialog;

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

public class ProductFilterDialog extends BottomSheetDialogFragment {
    private Map<String, Boolean> filterOptions;
    private List<Chip> chips;
    private List<String> initialList;
    private List<String> list;

    public ProductFilterDialog(List<String> initialList) {
        this.initialList = initialList;
    }

    public interface OnFilterSelectedListener {
        void onFilterSelected(List<String> filterOptions);
    }
    private OrderFilterDialog.OnFilterSelectedListener listener;
    private ChipGroup sortByChipGroup, searchOptionsChipGroup, categoryChipGroup;
    private Chip PriceChip, CreatedDateChip, AllChip, NationChip, ClubChip, ProductNameChip, NationSearchChip, ClubSearchChip;
    private ImageView btnClose;
    private Button btnApply;
    public void setOnFilterSelectedListener(OrderFilterDialog.OnFilterSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filters_products_bottom_sheet_dialog, container, false);
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

        categoryChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
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

    private void initOtherElements(View view) {
        filterOptions = new LinkedHashMap<>();
        list = new ArrayList<>();


        btnApply = view.findViewById(R.id.btnApply);
        btnClose = view.findViewById(R.id.btnClose);

        filterOptions.put("Price", false);
        filterOptions.put("Created Date", true);
        filterOptions.put("All", true);
        filterOptions.put("Nation", false);
        filterOptions.put("Club", false);
        filterOptions.put("Product Name Option", true);
        filterOptions.put("Nation Option", false);
        filterOptions.put("Club Option", false);
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

    private void initChipGroup(View view) {
        sortByChipGroup = view.findViewById(R.id.chip_group_sortBy);
        searchOptionsChipGroup = view.findViewById(R.id.chip_group_search_options);
        categoryChipGroup = view.findViewById(R.id.chip_group_category);
    }

    private void initChip(View view) {
        PriceChip = view.findViewById(R.id.priceChip);
        CreatedDateChip = view.findViewById(R.id.CreatedDateChip);
        AllChip = view.findViewById(R.id.AllChip);
        NationChip = view.findViewById(R.id.NationChip);
        ClubChip = view.findViewById(R.id.ClubChip);
        ProductNameChip = view.findViewById(R.id.ProductNameSearchChip);
        NationSearchChip = view.findViewById(R.id.NationSearchChip);
        ClubSearchChip = view.findViewById(R.id.ClubSearchChip);
    }

    private void loadInitialChipState() {
        chips = new ArrayList<>();
        chips.add(PriceChip);
        chips.add(CreatedDateChip);
        chips.add(AllChip);
        chips.add(ClubSearchChip);
        chips.add(NationSearchChip);
        chips.add(ProductNameChip);
        chips.add(ClubChip);
        chips.add(NationChip);
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
