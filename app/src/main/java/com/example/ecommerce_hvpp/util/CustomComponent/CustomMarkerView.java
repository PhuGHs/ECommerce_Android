package com.example.ecommerce_hvpp.util.CustomComponent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ecommerce_hvpp.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

@SuppressLint("ViewConstructor")
public class CustomMarkerView extends MarkerView {
    TextView tvContent;
    public CustomMarkerView (Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.admin_custom_marker_quantity);
        tvContent.setText("Vu");
    }


    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        String hello = "Hello";
        tvContent.setText(hello);
        tvContent.setTextColor((int)R.color.white);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }

}
