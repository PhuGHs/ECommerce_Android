package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.fragments.DetailProductCustomerFragment;
import com.example.ecommerce_hvpp.model.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.DataViewHolder> {
    private ArrayList<Product> listProduct;
    private Context context;
    private View view;
    private boolean isFavorite;

    public ProductAdapter(Context context, ArrayList<Product> listProduct, View view, Boolean isFavorite) {
        this.context = context;
        this.listProduct = listProduct;
        this.view = view;
        this.isFavorite = isFavorite;
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
        if (isFavorite == true) {
            holder.btnFav.setImageResource(R.drawable.full_heart);
        }

        //navigate to detail
        NavController navController = Navigation.findNavController(view);
        holder.productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("productID","P001");
                bundle.putInt("fragmentPrevious",R.id.homeFragment);

                navController.navigate(R.id.detailProductCustomerFragment, bundle);
            }
        });
    }

    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productSeason, productPrice;
        private ImageButton btnFav;
        private LinearLayout productLayout;
        public DataViewHolder(View itemView) {
            super(itemView);

            productLayout = (LinearLayout) itemView.findViewById(R.id.productLayout);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productSeason = (TextView) itemView.findViewById(R.id.productSeason);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);

            btnFav = (ImageButton) itemView.findViewById(R.id.btnFav);
        }
    }
}
