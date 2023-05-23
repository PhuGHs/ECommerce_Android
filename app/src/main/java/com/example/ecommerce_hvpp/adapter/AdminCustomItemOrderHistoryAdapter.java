package com.example.ecommerce_hvpp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.activities.TestAdminActivity;
import com.example.ecommerce_hvpp.databinding.AdminCustomItemOrderHistoryBinding;
import com.example.ecommerce_hvpp.fragments.AdminOrderHistoryFragment;
import com.example.ecommerce_hvpp.model.Customer;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firestore.v1.StructuredQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminCustomItemOrderHistoryAdapter extends RecyclerView.Adapter<AdminCustomItemOrderHistoryAdapter.AdminCustomItemOrderHistoryViewHolder> {
    Context mContext;
    List<OrderHistory> mListOrderHistory;
    List<OrderHistory> mListOrderHistoryOriginal;
    SimpleDateFormat templateDate;
    AdminProfileRepository repo;


    public AdminCustomItemOrderHistoryAdapter(Context context, List<OrderHistory> listOrderHistory) {
        this.mContext = context;
        this.mListOrderHistory = listOrderHistory;
    }
    @NonNull
    @Override
    public AdminCustomItemOrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdminCustomItemOrderHistoryBinding mAdminCustomItemOrderHistoryBinding =
                AdminCustomItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminCustomItemOrderHistoryViewHolder(mAdminCustomItemOrderHistoryBinding);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull AdminCustomItemOrderHistoryViewHolder holder, int position) {
        OrderHistory orderHistory = mListOrderHistory.get(position);
        if (orderHistory == null) {
            return;
        }

        templateDate = new SimpleDateFormat("dd MMM, yyyy");
        holder.mAdminCustomItemCustomerBinding.adminOrderHistoryComponentIdOrder.setText(String.valueOf(orderHistory.getID()));
        holder.mAdminCustomItemCustomerBinding.adminOrderHistoryComponentDate.setText(templateDate.format(orderHistory.getTimeCreate()));

        // get customer by Id and set data into UI
        repo = new AdminProfileRepository();
        Observable<Resource<Customer>> observable = repo.getObservableCustomerById(String.valueOf(orderHistory.getCustomerID()));
        Observer<Resource<Customer>> observer = getObserverCustomer(holder);

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        holder.mAdminCustomItemCustomerBinding.adminOrderHistoryComponentIconPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCallPhone();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListOrderHistory.size();
    }

    private Observer<Resource<Customer>> getObserverCustomer(@NonNull AdminCustomItemOrderHistoryViewHolder holder) {
        return new Observer<Resource<Customer>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                Log.e("Vucoder", "onSubscribe");
            }

            @Override
            public void onNext(@NonNull Resource<Customer> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        Customer customer = Objects.requireNonNull(resource.data);
                        holder.mAdminCustomItemCustomerBinding.adminOrderHistoryComponentNameCustomer.setText(customer.getName());
                        Glide.with(mContext).load(customer.getImagePath()).into(holder.mAdminCustomItemCustomerBinding.adminOrderHistoryComponentAvatarCustomer);
                        holder.mAdminCustomItemCustomerBinding.adminOrderHistoryComponentPhoneCustomer.setText(customer.getPhone());
                        holder.mAdminCustomItemCustomerBinding.adminOrderHistoryComponentAddressCustomer.setText(customer.getAddress());
                        break;
                    case ERROR:
                        Log.i("VuError", resource.message);
                        break;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                // Handle error state if needed
            }

            @Override
            public void onComplete() {
                // Handle completion if needed
                Log.e("Vucoder", "onComplete");
            }
        };
    }

//    @SuppressLint("NotifyDataSetChanged")
//    public void filterCustomer(String strSearch) {
//        if (strSearch.isEmpty()) {
//            mListOrderHistory = mListOrderHistoryOriginal;
//            notifyDataSetChanged();
//        } else {
//            List<OrderHistory> listOrderHistory = new ArrayList<>();
//            for (OrderHistory orderHistory : mListOrderHistoryOriginal) {
//                Customer customer =
//                if (orderHistory.getCustomerID().toLowerCase().contains(strSearch.toLowerCase()) ||
//                        orderHistory.getEmail().toLowerCase().contains(strSearch.toLowerCase())) {
//                    listOrderHistory.add(orderHistory);
//                }
//            }
//            mListOrderHistory = listOrderHistory;
//            notifyDataSetChanged();
//        }
//    }
    private void makeCallPhone() {
        Log.e("CallPhone", "Hello");
        String phone = "0814321006";
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        mContext.startActivity(intent);
    }

    public static class AdminCustomItemOrderHistoryViewHolder extends RecyclerView.ViewHolder {
        AdminCustomItemOrderHistoryBinding mAdminCustomItemCustomerBinding;

        public AdminCustomItemOrderHistoryViewHolder(@NonNull AdminCustomItemOrderHistoryBinding itemOrderHistoryBinding) {
            super(itemOrderHistoryBinding.getRoot());
            this.mAdminCustomItemCustomerBinding = itemOrderHistoryBinding;
        }
    }
}
