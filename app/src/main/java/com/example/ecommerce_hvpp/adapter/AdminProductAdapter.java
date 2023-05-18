package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.util.CurrencyFormat;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter {
    private List<Product> list;
    private Context context;
    private OnLongItemClickListener mOnLongItemClickListener;

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        mOnLongItemClickListener = onLongItemClickListener;
    }
    public interface OnLongItemClickListener {
        void itemLongClicked(View v, int position);
    }

    public AdminProductAdapter(Context context, List<Product> list) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_admin, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdminProductViewHolder viewHolder = (AdminProductViewHolder) holder;
        viewHolder.bind(list.get(position));
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mOnLongItemClickListener != null) {
                    mOnLongItemClickListener.itemLongClicked(view, position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class AdminProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivProductImage;
        private TextView tvProductName, tvPrice;
        public AdminProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
        public void bind(Product pd) {
            Glide.with(itemView)
                    .load(pd.getURLthumb())
                    .fitCenter()
                    .into(ivProductImage);
            tvProductName.setText(pd.getName());
            tvPrice.setText(CurrencyFormat.getVNDCurrency(pd.getPrice()));
        }
    }
}
