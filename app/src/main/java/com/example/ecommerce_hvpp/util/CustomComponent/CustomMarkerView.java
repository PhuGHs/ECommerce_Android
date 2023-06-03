package com.example.ecommerce_hvpp.util.CustomComponent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ecommerce_hvpp.R;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

@SuppressLint("ViewConstructor")
public class CustomMarkerView extends MarkerView {
    TextView markerText;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        markerText = findViewById(R.id.admin_custom_marker_quantity);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        markerText.setText(e.getY() + "");
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f), -getHeight());
    }
}
