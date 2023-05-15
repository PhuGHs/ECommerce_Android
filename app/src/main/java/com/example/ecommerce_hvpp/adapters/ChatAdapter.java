package com.example.ecommerce_hvpp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatMessage> chatMessages;
    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatViewHolder chatHolder = (ChatViewHolder)holder;
        ChatMessage message = chatMessages.get(position);
        if(message.getType() == ChatMessage.TYPE_OUTGOING) {
            chatHolder.outgoingLayout.setVisibility(View.VISIBLE);
            chatHolder.incomingLayout.setVisibility(View.GONE);
            chatHolder.tvOutgoingMessage.setText(message.getMessageText());
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateString = sfd.format(new Date(message.getSendingTime()));
            chatHolder.tvOutgoingMessageTime.setText(dateString);
        } else {
            chatHolder.outgoingLayout.setVisibility(View.GONE);
            chatHolder.incomingLayout.setVisibility(View.VISIBLE);
            chatHolder.tvIncomingMessage.setText(message.getMessageText());
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateString = sfd.format(new Date(message.getSendingTime()));
            chatHolder.tvIncomingMessageTime.setText(dateString);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages == null ? 0 : chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chatMessages.get(position).getType();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIncomingMessage, tvOutgoingMessage;
        private TextView tvIncomingMessageTime, tvOutgoingMessageTime;
        private LinearLayout incomingLayout, outgoingLayout;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            incomingLayout = itemView.findViewById(R.id.incoming_layout);
            outgoingLayout = itemView.findViewById(R.id.outgoing_layout);
            tvIncomingMessage = itemView.findViewById(R.id.incoming_message);
            tvOutgoingMessage = itemView.findViewById(R.id.outgoing_message);
            tvIncomingMessageTime = itemView.findViewById(R.id.incoming_message_time);
            tvOutgoingMessageTime = itemView.findViewById(R.id.outgoing_message_time);
        }
    }
}


