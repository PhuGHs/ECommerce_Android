package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import android.widget.TextView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.adapter.CartAdapter;
import com.example.ecommerce_hvpp.model.Cart;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

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
    LinearLayoutManager linearLayoutManager;
    CartAdapter adapter;
    ImageButton btnBackToHome;
    Button btnNavToCheckout;
    TextView totalPrice;
    private NavController navController;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize
        navController = Navigation.findNavController(requireView());
        totalPrice = (TextView) view.findViewById(R.id.totalPrice);
        listCartRv = (RecyclerView) view.findViewById(R.id.listCart);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        getCartData();
        btnBackToHome = (ImageButton) view.findViewById(R.id.btnBackToHome);
        btnNavToCheckout = (Button) view.findViewById(R.id.btnNavToCheckout);

        //navigate
        btnBackToHome.setOnClickListener(view1 -> navController.navigate(R.id.homeFragment));
        btnNavToCheckout.setOnClickListener(view12 -> navController.navigate(R.id.checkoutFragment));
    }
    public void getCartData(){
        MainActivity.PDviewModel.getUserCart().observe(getViewLifecycleOwner(), carts -> {
            adapter = new CartAdapter(getContext(), carts);
            listCartRv.setLayoutManager(linearLayoutManager);
            listCartRv.setAdapter(adapter);

            CartAdapter.mldTotalPrice.observe(getViewLifecycleOwner(), total -> totalPrice.setText("$" + Math.round(total * 100.0) / 100.0));
        });
    }
    public double getTotalPrice(List<Cart> carts){
        double sum = 0;
        for (Cart cart : carts){
            sum += cart.getProduct().getPrice();
        }
        return sum;
    }
}