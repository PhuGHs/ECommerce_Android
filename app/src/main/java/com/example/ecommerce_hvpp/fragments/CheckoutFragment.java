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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.CheckoutAdapter;
import com.example.ecommerce_hvpp.adapter.VoucherAdapter;
import com.google.android.recaptcha.Recaptcha;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckoutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckoutFragment newInstance(String param1, String param2) {
        CheckoutFragment fragment = new CheckoutFragment();
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
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }
    private ImageButton btnBackToCart;
    private ListView listVoucherAppliedLv;
    private ArrayList<Pair<String, String>> listVoucherApplied;
    private TextView addressApplied;
    Spinner spinnerTypeCheckout;
    ArrayList<String> listTypeCheckout;
    private NavController navController;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize
        navController = Navigation.findNavController(requireView());
        listVoucherAppliedLv = (ListView) view.findViewById(R.id.listVoucherApplied);
        listVoucherApplied = new ArrayList<>();
        btnBackToCart = (ImageButton) view.findViewById(R.id.btnBackToCart);
        addressApplied = (TextView) view.findViewById(R.id.addressApplied);
        spinnerTypeCheckout = (Spinner) view.findViewById(R.id.typeCheckout);
        listTypeCheckout = new ArrayList<>();

        getListVoucherApplied();
        getTypeCheckout();
        addressApplied.setText(getAddressApplied());

        CheckoutAdapter checkoutAdapter = new CheckoutAdapter(getContext(), R.layout.simple_spinner_string_item, listTypeCheckout);
        checkoutAdapter.setDropDownViewResource(R.layout.simple_spinner_string_item);
        spinnerTypeCheckout.setAdapter(checkoutAdapter);

        VoucherAdapter voucherAdapter = new VoucherAdapter(getContext(), R.layout.voucher_item, listVoucherApplied);
        listVoucherAppliedLv.setAdapter(voucherAdapter);

        //navigate
        btnBackToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.cartFragment);
            }
        });
    }
    private void getListVoucherApplied(){
        listVoucherApplied.add(new Pair<>("RF001", "$3.00"));
        listVoucherApplied.add(new Pair<>("RF002", "$4.00"));
        listVoucherApplied.add(new Pair<>("RF003", "$5.00"));
    }
    private void getTypeCheckout(){
        listTypeCheckout.add("Visa");
        listTypeCheckout.add("Card Debit");
        listTypeCheckout.add("Momo");
    }
    private String getAddressApplied(){
        return "Santigo Bernabeu Stadium, Madrid, Spain - Hala Madrid, Fede Valverde";
    }
}