package com.example.ecommerce_hvpp.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.fragments.customer_fragments.ReviewedFeedbackFragment;
import com.example.ecommerce_hvpp.fragments.customer_fragments.UnreviewedFeedbackFragment;
import com.example.ecommerce_hvpp.model.Feedback;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProfileViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UnreviewFeedBackAdapter extends RecyclerView.Adapter<UnreviewFeedBackAdapter.DataViewHolder>{
    private UnreviewedFeedbackFragment parent;
    private ArrayList<OrderHistorySubItem> unreviewList;
    public UnreviewFeedBackAdapter(UnreviewedFeedbackFragment parent, ArrayList<OrderHistorySubItem> unreviewList){
        this.parent = parent;
        this.unreviewList = unreviewList;
    }
    public int getItemCount() {
        return unreviewList.size();
    }
    @NonNull
    @Override
    public UnreviewFeedBackAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_unreviewed, parent, false);

        return new UnreviewFeedBackAdapter.DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull UnreviewFeedBackAdapter.DataViewHolder holder, int position) {
        OrderHistorySubItem item = unreviewList.get(position);

        holder.name_item_tv.setText(item.getName_subItem());
        holder.quantity_item_tv.setText(item.getQuantity_subItem() + " products");
        holder.sum_price_tv.setText(Double.toString(item.getSum_subItem()));
        Glide.with(holder.itemView).load(item.getImagePath_subItem()).fitCenter().into(holder.image_unreviewedfeedback_item);

        holder.review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("productId", item.getProductID());
                parent.getNavController().navigate(R.id.navigate_to_feedback_unreview, bundle);
            }
        });

    }
    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView name_item_tv, quantity_item_tv, sum_price_tv, day_purchase_tv;
        private ImageView image_unreviewedfeedback_item;
        private Button review_btn;
        public DataViewHolder(View itemView){
            super(itemView);

            name_item_tv = (TextView) itemView.findViewById(R.id.name_of_unreviewed_feeedback);
            quantity_item_tv = (TextView) itemView.findViewById(R.id.quantity_of_unreviewed_feedback);
            sum_price_tv = (TextView) itemView.findViewById(R.id.sum_of_unreviewed_feedback);
            day_purchase_tv = (TextView) itemView.findViewById(R.id.date_of_unreviewed_feedback);

            image_unreviewedfeedback_item = (ImageView) itemView.findViewById(R.id.image_of_unreviewed_feedback);
            review_btn = (Button) itemView.findViewById(R.id.review_btn_unreviewed_feedback);
        }
    }
    public String getDate(long timeStamp){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedTime = dateFormat.format(new Date(timeStamp));

        return formattedTime;
    }
}
