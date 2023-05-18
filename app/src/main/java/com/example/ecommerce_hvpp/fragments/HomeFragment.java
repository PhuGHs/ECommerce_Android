package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.ProductAdapter;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    RecyclerView listNewArrivalsRv;
    RecyclerView listBestSellerRv;
    ArrayList<Product> listNewArrivals = new ArrayList<>();
    ArrayList<Product> listBestSeller = new ArrayList<>();
    LinearLayoutManager linearLayoutManager1, linearLayoutManager2;
    ProductAdapter newArrivalAdapter;
    ProductAdapter bestSellerAdapter;
    private NavController navController;
    ImageSlider imgSlider;
    ProductViewModel viewModel = new ProductViewModel();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());

        listNewArrivalsRv = (RecyclerView) view.findViewById(R.id.listNewArrivals);
        listBestSellerRv = (RecyclerView) view.findViewById(R.id.listBestSeller);
        imgSlider = (ImageSlider) view.findViewById(R.id.autoImageSlider);

        loadImageSlider();

        linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        getListNewArrivals();
        getListBestSeller();

        ImageButton btnNavToCart = (ImageButton) view.findViewById(R.id.btnNavToCart);
        ImageButton btnNavToMessage = (ImageButton) view.findViewById(R.id.btnNavToMessage);
        btnNavToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.cartFragment);
            }
        });
        btnNavToMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navController.navigate(R.id.detailProductCustomerFragment);
            }
        });
    }
    public void getListNewArrivals(){
        viewModel.getListNewArrivals().observe(requireActivity(), resource -> {
            switch (resource.status){
                case LOADING:
                    break;
                case SUCCESS:
                    listNewArrivals.addAll((ArrayList<Product>) resource.data);
                    newArrivalAdapter = new ProductAdapter(getContext(), listNewArrivals, requireView(), false);
                    listNewArrivalsRv.setLayoutManager(linearLayoutManager1);
                    listNewArrivalsRv.setAdapter(newArrivalAdapter);
                    break;
                case ERROR:
                    CustomToast loginErrorToast = new CustomToast();
                    loginErrorToast.ShowToastMessage(requireActivity(), 2, resource.message);
                    break;
            }
        });
    }
    public void getListBestSeller(){
        viewModel.getListBestSeller().observe(requireActivity(), resource -> {
            switch (resource.status){
                case LOADING:
                    break;
                case SUCCESS:
                    listBestSeller = (ArrayList<Product>) resource.data;
                    bestSellerAdapter = new ProductAdapter(getContext(), listBestSeller, requireView(), false);
                    listBestSellerRv.setLayoutManager(linearLayoutManager2);
                    listBestSellerRv.setAdapter(bestSellerAdapter);
                    break;
                case ERROR:
                    CustomToast loginErrorToast = new CustomToast();
                    loginErrorToast.ShowToastMessage(requireActivity(), 2, resource.message);
                    break;
            }
        });
    }
    public void loadImageSlider(){
        ArrayList<SlideModel> listImage = new ArrayList<>();

        listImage.add(new SlideModel(R.drawable.wall0_real, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.wall1_ucl, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.wall2_arg, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.wall3_dortmund, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.wall4_etihad, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.wall5_liverpool, ScaleTypes.FIT));

        imgSlider.setImageList(listImage, ScaleTypes.FIT);
    }
}