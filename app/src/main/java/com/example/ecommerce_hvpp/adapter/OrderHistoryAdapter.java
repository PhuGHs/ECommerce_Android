package com.example.ecommerce_hvpp.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.OrderHistoryItem;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.example.ecommerce_hvpp.model.Voucher;
import com.example.ecommerce_hvpp.viewmodel.OrderHistoryViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.DataViewHolder>{
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private Context context;
    private ArrayList<OrderHistoryItem> itemList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private OrderHistorySubItem subitem = new OrderHistorySubItem();
    private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();
    private OrderHistoryViewModel viewModel;
    private OrderHistorySubAdapter adapter;
    private RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistoryItem> listOrderHistory) {
        this.context = context;
        this.itemList = listOrderHistory;
    }
    public int getItemCount() {
        return itemList.size();
    }
    @NonNull
    @Override
    public OrderHistoryAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_order, parent, false);

        return new OrderHistoryAdapter.DataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.DataViewHolder holder, int position) {
        OrderHistoryItem item = itemList.get(position);

        holder.quantity_tv.setText(Long.toString(item.getQuantity_of_product()));
        holder.day_of_order_tv.setText(getDate(item.getDayCreate_subItem()));
        holder.sum_of_order_tv.setText(item.getSum_of_order());

        subitem = getFirst_Item();
        Glide.with(holder.itemView.getContext()).load(subitem.getImagePath_subItem()).fitCenter().into(holder.image_item);
        holder.name_item_tv.setText(subitem.getName_subItem());
        holder.quantity_item_tv.setText(subitem.getQuantity_subItem());
        holder.price_item_tv.setText(Double.toString(subitem.getSum_subItem()));

    }
    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView quantity_tv, day_of_order_tv, sum_of_order_tv;
        private ImageView image_item;
        private TextView name_item_tv, quantity_item_tv, price_item_tv;
        public DataViewHolder(View itemView){
            super(itemView);

            quantity_tv = (TextView) itemView.findViewById(R.id.quantity_of_ordereditem_tiengviet);
            day_of_order_tv = (TextView) itemView.findViewById(R.id.day_of_order);
            sum_of_order_tv = (TextView) itemView.findViewById(R.id.sum_of_ordereditem_tiengviet);

            image_item = (ImageView) itemView.findViewById(R.id.image_of_1st_item_orderhistory);
            name_item_tv = (TextView) itemView.findViewById(R.id.name_of_ordereditem);
            quantity_item_tv = (TextView) itemView.findViewById(R.id.quantity_of_ordereditem);
            price_item_tv = (TextView) itemView.findViewById(R.id.total_money_ordereditem);

        }
    }
    public String getDate(long timeStamp){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedTime = dateFormat.format(new Date(timeStamp));

        return formattedTime;
    }
    public OrderHistorySubItem getFirst_Item(){
        FirebaseUser fbUser = mAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        firebaseHelper.getCollection("Order").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getString("customer_id").equals(fbUser.getUid())){
                            db.collection("Order").document(snapshot.getId()).collection("products")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    String image_path = document.getString("image");
                                                    String name = document.getString("name");
                                                    long price = document.getLong("price");
                                                    String quantity = document.getString("quantity");
                                                    subitem = new OrderHistorySubItem(image_path, name, quantity, price);
                                                    Log.d(TAG,  "Lay 1 san pham thanh cong ");
                                                }

                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                            break;
                        }
                    }
                })
                .addOnFailureListener(e -> {

                });
        return subitem;
    }
}
