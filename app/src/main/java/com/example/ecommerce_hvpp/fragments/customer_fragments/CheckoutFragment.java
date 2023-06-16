package com.example.ecommerce_hvpp.fragments.customer_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.adapter.CheckoutAdapter;
import com.example.ecommerce_hvpp.adapter.VoucherAdapter;
import com.example.ecommerce_hvpp.model.Voucher;
import com.example.ecommerce_hvpp.viewmodel.Customer.VoucherViewModel;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;

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
    private ImageButton btnBackToCart, navToAddress, navToVoucher;
    private ListView listVoucherAppliedLv;
    private ArrayList<Pair<String, Double>> listVoucherApplied;
    private TextView addressApplied, cartItems, cartPrice, totalOrder, shippingPrice;
    Double shipping = 1.99, total;
    Spinner spinnerTypeCheckout, spinnerShipping;
    ArrayList<String> listTypeCheckout, listTypeShipping;
    HashMap<String, Double> listShippingPrice;
    private NavController navController;
    private Button btnAccept;
    private EditText txtNote;
    Long nextDay = (long) 432000;
    String deliverMethod, paymentMethod;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize
        navController = Navigation.findNavController(requireView());
        listVoucherAppliedLv = (ListView) view.findViewById(R.id.listVoucherApplied);
        listVoucherApplied = new ArrayList<>();
        btnBackToCart = (ImageButton) view.findViewById(R.id.btnBackToCart);
        navToAddress = (ImageButton) view.findViewById(R.id.navToAddress);
        navToVoucher = (ImageButton) view.findViewById(R.id.navToVoucher);
        btnAccept = (Button) view.findViewById(R.id.btnCheckoutDone);
        addressApplied = (TextView) view.findViewById(R.id.addressApplied);
        cartItems = (TextView) view.findViewById(R.id.cartItems);
        cartPrice = (TextView) view.findViewById(R.id.cartPrice);
        totalOrder = (TextView) view.findViewById(R.id.totalOrder);
        shippingPrice = (TextView) view.findViewById(R.id.shippingPrice);
        spinnerTypeCheckout = (Spinner) view.findViewById(R.id.typeCheckout);
        spinnerShipping = (Spinner) view.findViewById(R.id.typeShipping);
        txtNote = (EditText) view.findViewById(R.id.txtNoteOrder);
        listTypeCheckout = new ArrayList<>();
        listTypeShipping = new ArrayList<>();
        listShippingPrice = new HashMap<>();
        getListVoucherApplied();
        getAddressApplied();
        getTypeCheckout();
        getTypeShipping();
        getCartItemsAndPrice();
        calcTotalOrder();

        CheckoutAdapter checkoutAdapter = new CheckoutAdapter(getContext(), R.layout.simple_spinner_string_item, listTypeCheckout);
        checkoutAdapter.setDropDownViewResource(R.layout.simple_spinner_string_item);
        spinnerTypeCheckout.setAdapter(checkoutAdapter);
        spinnerTypeCheckout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paymentMethod = listTypeCheckout.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        CheckoutAdapter shippingAdapter = new CheckoutAdapter(getContext(), R.layout.simple_spinner_string_item, listTypeShipping);
        shippingAdapter.setDropDownViewResource(R.layout.simple_spinner_string_item);
        spinnerShipping.setAdapter(shippingAdapter);
        spinnerShipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String key = listTypeShipping.get(i);
                deliverMethod = key;
                shipping = listShippingPrice.get(key);
                shippingPrice.setText("$" + shipping);
                totalOrder.setText("$" + Math.round((total + shipping) * 100.0) / 100.0);

                if (key.equals("Normal")) nextDay = (long) 432000;
                if (key.equals("Express")) nextDay = (long) 259200;
                if (key.equals("Same day")) nextDay = (long) 86400;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //navigate
        btnBackToCart.setOnClickListener(view1 -> navController.navigate(R.id.cartFragment));
        navToAddress.setOnClickListener(view12 -> {
            Bundle bundle = new Bundle();
            bundle.putString("Previous", "Checkout");
            navController.navigate(R.id.RecepientInfoFragment, bundle);
        });
        navToVoucher.setOnClickListener(view13 -> {
            Bundle bundle = new Bundle();
            bundle.putString("Previous", "Checkout");
            navController.navigate(R.id.VoucherFragment, bundle);
        });

        //create order
        btnAccept.setOnClickListener(view14 -> MainActivity.PDviewModel.createOrder(getContext(), deliverMethod, txtNote.getText().toString(), paymentMethod, (Timestamp.now().getSeconds() + nextDay) * 1000, Math.round((total + shipping) * 100.0) / 100.0));
    }
    private void getListVoucherApplied(){
        VoucherViewModel voucherViewModel = new ViewModelProvider(this).get(VoucherViewModel.class);
        voucherViewModel.showVoucherList().observe(getViewLifecycleOwner(), vouchers -> {
            if (listVoucherApplied.size() < 1){
                for (Voucher voucher : vouchers) {
                    if (MainActivity.PDviewModel.checkVoucherApply(voucher))
                        Log.d("Voucher", "add " + voucher.getId());
                    total -= voucher.getDiscountedValue();
                    listVoucherApplied.add(new Pair<>(voucher.getId(), voucher.getDiscountedValue()));
                }
                VoucherAdapter voucherAdapter = new VoucherAdapter(getContext(), R.layout.voucher_item, listVoucherApplied);
                listVoucherAppliedLv.setAdapter(voucherAdapter);
                totalOrder.setText("$" + Math.round((total + shipping) * 100.0) / 100.0);
            }
        });
    }
    public void getTypeShipping(){
        listTypeShipping.add("Normal");
        listTypeShipping.add("Express");
        listTypeShipping.add("Same day");
        listShippingPrice.put("Normal", 1.99);
        listShippingPrice.put("Express", 3.99);
        listShippingPrice.put("Same day", 6.99);
    }
    private void getTypeCheckout(){
        listTypeCheckout.add("Visa");
        listTypeCheckout.add("Card Debit");
        listTypeCheckout.add("Momo");
    }
    private void getAddressApplied(){
        MainActivity.PDviewModel.getAddressApplied().observe(getViewLifecycleOwner(), info -> addressApplied.setText(info));
    }
    private void getCartItemsAndPrice(){
        int items = MainActivity.PDviewModel.getTotalCartItems().getValue();
        String text = "";
        if (items > 1) text = " items)"; else text = " item)";
        cartItems.setText("(" + items + text);
        cartPrice.setText("$" + Math.round(MainActivity.PDviewModel.getTotalPriceCart().getValue() * 100.0) / 100.0);
    }
    public void calcTotalOrder(){
        total = shipping;
        total += Math.round(MainActivity.PDviewModel.getTotalPriceCart().getValue() * 100.0) / 100.0;
        totalOrder.setText("$" + Math.round(total * 100.0) / 100.0);
        total -= shipping;
    }
}