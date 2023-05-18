package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminProductImageSlider;
import com.example.ecommerce_hvpp.util.CurrencyFormat;
import com.example.ecommerce_hvpp.viewmodel.admin_product_management.AdminProductDetailsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

public class AdminProductDetailsFragment extends Fragment {
    private NavController navController;
    private AdminProductDetailsViewModel viewModel;
    private TextView tvHeader;
    private ImageView btnBack;
    private SliderView sliderView;
    private List<String> imageUrls;
    private AdminProductImageSlider SlideAdapter;
    private Spinner spType, spSeason;
    private final String TAG = "AdminProductDetailsFragment";
    private String Id;
    private EditText etName, etPrice, etDescription;
    private Button btnSave, btnCancel;
    private ArrayAdapter<CharSequence> TypeAdapter, SeasonAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_product_detail, container, false);

        //Initialize viewModel
        viewModel = new ViewModelProvider(this).get(AdminProductDetailsViewModel.class);

        //get productId
        if(getArguments() == null) {
            Id = "Add Product";
            viewModel.setIsEditMode(false);
        } else {
            Id = getArguments().getString("productId");
            viewModel.setIsEditMode(true);
        }

        //Initialize view
        tvHeader = view.findViewById(R.id.header_title);
        btnBack = view.findViewById(R.id.btnBackProductDetail);
        etName = view.findViewById(R.id.etName);
        etPrice = view.findViewById(R.id.etPrice);
        etDescription = view.findViewById(R.id.etDescriptionName);
        spType = view.findViewById(R.id.type_Spinner);
        spSeason = view.findViewById(R.id.season_spinner);

        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);

        //initialize spinner adapter
        TypeAdapter = ArrayAdapter.createFromResource(getContext() , R.array.type_array, R.layout.simple_spinner_string_item);
        SeasonAdapter = ArrayAdapter.createFromResource(getContext() , R.array.season_array, R.layout.simple_spinner_string_item);
        TypeAdapter.setDropDownViewResource(com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item);
        SeasonAdapter.setDropDownViewResource(com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item);
        spType.setAdapter(TypeAdapter);
        spSeason.setAdapter(SeasonAdapter);

        //Slider view
        sliderView = view.findViewById(R.id.imageSlider);
        SlideAdapter = new AdminProductImageSlider(getContext());
        sliderView.setSliderAdapter(SlideAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setScrollTimeInSec(3);
        sliderView.stopAutoCycle();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Initialize navController
        navController = Navigation.findNavController(view);

        tvHeader.setText(Id);
        viewModel.getIsEditMode().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isEditMode) {
                if(isEditMode) {
                    implementEditFunctionality();
                } else {
                    implementAddFunctionality();
                }
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // pop back stack using nav controller
                navController.popBackStack();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void implementEditFunctionality() {
        // set value
        viewModel.getProduct(Id).observe(getViewLifecycleOwner(), resource -> {
            switch(resource.status) {
                case LOADING:
                    break;
                case ERROR:
                    Log.e(TAG, resource.message);
                    break;
                case SUCCESS:
                    etName.setText(resource.data.getName());
                    etPrice.setText(CurrencyFormat.getVNDCurrency(resource.data.getPrice()));
                    if(resource.data.getClub() == "") {
                        spType.setSelection(TypeAdapter.getPosition("Nation"));
                    }
                    else {
                        spType.setSelection(TypeAdapter.getPosition("Club"));
                    }
                    spSeason.setSelection(SeasonAdapter.getPosition(resource.data.getSeason()));
                    SlideAdapter.addItem(resource.data.getURLmain());
                    SlideAdapter.addItem(resource.data.getURLsub1());
                    SlideAdapter.addItem(resource.data.getURLsub2());
                    break;
            }
        });
    }
    public void implementAddFunctionality() {

    }
}
