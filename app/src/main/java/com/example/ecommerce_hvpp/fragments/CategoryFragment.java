package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.ExpandableListCategoryAdapter;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    ExpandableListView listViewCategory;
    ExpandableListCategoryAdapter adapter;
    List<String> listTitle;
    ProductViewModel viewModel;
    HashMap<String, List<String>> listDetailCategory = new HashMap<>();
    private NavController navController;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        listViewCategory = (ExpandableListView) view.findViewById(R.id.listCategory);
        navController = Navigation.findNavController(requireView());

        viewModel.getCategories().observe(getViewLifecycleOwner(), listDetailCategory -> {
            listTitle = new ArrayList<>(listDetailCategory.keySet());
            adapter = new ExpandableListCategoryAdapter(getContext(), listTitle, listDetailCategory);
            listViewCategory.setAdapter(adapter);
            listViewCategory.setOnChildClickListener((expandableListView, view1, groupPosition, childPosition, l) -> {
                Bundle bundle = new Bundle();
                bundle.putString("Type", listTitle.get(groupPosition).toLowerCase(Locale.ROOT));
                bundle.putString("Category", listDetailCategory.get(listTitle.get(groupPosition)).get(childPosition));

                navController.navigate(R.id.detailCategoryFragment, bundle);
                return false;
            });
        });
    }
}