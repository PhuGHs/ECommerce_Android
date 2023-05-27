package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.model.Cart;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.DataViewHolder>{
    private List<Cart> listCart;
    private Context context;

    public CartAdapter(Context context, List<Cart> listCart) {
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

        return new CartAdapter.DataViewHolder(itemView).linkAdapter(this);
    }
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.DataViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        Product product = cart.getProduct();
        int p = position;

        holder.productName.setText(product.getName());
        holder.productSeason.setText(product.getSeason());
        holder.productPrice.setText("$"+ product.getPrice());

        Log.d("In cart", product.getName());
        holder.size.setText(cart.getSize());
        holder.quantity.setText(String.valueOf(cart.getQuantity()));

        Glide.with(context)
                .asBitmap()
                .load(product.getUrlthumb())
                .fitCenter()
                .into(holder.thumb);
        holder.btnRemove.setOnClickListener(view -> {
            Log.d("Remove from cart",product.getName());
            holder.adapter.listCart.remove(p);
            holder.adapter.notifyItemRemoved(p);
            MainActivity.PDviewModel.removeFromCart(product.getId(), cart.getQuantity(), cart.getSize());
        });

        //change quantity
    }

    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productSeason, productPrice, quantity, size;
        private ImageButton minusQuantity, plusQuantity, btnRemove;
        private ImageView thumb;
        private CartAdapter adapter;
        public DataViewHolder(View itemView) {
            super(itemView);

            thumb = (ImageView) itemView.findViewById(R.id.cartThumb);
            productName = (TextView) itemView.findViewById(R.id.cart_productName);
            productSeason = (TextView) itemView.findViewById(R.id.cart_productSeason);
            productPrice = (TextView) itemView.findViewById(R.id.cart_productPrice);
            quantity = (TextView) itemView.findViewById(R.id.cart_quantity);
            size = (TextView) itemView.findViewById(R.id.sizeChosen);
            minusQuantity = (ImageButton) itemView.findViewById(R.id.minusQuantity);
            plusQuantity = (ImageButton) itemView.findViewById(R.id.plusQuantity);
            btnRemove = (ImageButton) itemView.findViewById(R.id.btnRemoveFromCart);
        }
        public DataViewHolder linkAdapter(CartAdapter adapter){
            this.adapter = adapter;
            return this;
        }
    }
}
