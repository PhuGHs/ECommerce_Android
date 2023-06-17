package com.example.ecommerce_hvpp.fragments.customer_fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.ChatRoomAdapter;
import com.example.ecommerce_hvpp.model.ChatRoom;
import com.example.ecommerce_hvpp.viewmodel.ChatRoomViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomFragment extends Fragment {
    private static final String TAG = "ChatRoomFragment";
    private String currentUserUID;
    private ChatRoomViewModel viewModel;
    private ChatRoomAdapter adapter;
    private List<ChatRoom> list;
    private RecyclerView rcvInboxList;
    private EditText etSearch;
    private NavController navController;
    private String roomName;
    public NavController getNavController() {
        return navController;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_chat_list, container, false);

        //Initialize views
        rcvInboxList = view.findViewById(R.id.rcvInboxList);
        etSearch = view.findViewById(R.id.etSearchText);


        //Initialize viewModel
        viewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        //Initialize CurrentUserUID
        currentUserUID = viewModel.getCurrentUserUID();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rcvInboxList.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize navController
        navController = Navigation.findNavController(requireView());
        viewModel.getChatRoomList().observe(getViewLifecycleOwner(), resource -> {
            switch(resource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    list = new ArrayList<>();
                    if (resource.data != null) {
                        list.addAll(resource.data);
                    }
                    adapter = new ChatRoomAdapter(list, this);
                    rcvInboxList.setAdapter(adapter);
                    Log.i(TAG, list.isEmpty() ? "null" : "not null");
                    break;
                case ERROR:
                    break;
                default: break;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void getChatRoomName(ChatRoom room, String id) {
        viewModel.getUser(id).observe(getViewLifecycleOwner(), resource -> {
            switch(resource.status) {
                case LOADING:
                    break;
                case ERROR:
                    Log.e(TAG, resource.message);
                    break;
                case SUCCESS:
                    room.setRoomName(resource.data.getUsername());
                    room.setImagePath(resource.data.getImagePath());
                    adapter.notifyDataSetChanged();
                    break;
            }
        });
    }



    public String getCurrentUserUID() {
        return currentUserUID;
    }
}