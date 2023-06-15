package com.example.ecommerce_hvpp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.fragments.customer_fragments.OrderProgressFragment;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.customerRepositories.RecepInfoRepository;
import com.example.ecommerce_hvpp.repositories.customerRepositories.UserRepository;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.viewmodel.Customer.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrderProgressAdapter extends RecyclerView.Adapter<OrderProgressAdapter.DataViewHolder>{
    private OrderProgressFragment parent;
    private ArrayList<Order> listOrder;
    private OrderViewModel viewModel;

    public OrderProgressAdapter(OrderProgressFragment parent, ArrayList<Order> listOrder) {
        this.parent = parent;
        this.listOrder = listOrder;
    }
    public int getItemCount() {
        return listOrder.size();
    }
    @NonNull
    @Override
    public OrderProgressAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderprogress_detail, parent, false);

        return new OrderProgressAdapter.DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull OrderProgressAdapter.DataViewHolder holder, int position) {
        Order order = listOrder.get(position);
        viewModel = new ViewModelProvider(parent).get(OrderViewModel.class);

        holder.id_tv.setText("ID: #" + order.getId());
        holder.title_tv.setText(order.getTitle());

        if (order.getStatus().equals("Delivered")){
            holder.status_tv.setText("Delivered");
            holder.status_tv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.confirmed));
        }
        else if (order.getStatus().equals("Pending")){
            holder.status_tv.setText("Pending");
            holder.status_tv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.pending));
        }
        else {
            holder.status_tv.setText("Packed");
            holder.status_tv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.packaged));
        }
        holder.confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.confirmOrder(order.getId());
                viewModel.confirmItemsOfOrder(order.getId());
                holder.confirm_btn.setVisibility(View.INVISIBLE);
                CustomToast signOutToast = new CustomToast();
                signOutToast.ShowToastMessage(parent.getActivity(), 1, "Confirm order successfully");
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("orderprogress_id", order.getId());
                parent.getNavController().navigate(R.id.navigate_to_detail_orderprogress, bundle);
            }
        });

    }
    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView id_tv, title_tv, status_tv;
        private ImageView image;
        private Button confirm_btn;
        private LinearLayout linearLayout;
        public DataViewHolder(View itemView){
            super(itemView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.orderprogress_layout);
            id_tv = (TextView) itemView.findViewById(R.id.id_of_order);
            title_tv = (TextView) itemView.findViewById(R.id.title_of_order);
            status_tv = (TextView) itemView.findViewById(R.id.status_of_order);
            image = (ImageView) itemView.findViewById(R.id.image_of_orderprogress);

            confirm_btn = (Button) itemView.findViewById(R.id.confirm_btn);
        }
    }
}
