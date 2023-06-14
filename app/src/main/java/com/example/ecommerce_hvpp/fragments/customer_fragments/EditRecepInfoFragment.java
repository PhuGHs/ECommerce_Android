package com.example.ecommerce_hvpp.fragments.customer_fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.RecepInfo;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.viewmodel.Customer.OrderHistoryViewModel;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProfileViewModel;
import com.example.ecommerce_hvpp.viewmodel.Customer.RecepInfoViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditRecepInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditRecepInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFrament.
     */
    // TODO: Rename and change types and number of parameters
    public static EditRecepInfoFragment newInstance(String param1, String param2) {
        EditRecepInfoFragment fragment = new EditRecepInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private NavController navController;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecepInfoViewModel viewModel;
    private ImageButton back_Account_btn;
    private Button cancel_btn;
    private Button save_btn;
    private EditText name_edt;
    private EditText phonenumber_edt;
    private EditText address_edt;
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
        View v = inflater.inflate(R.layout.activity_edit_recepientinfo, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        name_edt = v.findViewById(R.id.name_recep_edittext);
        phonenumber_edt = v.findViewById(R.id.phonenumber_recep_edittext);
        address_edt = v.findViewById(R.id.address_recep_edittext);

        viewModel = new ViewModelProvider(this).get(RecepInfoViewModel.class);

        String id = getArguments().getString("recep_id");

        viewModel.showRecepDetail(id).observe(requireActivity(), RecepInfo -> {
            name_edt.setText(RecepInfo.getName());
            phonenumber_edt.setText(RecepInfo.getPhonenumber());
            address_edt.setText(RecepInfo.getAddress());
        });

        return v;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());

        back_Account_btn = (ImageButton) view.findViewById(R.id.back_info);
        cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
        save_btn = (Button) view.findViewById(R.id.save_btn);

        back_Account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.RecepientInfoFragment);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                navController.navigate(R.id.RecepientInfoFragment);
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

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                String id = getArguments().getString("recep_id");
                                updateRecepDetail(id);
                                navController.navigate(R.id.RecepientInfoFragment);
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
    public void updateRecepDetail(String recep_id){
        String name = name_edt.getText().toString();
        String phonenumber = phonenumber_edt.getText().toString();
        String address = address_edt.getText().toString();
        RecepInfo info = new RecepInfo();
        info.setName(name);
        info.setPhonenumber(phonenumber);
        info.setAddress(address);
        viewModel.updateRecepDetail(info, recep_id);
    }
}
