package com.example.ecommerce_hvpp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.ecommerce_hvpp.model.Feedback;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProfileViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReviewFeedBackAdapter extends RecyclerView.Adapter<ReviewFeedBackAdapter.DataViewHolder>  {
    private ReviewedFeedbackFragment parent;
    private ArrayList<Feedback> listReviewedFeedback;
    private ProfileViewModel viewModel;
    public ReviewFeedBackAdapter(ReviewedFeedbackFragment parent, ArrayList<Feedback> listReviewedFeedback){
        this.parent = parent;
        this.listReviewedFeedback = listReviewedFeedback;
    }
    public int getItemCount() {
        return listReviewedFeedback.size();
    }
    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_reviewed, parent, false);

        return new DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Feedback feedback = listReviewedFeedback.get(position);
        viewModel = new ViewModelProvider(parent).get(ProfileViewModel.class);

        holder.datetime_feedback_tv.setText(getDate(feedback.getDate()));
        holder.comment_tv.setText(feedback.getComment());
        holder.ratingBar.setRating(feedback.getPoint());
        viewModel.getUserName().observe(parent, UserName -> {
            holder.name_user_tv.setText(UserName);
        });
        viewModel.getUserImage().observe(parent, UserImage -> {
            Glide.with(holder.itemView).load(UserImage).into(holder.image_user_feedback);
        });

        MainActivity.PDviewModel.getDetailProduct(feedback.getProductID()).observe(parent, product -> {
            holder.name_feedback_item.setText(product.getName());
            Glide.with(holder.itemView).load(product.getUrlmain()).fitCenter().into(holder.image_feedback_item);
        });
    }

    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView name_user_tv, datetime_feedback_tv, comment_tv, name_feedback_item;
        private ImageView image_user_feedback, image_feedback_item;
        private RatingBar ratingBar;
        public DataViewHolder(View itemView){
            super(itemView);

            name_user_tv = (TextView) itemView.findViewById(R.id.name_user_feedback);
            datetime_feedback_tv = (TextView) itemView.findViewById(R.id.datetime_feedback);
            comment_tv = (TextView) itemView.findViewById(R.id.detail_feedback);
            name_feedback_item = (TextView) itemView.findViewById(R.id.name_of_feedbackitem);

            image_user_feedback = (ImageView) itemView.findViewById(R.id.image_of_user_feedback);
            image_feedback_item = (ImageView) itemView.findViewById(R.id.image_of_feedbackitem);

            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar_of_feedback);
        }
    }
    public String getDate(long timeStamp){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault());
        String formattedTime = dateFormat.format(new Date(timeStamp));

        return formattedTime;
    }
}
