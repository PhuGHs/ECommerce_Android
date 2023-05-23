package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.Feedback;
import com.example.ecommerce_hvpp.model.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FeedbackCustomerAdapter extends RecyclerView.Adapter<FeedbackCustomerAdapter.DataViewHolder> {
    private Context context;
    private ArrayList<Feedback> listFeedback;

    public FeedbackCustomerAdapter(Context context, ArrayList<Feedback> listFeedback) {
        this.context = context;
        this.listFeedback = listFeedback;
    }

    @Override
    public int getItemCount() {
        return listFeedback.size();
    }

    @Override
    public FeedbackCustomerAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_customer_item, parent, false);

        return new FeedbackCustomerAdapter.DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull FeedbackCustomerAdapter.DataViewHolder holder, int position) {
        Feedback feedback = listFeedback.get(position);

        //add customer name later
        holder.ratingBar.setRating((float) feedback.getPoint());
        holder.Comment.setText(feedback.getComment());
        holder.Time.setText(getDateFeedback(feedback.getDate()));
    }

    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView Name, Comment, Time;
        private RatingBar ratingBar;
        public DataViewHolder(View itemView) {
            super(itemView);

            Name = (TextView) itemView.findViewById(R.id.txtCustomerNameFeedback);
            Comment = (TextView) itemView.findViewById(R.id.txtFeedbackContent);
            Time = (TextView) itemView.findViewById(R.id.txtFeedbackTime);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBarDetailItem);
        }
    }
    public String getDateFeedback(long timeStamp){
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(timeStamp);

        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        int Month = calendar.get(Calendar.MONTH);
        int Year = calendar.get(Calendar.YEAR);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedTime = dateFormat.format(new Date(timeStamp));

        return formattedTime;
    }
}
