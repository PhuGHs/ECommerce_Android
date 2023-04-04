package com.example.ecommerce_hvpp.util.CustomComponent;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce_hvpp.R;

import javax.annotation.Nonnull;

public class CustomToast {
    public static void ShowToastMessage(@Nonnull Context context, int type, String message) {
        Toast toast = new Toast(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_toast, (ViewGroup) ((Activity) context).findViewById(R.id.layout_custom_toast1));
        RelativeLayout bg = view.findViewById(R.id.toast_bg);
        ImageView ic = view.findViewById(R.id.toast_icon);
        TextView titleState = view.findViewById(R.id.toast_state);
        TextView contentStr = view.findViewById(R.id.toast_content);

        //Assign
        if(type == 1)   { // Success
            bg.setBackgroundResource(R.drawable.toast_success);
            ic.setImageResource(R.drawable.baseline_check_24);
            titleState.setText("Success");
        }
        if(type == 2)   { // Error
            bg.setBackgroundResource(R.drawable.toast_error);
            ic.setImageResource(R.drawable.baseline_error_24);
            titleState.setText("Error");
        }
        if(type == 3)   { // Warning
            bg.setBackgroundResource(R.drawable.toast_warning);
            ic.setImageResource(R.drawable.baseline_warning_24);
            titleState.setText("Warning");
        }
        if(type == 4)   { // Information
            bg.setBackgroundResource(R.drawable.toast_normal);
            ic.setImageResource(R.drawable.baseline_info_24);
            titleState.setText("Information");
        }
        contentStr.setText(message);

        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
