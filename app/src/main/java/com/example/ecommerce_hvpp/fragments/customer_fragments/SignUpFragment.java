package com.example.ecommerce_hvpp.fragments.customer_fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
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
import com.example.ecommerce_hvpp.util.Validator;
import com.example.ecommerce_hvpp.viewmodel.Customer.RegisterLoginViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpFragment extends Fragment {
    private TextInputLayout username, email, password, confirmPassword;
    private TextView loginButton;
    private Button registerButton;
    private RegisterLoginViewModel viewModel;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(RegisterLoginViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());
        username = view.findViewById(R.id.reg_name);
        email = view.findViewById(R.id.reg_email);
        password = view.findViewById(R.id.reg_password);
        confirmPassword = view.findViewById(R.id.reg_confirmPassword);
        registerButton = view.findViewById(R.id.reg_btn);
        loginButton = view.findViewById(R.id.reg_login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signUpFragment);
            }
        });

        username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!Validator.isValidUsername(username.getEditText().getText().toString())) {
                    username.setError("Invalid username");
                } else {
                    username.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Validator.isValidEmail(email.getEditText().getText().toString())) {
                    email.setError("Invalid email");
                    registerButton.setEnabled(false);
                } else {
                    email.setError(null);
                    registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Validator.isValidPasswordHasEnoughCharacter(password.getEditText().getText().toString())) {
                    password.setError("Passwords length must be more than 8 characters");
                    registerButton.setEnabled(false);
                } else {
                    password.setError(null);
                    registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        confirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Validator.isValidPassword(password.getEditText().getText().toString(), confirmPassword.getEditText().getText().toString())) {
                    confirmPassword.setError("Passwords do not match");
                    registerButton.setEnabled(false);
                } else {
                    confirmPassword.setError(null);
                    registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        registerButton.setOnClickListener(views -> {
            String str_email = email.getEditText().getText().toString().trim();
            String str_password = password.getEditText().getText().toString();
            String str_username = username.getEditText().getText().toString();

            viewModel.registerUser(str_email, str_password, str_username).observe(requireActivity(), resource -> {
                switch(resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.SnackBarSuccess);
                        Snackbar.make(ctw, requireView(), "Đăng ký thành công!", Snackbar.LENGTH_LONG).show();
                        navController.navigate(R.id.loginFragment);
                        break;
                    case ERROR:
                        ContextThemeWrapper ctw2 = new ContextThemeWrapper(getActivity(), R.style.SnackBarError);
                        Snackbar.make(ctw2, requireView(), resource.message, Snackbar.LENGTH_LONG).show();
                        break;
                }
            });
        });
    }
}
