package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapters.ChatRoomAdapter;
import com.example.ecommerce_hvpp.model.ChatRoom;
import com.example.ecommerce_hvpp.viewmodel.ChatRoomViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomFragment extends Fragment {
    private static final String TAG = "ChatRoomFragment";
    private ChatRoomViewModel viewModel;
    private ChatRoomAdapter adapter;
    private List<ChatRoom> list;
    private RecyclerView rcvInboxList;
    private SearchView svSearch;
    private NavController navController;
    public NavController getNavController() {
        return navController;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_chat_list, container, false);

        //Initialize views
        rcvInboxList = view.findViewById(R.id.rcvInboxList);
        svSearch = view.findViewById(R.id.search_bar);


        //Initialize viewModel
        viewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rcvInboxList.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize navController
        navController = Navigation.findNavController(requireView());
        viewModel.getChatRoomList().observe(requireActivity(), resource -> {
            switch(resource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    list = new ArrayList<>();
                    if (resource.data != null) {
                        list.addAll(resource.data);
                    }
                    Log.i(TAG, list.isEmpty() ? "null" : "not null");
                    adapter = new ChatRoomAdapter(list, this);
                    rcvInboxList.setAdapter(adapter);
                    break;
                case ERROR:
                    break;
                default: break;
            }
        });
    }
}
