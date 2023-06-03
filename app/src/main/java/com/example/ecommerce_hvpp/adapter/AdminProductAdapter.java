package com.example.ecommerce_hvpp.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.fragments.admin_fragments.AdminProductListFragment;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.util.CurrencyFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AdminProductAdapter extends RecyclerView.Adapter implements Filterable {
    private List<Product> list;
    private List<Product> original;
    private List<Product> typeList;
    private AdminProductListFragment parent;
    private OnLongItemClickListener mOnLongItemClickListener;

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        mOnLongItemClickListener = onLongItemClickListener;
    }

    public interface OnLongItemClickListener {
        void itemLongClicked(View v, int position);
    }

    public AdminProductAdapter(AdminProductListFragment parent, List<Product> list) {
        this.list = list;
        original = new ArrayList<>(list);
        this.parent = parent;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_admin, parent, false);
        return new AdminProductAdapter.AdminProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdminProductViewHolder viewHolder = (AdminProductViewHolder) holder;
        viewHolder.bind(list.get(position));
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mOnLongItemClickListener != null) {
                    mOnLongItemClickListener.itemLongClicked(view, position);
                }
                return true;
            }});
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? 0 : list.size();
    }

    public int getListSize() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return orderFilter;
    }
    private Filter orderFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Product> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(original);
            } else {
                String filterPattern = charSequence.toString();
                for(Product pd : original) {
                    if(matchesFilter(pd, filterPattern)) {
                        filteredList.add(pd);
                    }
                }
            }
            FilterResults results = new FilterResults();
            Collections.sort(filteredList, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    String selectedSortOption = parent.getFilterOptions().get(0);
                    if (selectedSortOption.equals("Price")) {
                        return Double.compare(t1.getPrice(), product.getPrice());
                    } else {
                        return Long.compare(t1.getTimeAdded(), product.getTimeAdded());
                    }
                }
            });
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            List<Product> filteredList = (List<Product>) filterResults.values;
            if (filteredList != null) {
                list.clear();
                list.addAll(filteredList);
                notifyDataSetChanged();
            }
        }
    };

    @SuppressLint("NotifyDataSetChanged")
    public void setTypeAdapter(String type, String order) {
        list.clear();
        list.addAll(original);

        if (!type.equals("All")) {
            typeList = new ArrayList<>();
            if(type.equals("Nation")) {
                for (Product or : list) {
                    if (!or.getNation().equals("")) {
                        typeList.add(or);
                    }
                }
            } else if (type.equals("Club")) {
                for (Product or : list) {
                    if (!or.getClub().equals("")) {
                        typeList.add(or);
                    }
                }
            }

            list.clear();
            list.addAll(typeList);
        }

        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product product, Product t1) {
                String selectedSortOption = parent.getFilterOptions().get(0);
                if (selectedSortOption.equals("Price")) {
                    return Double.compare(t1.getPrice(), product.getPrice());
                } else {
                    return Long.compare(t1.getTimeAdded(), product.getTimeAdded());
                }
            }
        });

        notifyDataSetChanged();
    }
    public void setOriginalList(List<Product> list) {
        original.addAll(list);
    }

    private boolean matchesFilter(Product pd, String filterPattern) {
        String selectedFilterOption = parent.getFilterOptions().get(2);
        Log.i("type", selectedFilterOption);
        switch (selectedFilterOption) {
            case "Product Name Option":
                return pd.getName().toLowerCase().trim().contains(filterPattern);
            case "Nation Option":
                if(Objects.equals(pd.getNation(), "")) return false;
                return pd.getNation().toLowerCase().trim().contains(filterPattern);
            default:
                if(Objects.equals(pd.getClub(), "")) return false;
                return pd.getClub().toLowerCase().trim().contains(filterPattern);
        }
    }

    public class AdminProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivProductImage;
        private TextView tvProductName, tvPrice;
        public AdminProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
        public void bind(Product pd) {
            Glide.with(itemView)
                    .load(pd.getUrlthumb())
                    .fitCenter()
                    .into(ivProductImage);
            tvProductName.setText(pd.getName());
            tvPrice.setText(CurrencyFormat.getVNDCurrency(pd.getPrice()));
        }
    }
}
