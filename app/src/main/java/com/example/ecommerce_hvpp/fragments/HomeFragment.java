package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.ProductAdapter;
import com.example.ecommerce_hvpp.model.Product;

import java.util.ArrayList;

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

        getListNewArrivals();
        getListBestSeller();

        linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        newArrivalAdapter = new ProductAdapter(getContext(), listNewArrivals);
        bestSellerAdapter = new ProductAdapter(getContext(), listBestSeller);

        listNewArrivalsRv.setLayoutManager(linearLayoutManager1);
        listNewArrivalsRv.setAdapter(newArrivalAdapter);

        listBestSellerRv.setLayoutManager(linearLayoutManager2);
        listBestSellerRv.setAdapter(bestSellerAdapter);

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
                navController.navigate(R.id.detailProductCustomerFragment);
            }
        });
    }
    public void getListNewArrivals(){
        listNewArrivals.add(new Product("P001", "Real Madrid Home", "white", "Real Madrid", "", "1999/2000", 17.99,9,5));
        listNewArrivals.add(new Product("P002", "Real Madrid Away", "white", "Real Madrid", "", "1999/2000", 17.99,9,5));
        listNewArrivals.add(new Product("P003", "AC Milan Home", "white", "AC Milan", "", "1999/2000", 17.99,9,5));
        listNewArrivals.add(new Product("P004", "Arsenal Home", "white", "Arsenal", "", "1999/2000", 17.99,9,5));
        listNewArrivals.add(new Product("P005", "MU Away", "white", "Manchester United", "", "1999/2000", 17.99,9,5));
        listNewArrivals.add(new Product("P005", "MU Away", "white", "Manchester United", "", "1999/2000", 17.99,9,5));
    }
    public void getListBestSeller(){
        listBestSeller.add(new Product("P001", "Real Madrid Home", "white", "Real Madrid", "", "1999/2000", 17.99,9,5));
        listBestSeller.add(new Product("P002", "Real Madrid Away", "white", "Real Madrid", "", "1999/2000", 17.99,9,5));
        listBestSeller.add(new Product("P003", "AC Milan Home", "white", "AC Milan", "", "1999/2000", 17.99,9,5));
        listBestSeller.add(new Product("P004", "Arsenal Home", "white", "Arsenal", "", "1999/2000", 17.99,9,5));
        listBestSeller.add(new Product("P005", "MU Away", "white", "Manchester United", "", "1999/2000", 17.99,9,5));
        listBestSeller.add(new Product("P005", "MU Away", "white", "Manchester United", "", "1999/2000", 17.99,9,5));
    }
}