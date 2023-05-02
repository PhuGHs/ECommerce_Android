package com.example.ecommerce_hvpp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.dialog.ResetPasswordDialog;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.util.Validator;
import com.example.ecommerce_hvpp.viewmodel.RegisterLoginViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {
    private Button loginButton;
    private TextView btnResetPassword;
    private NavController navController;
    private TextInputLayout email, password;
    private TextView signup;
    private RegisterLoginViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(RegisterLoginViewModel.class);
        email = view.findViewById(R.id.log_email);
        signup = view.findViewById(R.id.reg_signup_button);
        password = view.findViewById(R.id.log_password);
        loginButton = view.findViewById(R.id.login_btn);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!Validator.isValidEmail(email.getEditText().getText().toString().trim())) {
                    email.setError("Invalid email");
                } else {
                    email.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.signUpFragment);
            }
        });

        btnResetPassword.setOnClickListener((View v) -> {
            ResetPasswordDialog resetPasswordDialog = new ResetPasswordDialog(getActivity());
            resetPasswordDialog.show();
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getEditText().getText().toString().trim();
                String str_password = password.getEditText().getText().toString();

                viewModel.loginUser(str_email, str_password).observe(requireActivity(), resource -> {
                    switch(resource.status) {
                        case LOADING:
                            break;
                        case SUCCESS:
                            Intent intent = new Intent(requireActivity(), MainActivity.class);
                            intent.putExtra("UID", viewModel.UID);
                            startActivity(intent);
                            break;
                        case ERROR:
                            CustomToast loginErrorToast = new CustomToast();
                            loginErrorToast.ShowToastMessage(requireActivity(), 2, resource.message);
                            break;
                    }
                });
            }
        });

        navController = Navigation.findNavController(requireView());
    }

}
