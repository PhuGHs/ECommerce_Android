package com.example.ecommerce_hvpp.adapter;


import static com.example.ecommerce_hvpp.util.CustomFormat.templateDate;

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
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminCustomItemOrderHistoryBinding;
import com.example.ecommerce_hvpp.fragments.admin_fragments.AdminOrderHistoryFragment;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.util.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    AdminProfileRepository repo;
    AdminOrderHistoryFragment parent;


    public AdminCustomItemOrderHistoryAdapter(Context context, List<OrderHistory> listOrderHistory, AdminOrderHistoryFragment parent) {
        this.mContext = context;
        this.mListOrderHistory = listOrderHistory;
        this.mListOrderHistoryOriginal = listOrderHistory;
        this.parent = parent;
    }
    @NonNull
    @Override
    public AdminCustomItemOrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdminCustomItemOrderHistoryBinding mAdminCustomItemOrderHistoryBinding =
                AdminCustomItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminCustomItemOrderHistoryViewHolder(mAdminCustomItemOrderHistoryBinding);
    }

    @SuppressLint({"SimpleDateFormat", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull AdminCustomItemOrderHistoryViewHolder holder, int position) {
        OrderHistory orderHistory = mListOrderHistory.get(position);

        if (orderHistory == null) {
            return;
        }

        holder.mAdminCustomItemOrderHistoryBinding.adminOrderHistoryComponentIdOrder.setText(orderHistory.getId());
        holder.mAdminCustomItemOrderHistoryBinding.adminOrderHistoryComponentDate.setText(templateDate.format(orderHistory.getCreatedDate()));
        holder.mAdminCustomItemOrderHistoryBinding.adminOrderHistoryComponentNameCustomer.setText(orderHistory.getRecipientName());
        holder.mAdminCustomItemOrderHistoryBinding.adminOrderHistoryComponentPhoneCustomer.setText(orderHistory.getPhoneNumber());
        holder.mAdminCustomItemOrderHistoryBinding.adminOrderHistoryComponentAddressCustomer.setText(orderHistory.getAddress());

//         get user by Id and set data into UI
        repo = new AdminProfileRepository();
        Observable<Resource<User>> observable = repo.getObservableCustomerById(String.valueOf(orderHistory.getCustomerId()));
        Observer<Resource<User>> observer = getObserverUser(holder, orderHistory);

//        // add customer into order history
        mListOrderHistory.set(position, orderHistory);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        holder.mAdminCustomItemOrderHistoryBinding.adminOrderHistoryComponentIconPhone.setOnClickListener(new View.OnClickListener() {
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

    @SuppressLint("NotifyDataSetChanged")
    public void setTypeAdapter(String type, String order) {
        mListOrderHistory.clear();
        mListOrderHistory.addAll(mListOrderHistoryOriginal);

//        if (!type.equals("All")) {
//            typeList = new ArrayList<>();
//            for (Order or : list) {
//                if (or.getStatus().equals(type)) {
//                    typeList.add(or);
//                }
//            }
//            list.clear();
//            list.addAll(typeList);
//        }

        Collections.sort(mListOrderHistory, new Comparator<OrderHistory>() {
            @Override
            public int compare(OrderHistory order, OrderHistory t1) {
                String selectedSortOption = parent.getFilterOptions().get(0);
                return Long.compare(t1.getCreatedDate().getTime(), order.getCreatedDate().getTime());
            }
        });

        notifyDataSetChanged();
    }

    private Observer<Resource<User>> getObserverUser(@NonNull AdminCustomItemOrderHistoryViewHolder holder, OrderHistory orderHistory) {
        return new Observer<Resource<User>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                Log.e("Vucoder", "onSubscribe");
            }

            @Override
            public void onNext(@NonNull Resource<User> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        User user = Objects.requireNonNull(resource.data);
                        orderHistory.setUser(user);
                        if (user.getImagePath() == null || user.getImagePath().equals("")) {
                            Glide
                                    .with(mContext)
                                    .load(R.drawable.baseline_no_avatar)
                                    .into(holder.mAdminCustomItemOrderHistoryBinding.adminOrderHistoryComponentAvatarCustomer);
                        } else {
                            // null
                            Glide
                                    .with(mContext)
                                    .load(user.getImagePath())
                                    .into(holder.mAdminCustomItemOrderHistoryBinding.adminOrderHistoryComponentAvatarCustomer);
                        }
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

    @SuppressLint("NotifyDataSetChanged")
    public void filterOrderHistory(String strSearch) {
        if (strSearch.isEmpty()) {
            mListOrderHistory = mListOrderHistoryOriginal;
            notifyDataSetChanged();
        } else {
            List<OrderHistory> listOrderHistory = new ArrayList<>();
            for (OrderHistory orderHistory : mListOrderHistoryOriginal) {
                if (String.valueOf(orderHistory.getId()).toLowerCase().contains(strSearch.toLowerCase()) ||
                        orderHistory.getRecipientName().toLowerCase().contains(strSearch.toLowerCase()) ||
                        orderHistory.getPhoneNumber().toLowerCase().contains(strSearch.toLowerCase()) ||
                        orderHistory.getAddress().toLowerCase().contains(strSearch.toLowerCase())) {
                    listOrderHistory.add(orderHistory);
                }
            }
            mListOrderHistory = listOrderHistory;
            notifyDataSetChanged();
        }
    }

    private void makeCallPhone() {
        Log.e("CallPhone", "Hello");
        String phone = "0814321006";
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        mContext.startActivity(intent);
    }

    public static class AdminCustomItemOrderHistoryViewHolder extends RecyclerView.ViewHolder {
        AdminCustomItemOrderHistoryBinding mAdminCustomItemOrderHistoryBinding;

        public AdminCustomItemOrderHistoryViewHolder(@NonNull AdminCustomItemOrderHistoryBinding itemOrderHistoryBinding) {
            super(itemOrderHistoryBinding.getRoot());
            this.mAdminCustomItemOrderHistoryBinding = itemOrderHistoryBinding;
        }
    }
}
