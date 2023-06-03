package com.example.ecommerce_hvpp.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.fragments.admin_fragments.AdminOrderedListFragment;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.util.CurrencyFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AdminOrderManagementAdapter extends RecyclerView.Adapter<AdminOrderManagementAdapter.OrderManagementViewHolder> implements Filterable {
    private List<Order> list;
    private List<Order> backUpList;
    private List<Order> typeList;
    private AdminOrderedListFragment parent;

    public AdminOrderManagementAdapter(List<Order> list, AdminOrderedListFragment parent) {
        this.list = list;
        this.parent = parent;
        backUpList = new ArrayList<>(list);
    }

    public void setBackUpList(List<Order> list) {
        backUpList.addAll(list);
    }

    @Override
    public OrderManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == R.layout.item_empty_view) {
            View emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false);
            return new OrderManagementViewHolder(emptyView, true);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_admin, parent, false);
            return new OrderManagementViewHolder(view, false);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull OrderManagementViewHolder holder, int position) {
        if(holder.isEmptyView) {

        } else {
            Order order = list.get(position);
            holder.bind(order, "ORDER#" + String.valueOf(position));
            holder.itemView.setOnClickListener(i -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("orderInfo", order);
                parent.getNavController().navigate(R.id.navigate_to_orderdetails, bundle);
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? 1 : list.size();
    }
    public int getListSize() {
        return list.size();
    }

    private Filter orderFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Order> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(backUpList);
            } else {
                String filterPattern = charSequence.toString();
                for(Order order : backUpList) {
                    if(matchesFilter(order, filterPattern)) {
                        filteredList.add(order);
                    }
                }
            }
            FilterResults results = new FilterResults();
            Collections.sort(filteredList, new Comparator<Order>() {
                @Override
                public int compare(Order order, Order t1) {
                    String selectedSortOption = parent.getFilterOptions().get(0);
                    if (selectedSortOption.equals("Price")) {
                        return Double.compare(order.getTotalPrice(), t1.getTotalPrice());
                    } else {
                        return Long.compare(order.getCreatedDate(), t1.getCreatedDate());
                    }
                }
            });
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            List<Order> filteredList = (List<Order>) filterResults.values;

            if (charSequence == null || charSequence.length() == 0) {
                list.clear();
                list.addAll(backUpList);
            } else {
                list.clear();
                list.addAll(filteredList);
            }

            if(list.size() > 1) {
                parent.getTvFoundText().setText("Found " + list.size() + " results");
            } else if (list.size() == 0) {
                parent.getTvFoundText().setText("");
            } else {
                parent.getTvFoundText().setText("Found " + list.size() + " result");
            }

            notifyDataSetChanged();
        }
    };
    @Override
    public Filter getFilter() {
        return orderFilter;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTypeAdapter(String type, String order) {
        list.clear();
        list.addAll(backUpList);

        if (!type.equals("All")) {
            typeList = new ArrayList<>();
            for (Order or : list) {
                if (or.getStatus().equals(type)) {
                    typeList.add(or);
                }
            }
            list.clear();
            list.addAll(typeList);
        }

        Collections.sort(list, new Comparator<Order>() {
            @Override
            public int compare(Order order, Order t1) {
                String selectedSortOption = parent.getFilterOptions().get(0);
                if (selectedSortOption.equals("Price")) {
                    return Double.compare(t1.getTotalPrice(), order.getTotalPrice());
                } else {
                    return Long.compare(t1.getCreatedDate(), order.getCreatedDate());
                }
            }
        });

        notifyDataSetChanged();
    }

    private boolean matchesFilter(Order order, String filterPattern) {
        String selectedFilterOption = parent.getFilterOptions().get(2);
        switch (selectedFilterOption) {
            case "Phone Number":
                return order.getPhone_number().contains(filterPattern);
            case "Recipient Name":
                return order.getRecipientName().toLowerCase().trim().contains(filterPattern);
            default:
                return order.getId().toLowerCase().trim().contains(filterPattern);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.isEmpty()) {
            return R.layout.item_empty_view;
        } else {
            return R.layout.item_order_admin;
        }
    }

    public class OrderManagementViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderId, tvRecipientName, tvAddress, tvPrice, tvOrderDate, tvOrderStatus;

        private boolean isEmptyView;
        public OrderManagementViewHolder(@NonNull View itemView, boolean isEmptyView) {
            super(itemView);
            this.isEmptyView = isEmptyView;
            if(isEmptyView) {

            } else {
                tvOrderId = itemView.findViewById(R.id.tvOrderId);
                tvRecipientName = itemView.findViewById(R.id.tvRecipientName);
                tvAddress = itemView.findViewById(R.id.tvAddress);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
                tvOrderStatus = itemView.findViewById(R.id.tvStatus);
            }
        }

        public void bind(Order order, String sequenceNumber) {
            if(isEmptyView) {

            } else {
                tvOrderId.setText(sequenceNumber);
                tvRecipientName.setText(order.getRecipientName());
                tvAddress.setText(order.getAddress());
                tvPrice.setText(CurrencyFormat.getVNDCurrency(order.getTotalPrice()));
                tvOrderDate.setText(formatDate(order.getCreatedDate()));
                if(Objects.equals(order.getStatus(), "Pending")) {
                    tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.pending));
                } else if (Objects.equals(order.getStatus(), "Packaged")) {
                    tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.packaged));
                } else if (Objects.equals(order.getStatus(), "Confirmed")) {
                    tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.confirmed));
                } else if (Objects.equals(order.getStatus(), "Delivered")) {
                    tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.delivered));
                }
                tvOrderStatus.setText(order.getStatus().toString());
            }
        }

        private String formatDate(long timeStamp) {
            Date date = new Date(timeStamp);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            return sdf.format(date);
        }
    }
}
