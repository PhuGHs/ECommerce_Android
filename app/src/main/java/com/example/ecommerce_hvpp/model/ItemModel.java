package com.example.ecommerce_hvpp.model;

import android.net.Uri;

public class ItemModel {
    private Uri imageUri;
    private String link;

    public ItemModel(Uri imageUri, String link) {
        this.imageUri = imageUri;
        this.link = link;
    }


    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
