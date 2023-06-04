package com.example.ecommerce_hvpp.fragments.customer_fragments;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Voucher;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProfileViewModel;
import com.example.ecommerce_hvpp.viewmodel.Customer.VoucherViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
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
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private NavController navController;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseHelper firebaseHelper;
    private ProfileViewModel viewModel;
    private VoucherViewModel voucherViewModel;
    private String name;
    private String imagePath;
    private TextView name_tv;
    private TextView number_of_voucher_tv;
    private TextView number_of_orderprogress_tv;
    private TextView number_of_feedback_tv;
    private ImageView ava_image;
    private String size_text = "";
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
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        name_tv = v.findViewById(R.id.name_tv);
        number_of_voucher_tv = v.findViewById(R.id.number_voucher);
        number_of_orderprogress_tv = v.findViewById(R.id.number_order_progress);
        number_of_feedback_tv = v.findViewById(R.id.number_feedback);
        ava_image = v.findViewById(R.id.image_of_user);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        voucherViewModel = new ViewModelProvider(this).get(VoucherViewModel.class);

        if (viewModel.showUserName() != null){
           viewModel.showUserName().observe(requireActivity(), userInfoResource -> {
               switch (userInfoResource.status){
                   case LOADING:
                       break;
                   case SUCCESS:
                       name = userInfoResource.data.getUsername();
                       imagePath = userInfoResource.data.getImagePath();
                       Glide.with(this).load(imagePath).fitCenter().into(ava_image);
                       name_tv.setText(name);
                       setQuantity(number_of_orderprogress_tv, "Order");
                       setQuantityFeedback(number_of_feedback_tv, "Feedback");
                       break;
                   case ERROR:
                       CustomToast loginErrorToast = new CustomToast();
                       loginErrorToast.ShowToastMessage(requireActivity(), 2, userInfoResource.message);
                       break;
               }
           });
        }
        voucherViewModel.showNumofVoucher().observe(requireActivity(), NumofVoucher -> number_of_voucher_tv.setText(Integer.toString(NumofVoucher)));
        return v;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());


        LinearLayout voucher_btn = (LinearLayout) view.findViewById(R.id.btn_voucher);
        LinearLayout orderprogress_btn = (LinearLayout) view.findViewById(R.id.btn_orderprogress);
        LinearLayout feedback_btn = (LinearLayout) view.findViewById(R.id.btn_feedback);

        RelativeLayout profile_btn = (RelativeLayout) view.findViewById(R.id.btn_profile);
        RelativeLayout recep_info_btn = (RelativeLayout) view.findViewById(R.id.btn_recep_info);
        RelativeLayout order_history_btn = (RelativeLayout) view.findViewById(R.id.btn_orderhistory);
        //Button chat_with_admin_btn = (Button) view.findViewById(R.id.btn_chat_with_admin);
        RelativeLayout logout_btn = (RelativeLayout) view.findViewById(R.id.btn_logout);


        voucher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.VoucherFragment);
            }
        });
        orderprogress_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.OrderProgressFragment);
            }
        });
        feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.FeedbackFragment_Reviewed);
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.editProfileFragment);
            }
        });

        recep_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.RecepientInfoFragment);
            }
        });

        order_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.OrderHistoryFragment);
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                mAuth.signOut();
                                navController.navigate(R.id.loginFragment);
                                CustomToast signOutToast = new CustomToast();
                                signOutToast.ShowToastMessage(getActivity(), 1, "Đăng xuất thành công");
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }
    public void setQuantity(TextView textView, String path){
        FirebaseUser fbUser = mAuth.getInstance().getCurrentUser();
        db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.getString("customerId").equals(fbUser.getUid())){
                                    count++;
                                }
                            }
                            size_text = Integer.toString(count);
                            textView.setText(size_text);
                            Log.d(TAG, path + ": " + size_text);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void setQuantityFeedback(TextView textView, String path){
        FirebaseUser fbUser = mAuth.getInstance().getCurrentUser();
        db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.getString("customer_id").equals(fbUser.getUid())){
                                    count++;
                                }
                            }
                            size_text = Integer.toString(count);
                            textView.setText(size_text);
                            Log.d(TAG, path + ": " + size_text);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}