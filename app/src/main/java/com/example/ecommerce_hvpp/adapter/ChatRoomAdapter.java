package com.example.ecommerce_hvpp.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.fragments.customer_fragments.ChatRoomFragment;
import com.example.ecommerce_hvpp.model.ChatRoom;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<ChatRoom> list;
    private List<ChatRoom> backUpList;
    private ChatRoomFragment parent;
    private String senderId;
    private String recipientId;
    private String roomName;
    private String lastMessage = "";
    private String url = "";

    public ChatRoomAdapter(List<ChatRoom> list, ChatRoomFragment parent) {
        this.list = list;
        this.parent = parent;
        this.backUpList = new ArrayList<>(list);
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
        ib.setRoomName(roomName);
        Glide.with(parent.getContext())
                        .load(url)
                                .fitCenter()
                                        .into(viewHolder.siImage);
        viewHolder.tvRecipientName.setText(roomName);
        viewHolder.tvLastMessage.setText(lastMessage);
        viewHolder.tvLastMessageTimestamp.setText(getLastMessageTimeStampFormat(ib.getLastMessageTimeStamp()));

        // implement button click and pass value to new fragment through navController bundle
        viewHolder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("roomId", ib.getChatRoomId());
            bundle.putString("roomName", roomName);
            bundle.putString("senderId", senderId);
            bundle.putString("recipientId", recipientId);
            bundle.putString("imagePath", url);
            parent.getNavController().navigate(R.id.navigate_to_chatDetail, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void updateRoomName(String roomName) {
        this.roomName = roomName;
        notifyDataSetChanged();
    }

    public void updateRoomAvatar(String url) {
        this.url = url;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return chatRoomFilter;
    }

    private Filter chatRoomFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ChatRoom> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(backUpList);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (ChatRoom room : backUpList) {
                    Log.i("roomName", room.getRoomName());
                    if (room.getRoomName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(room);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            List<ChatRoom> filteredList = (List<ChatRoom>) filterResults.values;
            if(filteredList != null) {
                list.clear();
                list.addAll(filteredList);
                notifyDataSetChanged();
            }
        }
    };

    public void setBackUpList(List<ChatRoom> rooms) {
        backUpList.addAll(rooms);
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

        public void bind(ChatRoom ib) {

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
