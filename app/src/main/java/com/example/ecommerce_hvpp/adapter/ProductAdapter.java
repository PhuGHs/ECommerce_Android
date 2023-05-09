package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.DataViewHolder> {
    private ArrayList<Product> listProduct;
    private Context context;

    public ProductAdapter(Context context, ArrayList<Product> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    @Override
    public ProductAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);

        return new DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.productName.setText(listProduct.get(position).getName());
        holder.productSeason.setText(listProduct.get(position).getSeason());
        holder.productPrice.setText(String.valueOf(listProduct.get(position).getPrice()));
    }

    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productSeason, productPrice;
        private ImageButton btnFav;
        public DataViewHolder(View itemView) {
            super(itemView);

            productName = (TextView) itemView.findViewById(R.id.productName);
            productSeason = (TextView) itemView.findViewById(R.id.productSeason);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);

            btnFav = (ImageButton) itemView.findViewById(R.id.btnFav);
        }
    }
}
