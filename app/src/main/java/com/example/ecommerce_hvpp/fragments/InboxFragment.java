package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapters.InboxAdapter;
import com.example.ecommerce_hvpp.model.Inbox;
import com.example.ecommerce_hvpp.viewmodel.InboxViewModel;

import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends Fragment {
    private InboxViewModel viewModel;
    private InboxAdapter adapter;
    private List<Inbox> list;
    private RecyclerView rcvInboxList;
    private SearchView svSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_chat_list, container, false);

        //Initialize views
        rcvInboxList = view.findViewById(R.id.rcvInboxList);
        svSearch = view.findViewById(R.id.search_bar);

        //Initialize viewModel
        viewModel = new ViewModelProvider(this).get(InboxViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rcvInboxList.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        list.add(new Inbox("1", "Lê Văn Phú", "Hello", 120));
        list.add(new Inbox("2", "Lê Văn Phi", "Chào bạn", 120));
        adapter = new InboxAdapter(list);
        rcvInboxList.setAdapter(adapter);
    }
}
