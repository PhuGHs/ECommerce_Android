package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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

public class ChatFragment extends Fragment {
    private RecyclerView rvChatList;
    private ChatAdapter chatAdapter;
    private EditText etTextMessage;
    private TextView tvTextMessage;
    private CardView btnSend;

    private ChatViewModel viewModel;

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

        //Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        //Initialize ChatAdapter
        if(viewModel.getChatMessagesLiveData().getValue() != null) {
            chatAdapter = new ChatAdapter(viewModel.getChatMessagesLiveData().getValue().data);
        }

        //set up recyclerview
        rvChatList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChatList.setAdapter(chatAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSend.setOnClickListener(v -> {
            String messageText = etTextMessage.getText().toString();

        });
    }
}
