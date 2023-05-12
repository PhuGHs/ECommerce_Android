package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.ViewPagerDetailPC;

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
    ViewPager imageViewPager;
    int[] images = {R.drawable.product_pattern, R.drawable.product_pattern, R.drawable.product_pattern};
    ViewPagerDetailPC viewPagerAdapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageViewPager = (ViewPager) view.findViewById(R.id.viewPagerDetailC);
        viewPagerAdapter = new ViewPagerDetailPC(getContext(), images);

        imageViewPager.setAdapter(viewPagerAdapter);
    }
}