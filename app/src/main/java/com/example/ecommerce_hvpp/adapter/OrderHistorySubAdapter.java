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
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.fragments.customer_fragments.OrderHistoryDetailFragment;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.model.OrderHistoryItem;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;

import java.util.ArrayList;
import java.util.List;

public class OrderHistorySubAdapter extends RecyclerView.Adapter<OrderHistorySubAdapter.DataViewHolder>{
    private OrderHistoryDetailFragment parent;
    private ArrayList<OrderHistorySubItem> subItemList;
    public OrderHistorySubAdapter(OrderHistoryDetailFragment parent, ArrayList<OrderHistorySubItem> subItemList){
        this.parent = parent;
        this.subItemList = subItemList;
    }
    public int getItemCount() {
        return subItemList.size();
    }
    @NonNull
    @Override
    public OrderHistorySubAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);

        return new OrderHistorySubAdapter.DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull OrderHistorySubAdapter.DataViewHolder holder, int position) {
        OrderHistorySubItem subItem = subItemList.get(position);

        holder.name_tv.setText(subItem.getName_subItem());
        holder.quantity_tv.setText("Quantity: " + subItem.getQuantity_subItem());
        holder.sum_of_sub_tv.setText("$" + Double.toString(subItem.getSum_subItem()));
        Glide.with(holder.itemView).load(subItem.getImagePath_subItem()).fitCenter().into(holder.image);

        holder.review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle(); Get data soon

                parent.getNavController().navigate(R.id.navigate_to_feedback);
            }
        });
    }
    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView name_tv, date_create_tv, quantity_tv, sum_of_sub_tv;
        private ImageView image;
        private Button review_btn;
        public DataViewHolder(View itemView){
            super(itemView);

            name_tv = (TextView) itemView.findViewById(R.id.name_of_ordereditem);
            quantity_tv = (TextView) itemView.findViewById(R.id.quantity_of_ordereditem);
            sum_of_sub_tv = (TextView) itemView.findViewById(R.id.total_money_ordereditem);
            image = (ImageView) itemView.findViewById(R.id.image_of_item_orderhistory);

            review_btn = (Button) itemView.findViewById(R.id.review_btn);
        }
    }
}
