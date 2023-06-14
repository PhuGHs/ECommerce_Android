package com.example.ecommerce_hvpp.fragments.customer_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminCustomItemPromotionAdapter;
import com.example.ecommerce_hvpp.adapter.ReviewFeedBackAdapter;
import com.example.ecommerce_hvpp.model.Feedback;
import com.example.ecommerce_hvpp.viewmodel.Customer.FeedBackViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReviewedFeedbackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReviewedFeedbackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewedFeedbackFragment newInstance(String param1, String param2) {
        ReviewedFeedbackFragment fragment = new ReviewedFeedbackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private NavController navController;
    private ImageButton back_Account_btn;
    private TextView unreviewed_navigate;
    private RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;
    private ReviewFeedBackAdapter adapter;
    private FeedBackViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_feedback_reviewed, container, false);

        viewModel = new ViewModelProvider(this).get(FeedBackViewModel.class);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview = v.findViewById(R.id.list_reviewed);

        viewModel.showReviewedFeedback().observe(getViewLifecycleOwner(), feedbacks -> getFeedbackAndSetFeedBackRecycleView(feedbacks));
        return v;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());
        back_Account_btn = (ImageButton) view.findViewById(R.id.back_info);
        unreviewed_navigate = (TextView) view.findViewById(R.id.unreviewed_navigate);

        back_Account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.accountFragment);
            }
        });
        unreviewed_navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.FeedbackFragment_Unreviewed);
            }
        });
    }
    public void getFeedbackAndSetFeedBackRecycleView(List<Feedback> listFeedback){
        adapter = new ReviewFeedBackAdapter(this, (ArrayList<Feedback>) listFeedback);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(linearLayoutManager);
    }
}
