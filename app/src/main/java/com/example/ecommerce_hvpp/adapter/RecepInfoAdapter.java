package com.example.ecommerce_hvpp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.RecepInfo;
import com.example.ecommerce_hvpp.repositories.RecepInfoRepository;
import com.example.ecommerce_hvpp.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class RecepInfoAdapter extends RecyclerView.Adapter<RecepInfoAdapter.DataViewHolder> {
    private Context context;
    private RecepInfoRepository repo = new RecepInfoRepository();
    private UserRepository user_repo = new UserRepository();
    private ArrayList<RecepInfo> listRecepInfo;


    public RecepInfoAdapter(Context context, ArrayList<RecepInfo> listRecepInfo) {
        this.context = context;
        this.listRecepInfo = listRecepInfo;
    }
    public int getItemCount() {
        return listRecepInfo.size();
    }
    @NonNull
    @Override
    public RecepInfoAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recepient_info, parent, false);

        return new RecepInfoAdapter.DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull RecepInfoAdapter.DataViewHolder holder, int position) {
        RecepInfo recepInfo = listRecepInfo.get(position);

        holder.name_tv.setText(recepInfo.getName());
        holder.phonenumber_tv.setText(recepInfo.getPhonenumber());
        holder.address_tv.setText(recepInfo.getAddress());
        if (recepInfo.getisApplied() == true){
            holder.isApplied_image.setImageResource(R.drawable.checked);
            holder.applied_btn.setVisibility(View.INVISIBLE);
        }

        holder.applied_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView name_tv, address_tv, phonenumber_tv;
        private ImageView isApplied_image;
        private Button applied_btn, edit_btn;
        private LinearLayout layout;
        public DataViewHolder(View itemView){
            super(itemView);

            name_tv = (TextView) itemView.findViewById(R.id.name_recep_info);
            address_tv = (TextView) itemView.findViewById(R.id.address_recep_info);
            phonenumber_tv = (TextView) itemView.findViewById(R.id.numberphone_recep_info);
            isApplied_image = (ImageView) itemView.findViewById(R.id.isApplied_image);
            applied_btn = (Button) itemView.findViewById(R.id.apply_btn);
            edit_btn = (Button) itemView.findViewById(R.id.edit_btn);
        }
    }
}
