package com.example.ecommerce_hvpp.fragments.admin_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.SkeletonScreen;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminOrderItemAdapter;
import com.example.ecommerce_hvpp.adapter.adapterItemdecorations.DividerItemDecoration;
import com.example.ecommerce_hvpp.adapter.adapterItemdecorations.VerticalItemDecoration;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.util.CurrencyFormat;
import com.example.ecommerce_hvpp.viewmodel.admin.admin_order_management.AdminOrderManagementViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.Objects;

public class AdminOrderDetailsFragment extends Fragment {
    //region Variables
    private NavController navController;
    private ImageView btnBack;
    private TextView tvHeader, tvFoundText, tvNumberOfVouchers, tvDeliveryMethod, tvPaymentMethod, tvNote, tvRecipientName, tvPhoneNumber, tvAddress, tvSubtotal, tvDeliveryFee, tvTotal;
    private RecyclerView rclProductItem;
    private AdminOrderItemAdapter adapter;
    private AdminOrderManagementViewModel viewModel;
    private Order order;
    private StepView stepView;
    private Button btnMarkAs;
    private SkeletonScreen skeletonScreen;
    //endregion
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_order_detail, container, false);

        initView(view);

        viewModel = new ViewModelProvider(this).get(AdminOrderManagementViewModel.class);

        if(getArguments() != null) {
            order = getArguments().getParcelable("orderInfo");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());
        customStepView();
        assignData();
        handleEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void initView(View view) {
        btnBack = view.findViewById(R.id.btnBackOrderDetail);
        btnMarkAs = view.findViewById(R.id.btnMarkAs);
        tvHeader = view.findViewById(R.id.header_title);
        tvFoundText = view.findViewById(R.id.tvFoundtext);
        tvNumberOfVouchers = view.findViewById(R.id.tvNumberOfVouchers);
        tvDeliveryMethod = view.findViewById(R.id.tvDeliveryMethod);
        tvPaymentMethod = view.findViewById(R.id.tvPaymentMethod);
        tvNote = view.findViewById(R.id.tvNote);
        tvRecipientName = view.findViewById(R.id.tvRecipientName);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvSubtotal = view.findViewById(R.id.tvSubtotal);
        tvDeliveryFee = view.findViewById(R.id.tvDeliveryFee);
        tvTotal = view.findViewById(R.id.tvTotal);
        rclProductItem = view.findViewById(R.id.RclOrderItemList);
        stepView = view.findViewById(R.id.step_view);
    }

    private void customStepView() {
        stepView.getState()
                .selectedTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                .animationType(StepView.ANIMATION_CIRCLE)
                .selectedCircleColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                .selectedCircleRadius(getResources().getDimensionPixelSize(R.dimen.dp14))
                .selectedStepNumberColor(ContextCompat.getColor(getContext(), R.color.white))
                .doneStepMarkColor(ContextCompat.getColor(getContext(), R.color.white))
                .steps(new ArrayList<String>() {{
                    add("Pending");
                    add("Confirmed");
                    add("Packaged");
                    add("Delivered");
                }})
                .stepsNumber(4)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .stepLineWidth(getResources().getDimensionPixelSize(R.dimen.dp1))
                .textSize(getResources().getDimensionPixelSize(R.dimen.sp14))
                .stepNumberTextSize(getResources().getDimensionPixelSize(R.dimen.sp16))
                .typeface(ResourcesCompat.getFont(getContext(), R.font.crimson_pro))
                // other state methods are equal to the corresponding xml attributes
                .commit();

        if(Objects.equals(order.getStatus(), "Pending")) {
        } else if (Objects.equals(order.getStatus(), "Confirmed")) {
            stepView.go(1, true);
        } else if (Objects.equals(order.getStatus(), "Packaged")) {
            stepView.go(2, true);
        } else if (Objects.equals(order.getStatus(), "Delivered")) {
            stepView.go(3, true);
            btnMarkAs.setVisibility(View.GONE);
        }
    }
    private void assignData() {
        rclProductItem.setLayoutManager(new LinearLayoutManager(getContext()));
        rclProductItem.addItemDecoration(new VerticalItemDecoration(20));
        rclProductItem.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
        adapter = new AdminOrderItemAdapter(order.getItems());
        rclProductItem.setAdapter(adapter);

        tvHeader.setText(order.getId());
        tvFoundText.setText("Found " + String.valueOf(order.getItems().size()) + " types of clothes");
        tvNumberOfVouchers.setText(String.valueOf(order.getVoucherList().size()) + " applied");
        tvDeliveryMethod.setText(order.getDeliverMethod());
        tvPaymentMethod.setText(order.getPaymentMethod());
        tvNote.setText(order.getNote());
        tvRecipientName.setText(order.getRecipientName());
        tvPhoneNumber.setText(order.getPhone_number());
        tvAddress.setText(order.getAddress());
        tvSubtotal.setText(CurrencyFormat.getVNDCurrency(order.getSubtotal()));
        tvTotal.setText(CurrencyFormat.getVNDCurrency(order.getSubtotal() - order.getTotalDiscount()));
    }
    private void handleEvents() {
        btnBack.setOnClickListener(v -> {
            navController.popBackStack();
        });

        btnMarkAs.setOnClickListener(v -> {
            stepView.go(1, true);
            String status = "";
            switch(order.getStatus()) {
                case "Pending":
                    status = "Confirmed";
                    break;
                case "Confirmed":
                    status = "Packaged";
                    break;
                case "Packaged":
                    status = "Delivered";
                    break;
            }
            viewModel.updateOrder(order.getId(), status);
            Snackbar.make(requireView(), "Updated order status", Snackbar.LENGTH_SHORT).show();
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // pop back stack using nav controller
                navController.popBackStack();
            }
        });
    }
}
