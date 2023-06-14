package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;

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

        return new DataViewHolder(itemView).linkAdapter(this);
    }
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Product product = listProduct.get(position);

        holder.Bind(product);

        if (isFavorite == true) {
            holder.btnFav.setImageResource(R.drawable.full_heart);
        }
        MainActivity.PDviewModel.isFavorite(product.getId()).observe((LifecycleOwner) context, Favorite -> {
            if (Favorite) holder.btnFav.setImageResource(R.drawable.full_heart);
            else holder.btnFav.setImageResource(R.drawable.outline_heart);
            holder.btnFav.setOnClickListener(view -> {
                if (Favorite) {
                    MainActivity.PDviewModel.removeFromWishList(context, product.getId());
                    holder.btnFav.setImageResource(R.drawable.outline_heart);
                }
                else {
                    MainActivity.PDviewModel.addToWishList(context, product.getId());
                    holder.btnFav.setImageResource(R.drawable.full_heart);
                }
            });
        });


        //navigate to detail
        NavController navController = Navigation.findNavController(view);
        holder.productLayout.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("productID",product.getId());

            navController.navigate(R.id.detailProductCustomerFragment, bundle);
        });
    }

    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productSeason, productPrice;
        private ImageButton btnFav;
        private CardView productLayout;
        private ImageView URLthumb;
        private ProductAdapter adapter;
        public DataViewHolder(View itemView) {
            super(itemView);

            productLayout = (CardView) itemView.findViewById(R.id.productLayout);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productSeason = (TextView) itemView.findViewById(R.id.productSeason);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
            URLthumb = (ImageView) itemView.findViewById(R.id.productURLthumb);

            btnFav = (ImageButton) itemView.findViewById(R.id.btnFav);
        }
        public void Bind(Product product){
            productName.setText(product.getName());
            productSeason.setText(product.getSeason());
            productPrice.setText("$"+String.valueOf(product.getPrice()));

            Glide.with(itemView)
                    .asBitmap()
                    .load(product.getUrlthumb())
                    .fitCenter()
                    .into(URLthumb);
        }
        public DataViewHolder linkAdapter(ProductAdapter adapter){
            this.adapter = adapter;
            return this;
        }
    }
}
