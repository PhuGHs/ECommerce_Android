package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.fragments.customer_fragments.OrderProgressFragment;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.repositories.customerRepositories.RecepInfoRepository;
import com.example.ecommerce_hvpp.repositories.customerRepositories.UserRepository;

import java.util.ArrayList;

public class OrderProgressAdapter extends RecyclerView.Adapter<OrderProgressAdapter.DataViewHolder>{
    private OrderProgressFragment parent;
    private ArrayList<Order> listOrder;

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

        holder.id_tv.setText("ID: #" + order.getId());
        holder.title_tv.setText(order.getTitle());
        holder.time_remain_tv.setText("Remaining day:    " + Integer.toString(order.getRemaining_day()) + " day");

        holder.detail_btn.setOnClickListener(new View.OnClickListener() {
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
        private TextView id_tv, title_tv, time_remain_tv;
        private ImageView image;
        private Button detail_btn;
        public DataViewHolder(View itemView){
            super(itemView);

            id_tv = (TextView) itemView.findViewById(R.id.id_of_order);
            title_tv = (TextView) itemView.findViewById(R.id.title_of_order);
            time_remain_tv = (TextView) itemView.findViewById(R.id.timeremain_of_order);
            image = (ImageView) itemView.findViewById(R.id.image_of_orderprogress);

            detail_btn = (Button) itemView.findViewById(R.id.detail_orderprogresss_btn);
        }
    }
}
