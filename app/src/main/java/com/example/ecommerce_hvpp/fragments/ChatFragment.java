package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapters.ChatAdapter;
import com.example.ecommerce_hvpp.viewmodel.ChatViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class ChatFragment extends Fragment {
    private RecyclerView rvChatList;
    private ChatAdapter chatAdapter;
    private EditText etTextMessage;
    private CardView btnSend;

    private ChatViewModel viewModel;
    private String roomId;
    private String senderId;
    private String recipientId;
    public static final String TAG = "ChatFragment";

    public ChatFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);

        //initialize views
        rvChatList = view.findViewById(R.id.RclMessageList);
        btnSend = view.findViewById(R.id.cvBtnSend);
        etTextMessage = view.findViewById(R.id.etMessage);

        //Initialize RoomId
        assert getArguments() != null;
        roomId = getArguments().getString("roomId");
        senderId = getArguments().getString("senderId");
        recipientId = getArguments().getString("recipientId");

        //Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        //Initialize ChatAdapter
        if(viewModel.getChatMessagesLiveData(roomId).getValue() != null) {
            viewModel.getChatMessagesLiveData(roomId).observe(requireActivity(), resource -> {
               switch(resource.status) {
                   case LOADING:
                       break;
                   case SUCCESS:
                       chatAdapter = new ChatAdapter(Objects.requireNonNull(resource.data));
                       //set up recyclerview
                       rvChatList.setLayoutManager(new LinearLayoutManager(getContext()));
                       rvChatList.setAdapter(chatAdapter);
                       break;
                   case ERROR:
                       Log.i(TAG, resource.message);
               }
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSend.setOnClickListener(v -> {
            String messageText = etTextMessage.getText().toString();
            Log.i(TAG, "button works");
            viewModel.sendChatMessage(roomId, senderId, recipientId, System.currentTimeMillis(), messageText);
            etTextMessage.setText("");
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

}
