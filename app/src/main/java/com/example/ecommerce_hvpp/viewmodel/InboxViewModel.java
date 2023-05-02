package com.example.ecommerce_hvpp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.Inbox;
import com.example.ecommerce_hvpp.repositories.InboxRepository;
import com.example.ecommerce_hvpp.util.Resource;

import java.util.List;

public class InboxViewModel extends ViewModel {
    private MutableLiveData<Resource<List<Inbox>>> _mldInbox;
    private InboxRepository repo;

    public InboxViewModel() {
        _mldInbox = new MutableLiveData<>();
        repo = new InboxRepository();
    }

    public LiveData<Resource<List<Inbox>>> getInboxList() {
        return repo.getInboxList();
    }
}
