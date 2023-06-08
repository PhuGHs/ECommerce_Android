package com.example.ecommerce_hvpp.fragments.customer_fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.util.Validator;
import com.example.ecommerce_hvpp.viewmodel.Customer.RecepInfoViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class NewRecepInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewRecepInfoFragment() {
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
    public static NewRecepInfoFragment newInstance(String param1, String param2) {
        NewRecepInfoFragment fragment = new NewRecepInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private NavController navController;
    private ImageButton back_Account_btn;
    private RecepInfoViewModel viewModel;
    private Button save_btn;
    private Button cancel_btn;
    private TextInputLayout address_edt;
    private TextInputLayout name_edt;
    private TextInputLayout phonenumber_edt;

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
        View v = inflater.inflate(R.layout.actitvity_new_recepinfo, container, false);
        viewModel = new ViewModelProvider(this).get(RecepInfoViewModel.class);

        return v;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());
        back_Account_btn = (ImageButton) view.findViewById(R.id.back_info);
        save_btn = (Button) view.findViewById(R.id.save_btn_newrecep);
        cancel_btn = (Button) view.findViewById(R.id.cancel_btn_newrecep);
        address_edt = (TextInputLayout) view.findViewById(R.id.new_recep_address);
        name_edt = (TextInputLayout) view.findViewById(R.id.new_recep_name);
        phonenumber_edt = (TextInputLayout) view.findViewById(R.id.new_recep_phonenumber);

        name_edt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!Validator.isValidUsername(name_edt.getEditText().getText().toString())) {
                    name_edt.setError("Invalid name");
                } else {
                    name_edt.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phonenumber_edt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Validator.isValidUsername(phonenumber_edt.getEditText().getText().toString())) {
                    phonenumber_edt.setError("Invalid phone number");
                    save_btn.setEnabled(false);
                } else {
                    phonenumber_edt.setError(null);
                    save_btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        address_edt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Validator.isValidUsername(address_edt.getEditText().getText().toString())) {
                    address_edt.setError("Invalid address");
                    save_btn.setEnabled(false);
                }
                else {
                    address_edt.setError(null);
                    save_btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        back_Account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.RecepientInfoFragment);
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
                                String name = name_edt.getEditText().getText().toString();
                                String address = address_edt.getEditText().getText().toString();
                                String phonenumber = phonenumber_edt.getEditText().getText().toString();
                                viewModel.addNewRecepInfo(address, name, phonenumber, false);
                                navController.navigate(R.id.RecepientInfoFragment);
                                CustomToast successToast = new CustomToast();
                                successToast.ShowToastMessage(requireActivity(), 1, "Add new recepient's information successfully");
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Add this new recepient's info?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
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

    }
}
