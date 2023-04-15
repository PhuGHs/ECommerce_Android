package com.example.ecommerce_hvpp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.fragments.LoginFragment;
import com.example.ecommerce_hvpp.fragments.SignUpFragment;
import com.example.ecommerce_hvpp.viewmodel.RegisterLoginViewModel;

public class RegisterLoginActivity extends AppCompatActivity {
    private NavController navController;
    private RegisterLoginViewModel viewModel;
    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;

    private View image;
    private ViewModelProvider vmProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.loginFragment);

        vmProvider = new ViewModelProvider(this);
        viewModel = vmProvider.get(RegisterLoginViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
