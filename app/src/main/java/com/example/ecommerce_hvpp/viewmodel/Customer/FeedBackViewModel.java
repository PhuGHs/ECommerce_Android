package com.example.ecommerce_hvpp.viewmodel.Customer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.Feedback;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.example.ecommerce_hvpp.repositories.customerRepositories.FeedBackRepository;
import com.example.ecommerce_hvpp.repositories.customerRepositories.OrderRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FeedBackViewModel extends ViewModel {
    private FeedBackRepository repo = new FeedBackRepository();
    private OrderRepository unreviewed_repo = new OrderRepository();
    private FirebaseAuth firebaseAuth;
    public LiveData<List<Feedback>> showReviewedFeedback(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getListReviewedFeedback(fbUser.getUid());
    }
    public LiveData<List<OrderHistorySubItem>> showUnreviewedFeedback(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getOrderId(fbUser.getUid());
    }
}
