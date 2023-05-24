package com.example.ecommerce_hvpp.viewmodel.Customer;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Feedback;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.model.Revenue;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ProductViewModel extends ViewModel {
    private FirebaseHelper helper;
    private List<Product> listNewArrivals, listBestSeller, listFavorite;
    private MutableLiveData<List<Product>> mldListNewArrivals, mldListBestSeller, mldListFavorite, mldDetailCategory;
    private MutableLiveData<List<Feedback>> mldListFeedback;
    private MutableLiveData<HashMap<String, List<String>>> mldCategories;
    private MutableLiveData<Product> detailProduct;
    private String TAG = "Product ViewModel";
    public ProductViewModel(){
        //init
        helper = FirebaseHelper.getInstance();
        mldListNewArrivals = new MutableLiveData<>();
        mldListBestSeller = new MutableLiveData<>();
        mldListFavorite = new MutableLiveData<>();
        mldListFeedback = new MutableLiveData<>();
    }
    public MutableLiveData<HashMap<String, List<String>>> getCategories(){
        mldCategories = new MutableLiveData<>();
        List<String> listClub = new ArrayList<>();
        List<String> listNation = new ArrayList<>();
        List<String> listSeason = new ArrayList<>();
        HashMap<String, List<String>> categories = new HashMap<>();

        helper.getCollection("Product").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        String club = document.getString("club");
                        String nation = document.getString("nation");
                        String season = document.getString("season");
                        if (!club.isEmpty() && !listClub.contains(club)){
                            listClub.add(club);
                        }
                        if (!nation.isEmpty() && !listNation.contains(nation)){
                            listNation.add(nation);
                        }
                        if (!season.isEmpty() && !listSeason.contains(season)){
                            if (season.length() < 5){ // it's a single season
                                long singleSeason = Long.parseLong(season);
                                String season1 = (singleSeason - 1) + "/" + singleSeason;
                                String season2 = singleSeason + "/" + (singleSeason + 1);
                                if (!listSeason.contains(season1)) listSeason.add(season1);
                                if (!listSeason.contains(season2)) listSeason.add(season2);
                            }
                            else listSeason.add(season);
                        }
                    }
                    categories.put("Club", listClub);
                    categories.put("Nation", listNation);
                    categories.put("Season", listSeason);

                    mldCategories.setValue(categories);
                });
        return mldCategories;
    }
    public MutableLiveData<List<Product>> getDetailCategory(String type, String category){
        mldDetailCategory = new MutableLiveData<>();
        List<Product> listDetailCategory = new ArrayList<>();

        if (!type.equals("season")){
            helper.getCollection("Product").whereEqualTo(type, category).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String id = documentSnapshot.getString("id");
                            String name = documentSnapshot.getString("name");
                            String club = documentSnapshot.getString("club");
                            String nation = documentSnapshot.getString("nation");
                            String season = documentSnapshot.getString("season");
                            double Price = documentSnapshot.getDouble("price");
                            double Point = documentSnapshot.getDouble("point");
                            String urlmain = documentSnapshot.getString("url_main");
                            String urlsub1 = documentSnapshot.getString("url_sub1");
                            String urlsub2 = documentSnapshot.getString("url_sub2");
                            String urlthumb = documentSnapshot.getString("url_thumb");
                            long sizeM = documentSnapshot.getLong("size_m");
                            long sizeL = documentSnapshot.getLong("size_l");
                            long sizeXL = documentSnapshot.getLong("size_xl");
                            String status = documentSnapshot.getString("status");
                            Timestamp timeAdded = documentSnapshot.getTimestamp("time_added");
                            String desc = documentSnapshot.getString("description");

                            Log.d(TAG, id + "--" + name);

                            listDetailCategory.add(new Product(id, name, club, nation, season, desc, Price, Point, sizeM, sizeL, sizeXL, urlmain, urlsub1, urlsub2, urlthumb, status, timeAdded.getSeconds() * 1000));
                        }
                        mldDetailCategory.setValue(listDetailCategory);
                    });
        }
        else
        {
            helper.getCollection("Product").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String season = documentSnapshot.getString("season");
                            if (season.equals(category) || category.contains(season)){
                                String id = documentSnapshot.getString("id");
                                String name = documentSnapshot.getString("name");
                                String club = documentSnapshot.getString("club");
                                String nation = documentSnapshot.getString("nation");
                                double Price = documentSnapshot.getDouble("price");
                                double Point = documentSnapshot.getDouble("point");
                                String urlmain = documentSnapshot.getString("url_main");
                                String urlsub1 = documentSnapshot.getString("url_sub1");
                                String urlsub2 = documentSnapshot.getString("url_sub2");
                                String urlthumb = documentSnapshot.getString("url_thumb");
                                long sizeM = documentSnapshot.getLong("size_m");
                                long sizeL = documentSnapshot.getLong("size_l");
                                long sizeXL = documentSnapshot.getLong("size_xl");
                                String status = documentSnapshot.getString("status");
                                Timestamp timeAdded = documentSnapshot.getTimestamp("time_added");
                                String desc = documentSnapshot.getString("description");

                                Log.d(TAG, id + "--" + name);

                                listDetailCategory.add(new Product(id, name, club, nation, season, desc, Price, Point, sizeM, sizeL, sizeXL, urlmain, urlsub1, urlsub2, urlthumb, status, timeAdded.getSeconds() * 1000));
                            }
                        }
                        mldDetailCategory.setValue(listDetailCategory);
                    });
        }
        return mldDetailCategory;
    }
    private void initListFavoriteLiveData() {
        listFavorite = new ArrayList<>();

        helper.getCollection("Product").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String id = documentSnapshot.getString("id");
                        String name = documentSnapshot.getString("name");
                        String club = documentSnapshot.getString("club");
                        String nation = documentSnapshot.getString("nation");
                        String season = documentSnapshot.getString("season");
                        double Price = documentSnapshot.getDouble("price");
                        double Point = documentSnapshot.getDouble("point");
                        String urlmain = documentSnapshot.getString("url_main");
                        String urlsub1 = documentSnapshot.getString("url_sub1");
                        String urlsub2 = documentSnapshot.getString("url_sub2");
                        String urlthumb = documentSnapshot.getString("url_thumb");
                        long sizeM = documentSnapshot.getLong("size_m");
                        long sizeL = documentSnapshot.getLong("size_l");
                        long sizeXL = documentSnapshot.getLong("size_xl");
                        String status = documentSnapshot.getString("status");
                        Timestamp timeAdded = documentSnapshot.getTimestamp("time_added");
                        String desc = documentSnapshot.getString("description");

                        Log.d(TAG, id + "--" + name);

                        listFavorite.add(new Product(id, name, club, nation, season, desc, Price, Point, sizeM, sizeL, sizeXL, urlmain, urlsub1, urlsub2, urlthumb, status, timeAdded.getSeconds() * 1000));
                    }
                    mldListFavorite.setValue(listFavorite);
                });
    }

    private void initListBestSellerLiveData() {
        listBestSeller = new ArrayList<>();
        List<Revenue> listRevenue = new ArrayList<>();
        List<Product> listNew = new ArrayList<>();

        //get all product
        helper.getCollection("Product").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String id = documentSnapshot.getString("id");
                        String name = documentSnapshot.getString("name");
                        String club = documentSnapshot.getString("club");
                        String nation = documentSnapshot.getString("nation");
                        String season = documentSnapshot.getString("season");
                        double Price = documentSnapshot.getDouble("price");
                        double Point = documentSnapshot.getDouble("point");
                        String urlmain = documentSnapshot.getString("url_main");
                        String urlsub1 = documentSnapshot.getString("url_sub1");
                        String urlsub2 = documentSnapshot.getString("url_sub2");
                        String urlthumb = documentSnapshot.getString("url_thumb");
                        long sizeM = documentSnapshot.getLong("size_m");
                        long sizeL = documentSnapshot.getLong("size_l");
                        long sizeXL = documentSnapshot.getLong("size_xl");
                        String status = documentSnapshot.getString("status");
                        Timestamp timeAdded = documentSnapshot.getTimestamp("time_added");
                        String desc = documentSnapshot.getString("description");

                        Log.d(TAG, id + "--" + name);

                        listNew.add(new Product(id, name, club, nation, season, desc, Price, Point, sizeM, sizeL, sizeXL, urlmain, urlsub1, urlsub2, urlthumb, status, timeAdded.getSeconds() * 1000));
                    }
                });
        helper.getCollection("OrderDetail").orderBy("product_id").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    //get all product revenue
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String product_id = documentSnapshot.getString("product_id");
                        long quantity = documentSnapshot.getLong("quantity");

                        if (listRevenue.size() < 1){
                            listRevenue.add(new Revenue(product_id, quantity));
                        }
                        else {
                            if (product_id.equals(listRevenue.get(listRevenue.size() - 1).getProduct_id())){
                                listRevenue.get(listRevenue.size() - 1).setQuantity(quantity);
                            }
                            else {
                                Log.d(listRevenue.get(listRevenue.size() - 1).getProduct_id(), listRevenue.get(listRevenue.size() - 1).getQuantity() + "/");

                                listRevenue.add(new Revenue(product_id, quantity));
                            }
                        }
                    }
                    // find top 3 best seller
                    for (int j = 0; j < 3; j++){
                        long max = 0;
                        int maxIndex = 0;
                        String best = "";
                        for (int i = 0; i < listRevenue.size(); i++){
                            if (listRevenue.get(i).getQuantity() > max){
                                max = listRevenue.get(i).getQuantity();
                                best = listRevenue.get(i).getProduct_id();
                                maxIndex = i;
                            }
                        }
                        listRevenue.get(maxIndex).resetQuantity();
                        for (Product product : listNew){
                            if (product.getId().equals(best)){
                                listBestSeller.add(product);
                            }
                        }
                    }
                });
        mldListBestSeller.setValue(listBestSeller);
    }

    private void initListNewArrivalsLiveData() {
        listNewArrivals = new ArrayList<>();

        helper.getCollection("Product").orderBy("time_added").limit(4).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                        String id = documentSnapshot.getString("id");
                        String name = documentSnapshot.getString("name");
                        String club = documentSnapshot.getString("club");
                        String nation = documentSnapshot.getString("nation");
                        String season = documentSnapshot.getString("season");
                        double Price = documentSnapshot.getDouble("price");
                        double Point = documentSnapshot.getDouble("point");
                        String urlmain = documentSnapshot.getString("url_main");
                        String urlsub1 = documentSnapshot.getString("url_sub1");
                        String urlsub2 = documentSnapshot.getString("url_sub2");
                        String urlthumb = documentSnapshot.getString("url_thumb");
                        long sizeM = documentSnapshot.getLong("size_m");
                        long sizeL = documentSnapshot.getLong("size_l");
                        long sizeXL = documentSnapshot.getLong("size_xl");
                        String status = documentSnapshot.getString("status");
                        Timestamp timeAdded = documentSnapshot.getTimestamp("time_added");
                        String desc = documentSnapshot.getString("description");

                        Log.d(TAG, id + "--" + name);

                        listNewArrivals.add(new Product(id, name, club, nation, season, desc, Price, Point, sizeM, sizeL, sizeXL, urlmain, urlsub1, urlsub2, urlthumb, status, timeAdded.getSeconds() * 1000));
                    }
                    mldListNewArrivals.setValue(listNewArrivals);
                });
    }

    public MutableLiveData<List<Product>> getMldListNewArrivals() {
        initListNewArrivalsLiveData();
        return mldListNewArrivals;
    }

    public MutableLiveData<List<Product>> getMldListBestSeller() {
        initListBestSellerLiveData();
        return mldListBestSeller;
    }

    public MutableLiveData<List<Product>> getMldListFavorite() {
        initListFavoriteLiveData();
        return mldListFavorite;
    }

    public MutableLiveData<Product> getDetailProduct(String productID) {
        detailProduct = new MutableLiveData<>();

        helper.getCollection("Product").document(productID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        String id = documentSnapshot.getString("id");
                        String name = documentSnapshot.getString("name");
                        String club = documentSnapshot.getString("club");
                        String nation = documentSnapshot.getString("nation");
                        String season = documentSnapshot.getString("season");
                        double Price = documentSnapshot.getDouble("price");
                        double Point = documentSnapshot.getDouble("point");
                        String urlmain = documentSnapshot.getString("url_main");
                        String urlsub1 = documentSnapshot.getString("url_sub1");
                        String urlsub2 = documentSnapshot.getString("url_sub2");
                        String urlthumb = documentSnapshot.getString("url_thumb");
                        long sizeM = documentSnapshot.getLong("size_m");
                        long sizeL = documentSnapshot.getLong("size_l");
                        long sizeXL = documentSnapshot.getLong("size_xl");
                        String status = documentSnapshot.getString("status");
                        Timestamp timeAdded = documentSnapshot.getTimestamp("time_added");
                        String desc = documentSnapshot.getString("description");

                        detailProduct.setValue(new Product(id, name, club, nation, season, desc, Price, Point, sizeM, sizeL, sizeXL, urlmain, urlsub1, urlsub2, urlthumb, status, timeAdded.getSeconds() * 1000));
                    }
                });

        return detailProduct;
    }
    public MutableLiveData<List<Feedback>> getFeedbackProduct(String productID){
        List<Feedback> listFeedback = new ArrayList<>();

        helper.getCollection("Feedback").whereEqualTo("product_id", productID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String name = documentSnapshot.getString("name");
                        String customerID = documentSnapshot.getString("customer_id");
                        String product_ID = documentSnapshot.getString("product_id");
                        String comment = documentSnapshot.getString("comment");
                        Long point = documentSnapshot.getLong("point");
                        Timestamp date = documentSnapshot.getTimestamp("date");

                        Log.d(TAG, date.toString());

                        listFeedback.add(new Feedback(customerID, productID, point, date.getSeconds() * 1000, comment));
                    }
                    mldListFeedback.setValue(listFeedback);
                });
        return mldListFeedback;
    }
}
