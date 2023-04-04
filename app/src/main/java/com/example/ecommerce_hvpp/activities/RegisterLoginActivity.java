package com.example.ecommerce_hvpp.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.util.Validator;
import com.example.ecommerce_hvpp.viewmodel.RegisterLoginViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterLoginActivity extends AppCompatActivity {
    private RegisterLoginViewModel viewModel;
    private TextInputLayout username, email, password, confirmPassword;
    private Button registerButton;
    private View image;
    private ViewModelProvider vmProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        vmProvider = new ViewModelProvider(this);
        viewModel = vmProvider.get(RegisterLoginViewModel.class);
        username = findViewById(R.id.reg_name);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_password);
        confirmPassword = findViewById(R.id.reg_confirmPassword);
        registerButton = findViewById(R.id.reg_btn);

        username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!Validator.isValidUsername(username.getEditText().getText().toString())) {
                    username.setError("Invalid username");
                    return;
                }
                username.setError(null);
            }
        });

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Validator.isValidEmail(email.getEditText().getText().toString())) {
                    email.setError("Invalid email");
                    return;
                }
                email.setError(null);
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
                }
                else {
                    email.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Validator.isValidPassword(password.getEditText().getText().toString(), confirmPassword.getEditText().getText().toString())) {
                    confirmPassword.setError("Passwords do not match");
                }
                else {
                    email.setError(null);
                }
            }
        });

        registerButton.setOnClickListener(view -> {
            String str_email = email.getEditText().getText().toString().trim();
            String str_password = password.getEditText().toString().trim();

            viewModel.registerUser(str_email, str_password).observe(this, resource -> {
                switch(resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                        break;
                    case ERROR:
                        CustomToast ct = new CustomToast();
                        ct.ShowToastMessage(this, 2, resource.message);
                        break;
                }
            });
        });
    }
}
