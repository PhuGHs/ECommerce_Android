package com.example.ecommerce_hvpp.adapter;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatRoom> list;
    private ChatRoomFragment parent;
    private String senderId;
    private String recipientId;
    private String roomName;
    private String lastMessage = "";

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

        //decide who is recipient, who is sender
        if(Objects.equals(ib.getUser1Id(), parent.getCurrentUserUID())) {
            senderId =  ib.getUser1Id();
            recipientId = ib.getUser2Id();
        } else {
            senderId = ib.getUser2Id();
            recipientId = ib.getUser1Id();
        }

        if(!Objects.equals(parent.getCurrentUserUID(), ib.getRecipientId())) {
            lastMessage = "You: " + ib.getLastMessage();
        } else {
            lastMessage = ib.getLastMessage();
        }

        // assign value
        parent.getChatRoomName(recipientId);
        roomName = parent.getRoomName();
        viewHolder.tvRecipientName.setText(roomName);
        viewHolder.tvLastMessage.setText(lastMessage);
        viewHolder.siImage.setImageResource(R.drawable.profile);
        viewHolder.tvLastMessageTimestamp.setText(getLastMessageTimeStampFormat(ib.getLastMessageTimeStamp()));


        // implement button click and pass value to new fragment through navController bundle
        viewHolder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("roomId", ib.getChatRoomId());
            bundle.putString("roomName", roomName);
            bundle.putString("senderId", senderId);
            bundle.putString("recipientId", recipientId);
            parent.getNavController().navigate(R.id.navigate_to_chatDetail, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ShapeableImageView siImage;
        private TextView tvRecipientName, tvLastMessage, tvLastMessageTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            siImage = itemView.findViewById(R.id.profileImageInChatItem);
            tvRecipientName = itemView.findViewById(R.id.tvRecipientName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvLastMessageTimestamp = itemView.findViewById(R.id.tvLastMessageTimestamp);
        }
    }

    public String getLastMessageTimeStampFormat(long timeStamp) {
        Calendar calendar = Calendar.getInstance();

        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisYear = calendar.get(Calendar.YEAR);

        calendar.setTimeInMillis(timeStamp);

        int messageDay = calendar.get(Calendar.DAY_OF_MONTH);
        int messageMonth = calendar.get(Calendar.MONTH);
        int messageYear = calendar.get(Calendar.YEAR);

        String formattedTime;
        if (today == messageDay && thisMonth == messageMonth && thisYear == messageYear) {
            // Tin nhắn được gửi trong ngày hôm nay
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            formattedTime = dateFormat.format(new Date(timeStamp));
        } else if (thisMonth == messageMonth && thisYear == messageYear) {
            // Tin nhắn được gửi trong tháng hiện tại
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
            formattedTime = dateFormat.format(new Date(timeStamp));
        } else {
            // Tin nhắn được gửi trong năm khác hoặc trước đó
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formattedTime = dateFormat.format(new Date(timeStamp));
        }
        return formattedTime;
    }
}
