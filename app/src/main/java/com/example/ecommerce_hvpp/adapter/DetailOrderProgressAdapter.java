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
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;

import java.util.ArrayList;

public class DetailOrderProgressAdapter extends RecyclerView.Adapter<DetailOrderProgressAdapter.DataViewHolder> {
    private Context context;
    private ArrayList<OrderHistorySubItem> listItems;
    public DetailOrderProgressAdapter(Context context, ArrayList<OrderHistorySubItem> listItems){
        this.context = context;
        this.listItems = listItems;
    }
    public int getItemCount() {
        return listItems.size();
    }
    @NonNull
    @Override
    public DetailOrderProgressAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details_orderprogress, parent, false);

        return new DetailOrderProgressAdapter.DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull DetailOrderProgressAdapter.DataViewHolder holder, int position) {
        OrderHistorySubItem subItem = listItems.get(position);

        holder.name_tv.setText(subItem.getName_subItem());
        holder.quantity_tv.setText("Quantity:   " + subItem.getQuantity_subItem());
        holder.price_tv.setText("$" + Double.toString(subItem.getSum_subItem()));
        Glide.with(holder.itemView).load(subItem.getImagePath_subItem()).fitCenter().into(holder.image);
    }
    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView name_tv, price_tv, quantity_tv;
        private ImageView image;
        public DataViewHolder(View itemView){
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image_item_orderprogress_detail);
            name_tv = (TextView) itemView.findViewById(R.id.name_of_itemorder);
            price_tv = (TextView) itemView.findViewById(R.id.price_of_order);
            quantity_tv = (TextView) itemView.findViewById(R.id.quantity_of_order);
        }
    }
}
