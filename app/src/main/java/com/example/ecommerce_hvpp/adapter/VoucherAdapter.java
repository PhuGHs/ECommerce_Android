package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.util.Pair;
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

public class VoucherAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Pair<String, String>> listVoucher;

    public VoucherAdapter(@NonNull Context context, int resource, ArrayList<Pair<String, String>> listVoucher) {
        super(context, resource, listVoucher);
        this.context = context;
        this.listVoucher = listVoucher;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null){
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.voucher_item,parent,false);
        }

        TextView voucherCode = (TextView) v.findViewById(R.id.voucherCode);
        TextView voucherValue = (TextView) v.findViewById(R.id.voucherValue);

        voucherCode.setText(listVoucher.get(position).first);
        voucherValue.setText("-" + listVoucher.get(position).second);

        return v;
    }
}
