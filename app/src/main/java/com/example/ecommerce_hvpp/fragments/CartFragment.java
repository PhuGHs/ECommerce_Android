package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.CartAdapter;
import com.example.ecommerce_hvpp.model.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
    RecyclerView listCartRv;
    ArrayList<Pair<Product, Integer>> listCart = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    CartAdapter adapter;
    ImageButton btnBackToHome;
    Button btnNavToCheckout;
    private NavController navController;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize
        navController = Navigation.findNavController(requireView());
        listCartRv = (RecyclerView) view.findViewById(R.id.listCart);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        getCartData();
        adapter = new CartAdapter(getContext(), listCart);
        btnBackToHome = (ImageButton) view.findViewById(R.id.btnBackToHome);
        btnNavToCheckout = (Button) view.findViewById(R.id.btnNavToCheckout);

        listCartRv.setLayoutManager(linearLayoutManager);
        listCartRv.setAdapter(adapter);

        //navigate
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.homeFragment);
            }
        });
        btnNavToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.checkoutFragment);
            }
        });
    }
    public void getCartData(){
        listCart.add(new Pair<>(new Product("P001", "Real Madrid Home", "Real Madrid", "", "1999/2000", 17.99,9,5,"","","",""), 2));
        listCart.add(new Pair<>(new Product("P002", "Real Madrid Away", "Real Madrid", "", "1999/2000", 17.99,9,5,"","","",""), 2));
        listCart.add(new Pair<>(new Product("P003", "AC Milan Home", "Real Madrid", "", "1999/2000", 17.99,9,5,"","","",""), 2));
        listCart.add(new Pair<>(new Product("P004", "Bayern Munich Home","Real Madrid", "", "1999/2000", 17.99,9,5,"","","",""), 2));
        listCart.add(new Pair<>(new Product("P005", "Liverpool Home","Real Madrid", "", "1999/2000", 17.99,9,5,"","","",""), 2));
        listCart.add(new Pair<>(new Product("P006", "Atletico Madrid Away","Real Madrid", "", "1999/2000", 17.99,9,5,"","","",""), 2));
    }
}