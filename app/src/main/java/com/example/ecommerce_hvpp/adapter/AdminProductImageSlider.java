package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.ItemModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminProductImageSlider extends SliderViewAdapter<AdminProductImageSlider.ProductImageSliderViewHolder> {

    private Context context;
    private List<ItemModel> list = new ArrayList<>();

    public AdminProductImageSlider(Context context) {
        this.context = context;
    }

    public void renewItems(List<ItemModel> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    public List<ItemModel> getList() {
        return list;
    }

    public void deleteItem(int position) {
        this.list.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(ItemModel item) {
        this.list.add(item);
        notifyDataSetChanged();
    }
    @Override
    public ProductImageSliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider_product_admin, parent, false);
        return new ProductImageSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductImageSliderViewHolder viewHolder, int position) {
        ItemModel itemModel = list.get(position);

        if(itemModel.getLink() == null) {
            Glide.with(viewHolder.itemView)
                    .load(itemModel.getImageUri())
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);
        } else {
            Glide.with(viewHolder.itemView)
                    .load(itemModel.getLink())
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    class ProductImageSliderViewHolder extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;
        public ProductImageSliderViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
