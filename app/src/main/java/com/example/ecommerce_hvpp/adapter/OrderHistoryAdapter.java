package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.OrderHistoryItem;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.example.ecommerce_hvpp.model.Voucher;
import com.example.ecommerce_hvpp.viewmodel.OrderHistoryViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.DataViewHolder>{
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private Context context;
    private ArrayList<OrderHistoryItem> itemList;
    private OrderHistoryViewModel viewModel;
    private OrderHistorySubAdapter adapter;
    private RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistoryItem> listOrderHistory) {
        this.context = context;
        this.itemList = listOrderHistory;
    }
    public int getItemCount() {
        return itemList.size();
    }
    @NonNull
    @Override
    public OrderHistoryAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_order, parent, false);

        return new OrderHistoryAdapter.DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.DataViewHolder holder, int position) {
        OrderHistoryItem item = itemList.get(position);

        holder.quantity_tv.setText(Long.toString(item.getQuantity_of_product()));
        holder.day_of_order_tv.setText(getDate(item.getDayCreate_subItem()));
        holder.sum_of_order_tv.setText(item.getSum_of_order());

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getSubItemList().size());

    }
    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView quantity_tv, day_of_order_tv, sum_of_order_tv;
        private RecyclerView recyclerView;
        public DataViewHolder(View itemView){
            super(itemView);

            quantity_tv = (TextView) itemView.findViewById(R.id.quantity_of_ordereditem_tiengviet);
            day_of_order_tv = (TextView) itemView.findViewById(R.id.day_of_order);
            sum_of_order_tv = (TextView) itemView.findViewById(R.id.sum_of_ordereditem_tiengviet);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.list_order_item);
        }
    }
    public String getDate(long timeStamp){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedTime = dateFormat.format(new Date(timeStamp));

        return formattedTime;
    }
}
