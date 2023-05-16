package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class AdminProductImageSlider extends SliderViewAdapter<AdminProductImageSlider.ProductImageSliderViewHolder> {

    private Context context;
    private List<String> list;
    @Override
    public ProductImageSliderViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(ProductImageSliderViewHolder viewHolder, int position) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    class ProductImageSliderViewHolder extends SliderViewAdapter.ViewHolder {

        public ProductImageSliderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
