package com.example.ecommerce_hvpp.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.activities.RegisterLoginActivity;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.util.Validator;
import com.example.ecommerce_hvpp.viewmodel.RegisterLoginViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

public class ResetPasswordDialog extends BottomSheetDialog {
    private TextInputLayout email;
    private Button cancel, send;
    private RegisterLoginViewModel viewModel;
    private RegisterLoginActivity activity;
    private static final int DISMISS_DELAY_MS = 1500;
    private Handler handler = new Handler();
    private Context context;
    public ResetPasswordDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_dialog);

        activity = (RegisterLoginActivity) context;
        viewModel = new ViewModelProvider(activity).get(RegisterLoginViewModel.class);
        email = findViewById(R.id.email);
        cancel = findViewById(R.id.cancel_button);
        send = findViewById(R.id.send_button);

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!Validator.isValidEmail(email.getEditText().getText().toString().trim())) {
                    email.setError("Invalid email!");
                    send.setEnabled(false);
                } else {
                    email.setError(null);
                    send.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cancel.setOnClickListener((View view) -> {
            dismiss();
        });

        send.setOnClickListener((View view) -> {
            viewModel.resetUserPassword(email.getEditText().getText().toString().trim()).observe(activity, resource -> {
                switch(resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        CustomToast resetSuccessToast = new CustomToast();
                        resetSuccessToast.ShowToastMessage(context, 1, "Successfully sent password reset request to your Email");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismiss();
                            }
                        }, DISMISS_DELAY_MS);
                        break;
                    case ERROR:
                        CustomToast resetFailureToast = new CustomToast();
                        resetFailureToast.ShowToastMessage(context, 2, resource.message);
                        break;
                }
            });
        });
    }

}
