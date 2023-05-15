package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.ExpandableListCategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    HashMap<String, List<String>> listDetailCategory = new HashMap<>();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewCategory = (ExpandableListView) view.findViewById(R.id.listCategory);
        getCategories();
        listTitle = new ArrayList<>(listDetailCategory.keySet());
        adapter = new ExpandableListCategoryAdapter(getContext(), listTitle, listDetailCategory);
        listViewCategory.setAdapter(adapter);
        listViewCategory.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(getContext(), listTitle.get(groupPosition)
                        + "->" + listDetailCategory.get(listTitle.get(groupPosition))
                        .get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    public void getCategories(){
        List<String> Club = new ArrayList<>();
        Club.add("Real Madrid");
        Club.add("AC Milan");
        Club.add("Man City");
        Club.add("Arsenal");

        List<String> Nation = new ArrayList<>();
        Nation.add("Croatia");
        Nation.add("Spain");
        Nation.add("Belgium");
        Nation.add("Brazil");
        Nation.add("Argentina");

        List<String> Season = new ArrayList<>();
        Season.add("1996/1997");
        Season.add("2000/2001");
        Season.add("2001/2002");

        listDetailCategory.put("Club", Club);
        listDetailCategory.put("Nation", Nation);
        listDetailCategory.put("Season", Season);
    }
}