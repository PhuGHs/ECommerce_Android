package com.example.ecommerce_hvpp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.Inbox;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Inbox> list;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Inbox inboxItem);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public InboxAdapter(List<Inbox> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Inbox ib = list.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvRecipientName.setText(ib.getRecipientName());
        viewHolder.tvLastMessage.setText(ib.getLastMessage());
        viewHolder.siImage.setImageResource(R.drawable.profile);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView siImage;
        private TextView tvRecipientName, tvLastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            siImage = itemView.findViewById(R.id.profileImageInChatItem);
            tvRecipientName = itemView.findViewById(R.id.tvRecipientName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            itemView.setOnClickListener(v -> {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(list.get(getAdapterPosition()));
                }
            });
        }
    }
}
