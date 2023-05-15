package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerce_hvpp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailProductCustomerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailProductCustomerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailProductCustomerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailProductCustomerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailProductCustomerFragment newInstance(String param1, String param2) {
        DetailProductCustomerFragment fragment = new DetailProductCustomerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_product_customer, container, false);
    }
    ImageButton btnBackToPrevious;
    RatingBar ratingBar;
    String productID;
    private NavController navController;
    ImageSlider detailImgSlider;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize
        navController = Navigation.findNavController(requireView());
        btnBackToPrevious = (ImageButton) view.findViewById(R.id.btnBackToPrevious);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBarDetailC);
        detailImgSlider = (ImageSlider) view.findViewById(R.id.detailImageSlider);

        //set data
        getDataFromPreviousFragment();
        loadDetailImage();

        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });

    }
    public void getDataFromPreviousFragment(){
        Bundle bundle = getArguments();
        if (bundle != null){
            productID = bundle.getString("productID");
            ratingBar.setRating((float)4.4);
        }
    }
    public void loadDetailImage(){
        ArrayList<SlideModel> listImage = new ArrayList<>();

        listImage.add(new SlideModel(R.drawable.product_pattern, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.product_pattern_with_bg, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.product_pattern_with_bg, ScaleTypes.FIT));

        detailImgSlider.setImageList(listImage, ScaleTypes.FIT);
    }
}