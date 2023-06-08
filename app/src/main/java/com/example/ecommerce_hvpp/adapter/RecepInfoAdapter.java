package com.example.ecommerce_hvpp.adapter;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.fragments.customer_fragments.RecepientInfoFragment;
import com.example.ecommerce_hvpp.model.RecepInfo;
import com.example.ecommerce_hvpp.repositories.customerRepositories.RecepInfoRepository;
import com.example.ecommerce_hvpp.repositories.customerRepositories.UserRepository;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.viewmodel.Customer.RecepInfoViewModel;

import java.util.ArrayList;

public class RecepInfoAdapter extends RecyclerView.Adapter<RecepInfoAdapter.DataViewHolder> {
    private RecepInfoViewModel viewModel;
    private ArrayList<RecepInfo> listRecepInfo;
    private RecepientInfoFragment parent;
    private int tickPos = -1;
    public RecepInfoAdapter(RecepientInfoFragment parent, ArrayList<RecepInfo> listRecepInfo) {
        this.parent = parent;
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

        viewModel = new ViewModelProvider(parent).get(RecepInfoViewModel.class);

        holder.name_tv.setText(recepInfo.getName());
        holder.phonenumber_tv.setText(recepInfo.getPhonenumber());
        holder.address_tv.setText(recepInfo.getAddress());

        if (recepInfo.getisApplied() == true){
            tickPos = holder.getAdapterPosition();
            holder.isApplied_image.setImageResource(R.drawable.checked);
            holder.applied_btn.setVisibility(View.INVISIBLE);
        }
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                listRecepInfo.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                viewModel.deleteRecepInfo(recepInfo.getAddress());
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Delete this recipient's information?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        holder.applied_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tickPos != -1){
                    RecepInfo recep = listRecepInfo.get(tickPos);
                    assert recep != null;
                    recep.setApplied(false);
                    listRecepInfo.set(tickPos, recep);
                    Log.d(TAG, "Current tick: " + tickPos);
                    notifyItemChanged(tickPos);
                }


                tickPos = holder.getAdapterPosition();
                RecepInfo recep1 = listRecepInfo.get(tickPos);
                recep1.setApplied(true);
                listRecepInfo.set(tickPos, recep1);
                Log.d(TAG, "New current tick: " + tickPos);
                notifyItemChanged(tickPos);

                viewModel.updateStatusRecepDetail(recepInfo.getRecep_ID());
            }
        });
        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("recep_id", recepInfo.getRecep_ID());
                parent.getNavController().navigate(R.id.navigate_to_editrecepinfo, bundle);
            }
        });

    }
//    public int getCurrentTick(){
//        for (RecepInfo recepInfo : listRecepInfo){
//            if (recepInfo.getisApplied()){
//                return listRecepInfo.indexOf(recepInfo);
//            }
//        }
//        return -1;
//    }
    public RecepInfo getCurrentTick(){
        for (RecepInfo recepInfo : listRecepInfo){
            if (recepInfo.getisApplied() == true){
                return recepInfo;
            }
        }
        return null;
    }
    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView name_tv, address_tv, phonenumber_tv;
        private ImageView isApplied_image;
        private Button applied_btn, edit_btn;
        private AppCompatImageButton delete_btn;
        public DataViewHolder(View itemView){
            super(itemView);

            name_tv = (TextView) itemView.findViewById(R.id.name_recep_info);
            address_tv = (TextView) itemView.findViewById(R.id.address_recep_info);
            phonenumber_tv = (TextView) itemView.findViewById(R.id.numberphone_recep_info);
            isApplied_image = (ImageView) itemView.findViewById(R.id.isApplied_image);
            applied_btn = (Button) itemView.findViewById(R.id.apply_btn);
            edit_btn = (Button) itemView.findViewById(R.id.edit_btn);
            delete_btn = (AppCompatImageButton) itemView.findViewById(R.id.delete_btn);

        }

    }
}
