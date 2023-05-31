package com.example.ecommerce_hvpp.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.fragments.admin_fragments.AdminOrderedListFragment;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.util.CurrencyFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AdminOrderManagementAdapter extends RecyclerView.Adapter<AdminOrderManagementAdapter.OrderManagementViewHolder> {
    private List<Order> list;
    private AdminOrderedListFragment parent;

    public AdminOrderManagementAdapter(List<Order> list, AdminOrderedListFragment parent) {
        this.list = list;
        this.parent = parent;
    }

    @NonNull
    @Override
    public OrderManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderManagementViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_admin, null));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderManagementViewHolder holder, int position) {
        Order order = list.get(position);
        if(order == null) {
            return;
        }
        holder.bind(order, "ORDER#" + String.valueOf(position));
        holder.itemView.setOnClickListener(i -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("orderInfo", order);
            parent.getNavController().navigate(R.id.navigate_to_orderdetails, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class OrderManagementViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderId, tvRecipientName, tvAddress, tvPrice, tvOrderDate, tvOrderStatus;

        public OrderManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvRecipientName = itemView.findViewById(R.id.tvRecipientName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvStatus);
        }

        public void bind(Order order, String sequenceNumber) {
            tvOrderId.setText(sequenceNumber);
            tvRecipientName.setText(order.getRecipientName());
            tvAddress.setText(order.getAddress());
            tvPrice.setText(CurrencyFormat.getVNDCurrency(order.getTotalPrice()));
            tvOrderDate.setText(formatDate(order.getCreatedDate()));
            if(Objects.equals(order.getStatus(), "Pending")) {
                tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.pending));
            } else if (Objects.equals(order.getStatus(), "Packaged")) {
                tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.packaged));
            } else if (Objects.equals(order.getStatus(), "Confirmed")) {
                tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.confirmed));
            } else if (Objects.equals(order.getStatus(), "Delivered")) {
                tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.delivered));
            }
            tvOrderStatus.setText(order.getStatus().toString());
        }

        private String formatDate(long timeStamp) {
            Date date = new Date(timeStamp);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            return sdf.format(date);
        }
    }
}
