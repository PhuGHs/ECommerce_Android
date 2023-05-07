package com.example.ecommerce_hvpp.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.fragments.ChatRoomFragment;
import com.example.ecommerce_hvpp.model.ChatRoom;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatRoom> list;
    private ChatRoomFragment parent;

    public ChatRoomAdapter(List<ChatRoom> list, ChatRoomFragment parent) {
        this.list = list;
        this.parent = parent;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatRoom ib = list.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvRecipientName.setText(ib.getRecipientName());
        viewHolder.tvLastMessage.setText(ib.getLastMessage());
        viewHolder.siImage.setImageResource(R.drawable.profile);

        viewHolder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("roomId", ib.getChatRoomId());
            bundle.putString("senderId", ib.getSenderId());
            bundle.putString("recipientId", ib.getRecipientName());
            parent.getNavController().navigate(R.id.navigate_to_chatDetail, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ShapeableImageView siImage;
        private TextView tvRecipientName, tvLastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            siImage = itemView.findViewById(R.id.profileImageInChatItem);
            tvRecipientName = itemView.findViewById(R.id.tvRecipientName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
        }
    }
}
