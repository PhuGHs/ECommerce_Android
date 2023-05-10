package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.DataViewHolder>{
    private ArrayList<Pair<Product, Integer>> listCart;
    private Context context;

    public CartAdapter(Context context, ArrayList<Pair<Product, Integer>> listCart) {
        this.context = context;
        this.listCart = listCart;
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    @Override
    public CartAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);

        return new CartAdapter.DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.DataViewHolder holder, int position) {
        holder.productName.setText(listCart.get(position).first.getName());
        holder.productSeason.setText(listCart.get(position).first.getSeason());
        holder.productPrice.setText(String.valueOf(listCart.get(position).first.getPrice()));

        //int count = listCart.get(position).second;
        holder.quantity.setText(String.valueOf(listCart.get(position).second));

        //change quantity
    }

    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productSeason, productPrice, quantity;
        private ImageButton minusQuantity, plusQuantity;
        public DataViewHolder(View itemView) {
            super(itemView);

            productName = (TextView) itemView.findViewById(R.id.cart_productName);
            productSeason = (TextView) itemView.findViewById(R.id.cart_productSeason);
            productPrice = (TextView) itemView.findViewById(R.id.cart_productPrice);
            quantity = (TextView) itemView.findViewById(R.id.cart_quantity);
            minusQuantity = (ImageButton) itemView.findViewById(R.id.minusQuantity);
            plusQuantity = (ImageButton) itemView.findViewById(R.id.plusQuantity);
        }
    }
}
