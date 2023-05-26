package com.example.ecommerce_hvpp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.OrderDetail;
import com.example.ecommerce_hvpp.util.CurrencyFormat;

import java.util.List;

public class AdminOrderItemAdapter extends RecyclerView.Adapter<AdminOrderItemAdapter.OrderItemViewHolder> {
    private List<OrderDetail> list;

    public AdminOrderItemAdapter(List<OrderDetail> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productitem_order, null));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        holder.bind(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvType, tvQuantity, tvPrice;
        private ImageView imgItem;
        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }

        public void bind(OrderDetail pd) {
            Glide.with(itemView)
                            .load(pd.getImage())
                                    .fitCenter()
                                            .into(imgItem);
            tvName.setText(pd.getName());
            tvQuantity.setText("Quantity: " + String.valueOf(pd.getQuantity()));
            tvType.setText("Type: " + pd.getSize().toString());
            tvPrice.setText(CurrencyFormat.getVNDCurrency(pd.getPrice()));
        }
    }
}
