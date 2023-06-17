package com.example.ecommerce_hvpp.fragments.customer_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.ecommerce_hvpp.util.SessionManager;
import com.example.ecommerce_hvpp.util.Validator;
import com.example.ecommerce_hvpp.viewmodel.Customer.RegisterLoginViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginFragment extends Fragment {
    private CircularProgressButton loginButton;
    private TextView btnResetPassword;
    private NavController navController;
    private TextInputLayout email, password;
    private TextView signup;
    private RegisterLoginViewModel viewModel;
    private SessionManager sessionManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(RegisterLoginViewModel.class);
        sessionManager = new SessionManager(getContext());
        email = view.findViewById(R.id.log_email);
        signup = view.findViewById(R.id.reg_signup_button);
        password = view.findViewById(R.id.log_password);
        loginButton = view.findViewById(R.id.login_btn);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
            ResetPasswordDialog resetPasswordDialog = new ResetPasswordDialog(getContext());
            resetPasswordDialog.show();
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getEditText().getText().toString().trim();
                String str_password = password.getEditText().getText().toString();

                if(str_email.isEmpty() || str_password.isEmpty()) {
                    ContextThemeWrapper ctw2 = new ContextThemeWrapper(getActivity(), R.style.SnackBarError);
                    Snackbar.make(ctw2, requireView(), "Please fill in all the blanks!", Snackbar.LENGTH_LONG).show();
                    return;
                }

                viewModel.loginUser(str_email, str_password).observe(getViewLifecycleOwner(), resource -> {
                    switch(resource.status) {
                        case LOADING:
                            loginButton.startAnimation();
                            break;
                        case SUCCESS:
                            Intent intent = new Intent(requireActivity(), MainActivity.class);
                            intent.putExtra("UID", viewModel.UID);
                            sessionManager.saveSession(str_email, str_password);
                            startActivity(intent);
                            email.getEditText().setText("");
                            password.getEditText().setText("");
                            loginButton.revertAnimation();
                            email.setError(null);
                            password.setError(null);
                            break;
                        case ERROR:
                            loginButton.revertAnimation();
                            ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.SnackBarError);
                            Snackbar.make(ctw, requireView(), resource.message, Snackbar.LENGTH_LONG).show();
                            break;
                    }
                });

                viewModel.insertVoucherForNewUser();
            }
        });

        navController = Navigation.findNavController(requireView());
    }

}
