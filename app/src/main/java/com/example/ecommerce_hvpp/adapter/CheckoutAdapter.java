package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommerce_hvpp.R;

import java.util.ArrayList;
import java.util.List;

public class CheckoutAdapter extends ArrayAdapter {
    private ArrayList<String> listTypeCheckout;
    private Context context;

    public CheckoutAdapter(@NonNull Context context, int resource, ArrayList<String> listTypeCheckout) {
        super(context, resource, listTypeCheckout);
        this.context = context;
        this.listTypeCheckout = listTypeCheckout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null){
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.simple_spinner_string_item,parent,false);
        }

        TextView item = (TextView) v.findViewById(R.id.spinner_string_item);
        item.setText(listTypeCheckout.get(position));

        return v;
    }
}
