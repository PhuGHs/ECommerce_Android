package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.Voucher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VoucherListAdapter extends RecyclerView.Adapter<VoucherListAdapter.DataViewHolder> {
    private Context context;
    private ArrayList<Voucher> listVoucher;

    public VoucherListAdapter(Context context, ArrayList<Voucher> listVoucher) {
        this.context = context;
        this.listVoucher = listVoucher;
    }
    public int getItemCount() {
        return listVoucher.size();
    }
    @NonNull
    @Override
    public VoucherListAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher, parent, false);

        return new VoucherListAdapter.DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull VoucherListAdapter.DataViewHolder holder, int position) {
        Voucher voucher = listVoucher.get(position);

        holder.name_tv.setText(voucher.getVoucherName());
        holder.code_tv.setText("Code:   " + voucher.getId());
        holder.discount_value_tv.setText(String.valueOf(voucher.getDiscountedValue()));

        holder.date_end_tv.setText(getDate(voucher.getEndDate()));
    }
    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView name_tv, code_tv, discount_value_tv, date_end_tv;
        private ImageView image;
        public DataViewHolder(View itemView){
            super(itemView);

            name_tv = (TextView) itemView.findViewById(R.id.title_of_voucher);
            code_tv = (TextView) itemView.findViewById(R.id.code_of_voucher);
            discount_value_tv = (TextView) itemView.findViewById(R.id.discount_of_voucher);
            date_end_tv = (TextView) itemView.findViewById(R.id.expireday_of_voucher);
            image = (ImageView) itemView.findViewById(R.id.image_of_voucher);
        }
    }
    public String getDate(long timeStamp){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedTime = dateFormat.format(new Date(timeStamp));

        return formattedTime;
    }
}
