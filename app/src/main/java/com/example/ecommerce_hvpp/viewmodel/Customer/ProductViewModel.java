package com.example.ecommerce_hvpp.viewmodel.Customer;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Cart;
import com.example.ecommerce_hvpp.model.Feedback;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.model.Revenue;
import com.example.ecommerce_hvpp.model.Voucher;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ProductViewModel extends ViewModel {
    private FirebaseHelper helper;
    private List<Product> listNewArrivals, listBestSeller, listFavorite;
    public HashMap<String, Product> listAllProduct;
    private HashMap<String, Revenue> listRevenue = new HashMap<>();
    private MutableLiveData<List<Product>> mldListNewArrivals, mldListBestSeller, mldListFavorite, mldDetailCategory;
    private MutableLiveData<List<Feedback>> mldListFeedback;
    private MutableLiveData<List<Cart>> mldListCart;
    private MutableLiveData<String> addressApplied = new MutableLiveData<>();
    private List<Cart> listCart;
    private MutableLiveData<Integer> totalCartItems = new MutableLiveData<>(0);
    private MutableLiveData<Double> totalPriceCart = new MutableLiveData<>((double)0);
    private MutableLiveData<HashMap<String, List<String>>> mldCategories;
    private Pair<Pair<String, String>, String> recipientInfo;
    private String TAG = "Product ViewModel";
    Context context;
    public ProductViewModel(Context context){
        //init
        helper = FirebaseHelper.getInstance();
        mldListNewArrivals = new MutableLiveData<>();
        mldListBestSeller = new MutableLiveData<>();
        mldListFavorite = new MutableLiveData<>();
        mldListFeedback = new MutableLiveData<>();
        mldCategories = new MutableLiveData<>();
        mldListCart = new MutableLiveData<>();

        this.context = context;
        getAllProduct();
    }
    public void initData(){
        initCategories();
        initListNewArrivalsLiveData();
        initListBestSellerLiveData();
        initListFavoriteLiveData();
        initUserCart();
    }
    private void getAllProduct(){
        listAllProduct = new HashMap<>();

        SQLiteDatabase db = context.openOrCreateDatabase("PD.db", 0, null);

        String query = "SELECT * FROM PRODUCT WHERE Status != 'disabled' ";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToNext();
        while (cursor.isAfterLast() == false){
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String club = cursor.getString(2);
            String nation = cursor.getString(3);
            String season = cursor.getString(4);
            String desc = cursor.getString(5);
            double point = cursor.getDouble(6);
            double price = cursor.getDouble(7);
            String status = cursor.getString(8);
            long sizeM = cursor.getLong(9);
            long sizeL = cursor.getLong(10);
            long sizeXL = cursor.getLong(11);
            long timeAdded = cursor.getLong(12);
            String URLmain = cursor.getString(13);
            String URLsub1 = cursor.getString(14);
            String URLsub2 = cursor.getString(15);
            String URLthumb = cursor.getString(16);

            listAllProduct.put(id, new Product(id, name, club, nation, season, desc, price, point, sizeM, sizeL, sizeXL, URLmain, URLsub1, URLsub2, URLthumb, status, timeAdded * 1000));

            Log.d("Get list all product", id);
            cursor.moveToNext();
        }
    }
    public void createOrder(Context context, String deliverMethod, String note, String paymentMethod, long estimateDate, double totalPrice){
        String customer_id = helper.getAuth().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("id", customer_id + estimateDate);
        data.put("address", recipientInfo.second);
        data.put("recipientName", recipientInfo.first.first);
        data.put("phoneNumber", recipientInfo.first.second);
        data.put("customerId", customer_id);
        data.put("createdDate", Timestamp.now());
        data.put("deliverMethod", deliverMethod);
        data.put("paymentMethod", paymentMethod);
        data.put("note", note);
        Timestamp esDate = new Timestamp(estimateDate/1000, 0);
        data.put("receiveDate", esDate);
        data.put("status", "Pending");
        data.put("totalPrice", totalPrice);

        String document_id = helper.getDb().collection("Order").document().getId();

        helper.getDb().collection("Order").document(document_id)
                .set(data)
                .addOnSuccessListener(unused -> {
                    Log.d("Create Order", "success");
                    CustomToast.ShowToastMessage(context, 1, "Create order successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.d("Create Order", "failure");
                    CustomToast.ShowToastMessage(context, 1, "Create order failed!");
                })
                .addOnCompleteListener(task -> {
                    if (task.isComplete()){
                        for (Cart cart : listCart){
                            Map<String, Object> item = new HashMap<>();
                            item.put("id", cart.getProduct().getId() + cart.getSize() + cart.getQuantity());
                            item.put("product_id", cart.getProduct().getId());
                            item.put("quantity", cart.getQuantity());
                            item.put("size", cart.getSize());
                            helper.getDb().collection("Order").document(document_id)
                                    .collection("items").document(cart.getProduct().getId() + cart.getSize() + cart.getQuantity())
                                    .set(item)
                                    .addOnSuccessListener(unused -> Log.d("Order items", "success"))
                                    .addOnFailureListener(e -> Log.d("Order items", "failure"));
                        }
                    }
                });
    }
    public List<Product> getListFound(String queryName){
        List<Product> listFound = new ArrayList<>();
        Set<String> keySet = listAllProduct.keySet();

        for (String key : keySet){
            if (listAllProduct.get(key).getName().contains(queryName)){
                listFound.add(listAllProduct.get(key));
            }
        }
        return listFound;
    }
    public void addToWishList(Context context, String product_id){
        Map<String, Object> data = new HashMap<>();
        data.put("product_id", product_id);
        String customer_id = helper.getAuth().getCurrentUser().getUid();
        data.put("customer_id", customer_id);

        helper.getDb().collection("WishList").document(customer_id + "_" + product_id)
                .set(data)
                .addOnSuccessListener(unused -> {
                    Log.d("WishList","add Success");
                    CustomToast.ShowToastMessage(context, 1, "Add to wishlist successfully");
                })
                .addOnFailureListener(e -> {
                    Log.d("WishList","add Failure");
                    CustomToast.ShowToastMessage(context, 2, "Add to wishlist failed");
                });
        listFavorite.add(listAllProduct.get(product_id));
        mldListFavorite.setValue(listFavorite);
    }
    public void removeFromWishList(Context context, String product_id){
        String customer_id = helper.getAuth().getCurrentUser().getUid();

        helper.getDb().collection("WishList").document(customer_id + "_" + product_id)
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d("WishList", "delete Success");
                    CustomToast.ShowToastMessage(context, 1, "Remove from wishlist successfully");
                })
                .addOnFailureListener(e -> {
                    Log.d("WishList", "delete Failure");
                    CustomToast.ShowToastMessage(context, 2, "Remove from wishlist failed");
                });
        listFavorite.remove(listAllProduct.get(product_id));
        Log.d("Remove",product_id);
        mldListFavorite.setValue(listFavorite);
    }
    public void addToCart(Context context, String product_id, String size, long quantity){
        String customer_id = helper.getAuth().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("product_id", product_id);
        data.put("customer_id", customer_id);
        data.put("quantity", quantity);
        data.put("size", size);

        boolean added = false;
        long quantityBefore = 0;
        for (Cart cart : listCart) {
            if (cart.getProduct().getId().equals(product_id)){
                if (cart.getSize().equals(size)){
                    added = true;
                    quantityBefore = cart.getQuantity();
                    if (quantity + quantityBefore <= listAllProduct.get(product_id).getSize(size)) cart.setQuantity(quantity + quantityBefore);
                }
            }
        }
        if (added) {
            if (quantity + quantityBefore > listAllProduct.get(product_id).getSize(size)){
                CustomToast.ShowToastMessage(context, 2, "You have added this product before. The total quantity is not enough");
                return;
            }
            helper.getDb().collection("Cart").document(customer_id + "_" + product_id + "_" + size)
                    .update("quantity", quantity + quantityBefore)
                    .addOnSuccessListener(unused -> {
                        Log.d("Cart", "add more quantity Success");
                        CustomToast.ShowToastMessage(context, 1, "Add to cart successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.d("Cart", "add Failure");
                        CustomToast.ShowToastMessage(context, 2, "Add to cart failed");
                    });
        } else {
            helper.getDb().collection("Cart").document(customer_id + "_" + product_id + "_" + size)
                    .set(data)
                    .addOnSuccessListener(unused -> {
                        Log.d("Cart", "add Success");
                        CustomToast.ShowToastMessage(context, 1, "Add to cart successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.d("Cart", "add Failure");
                        CustomToast.ShowToastMessage(context, 2, "Add to cart failed");
                    });
            listCart.add(new Cart(listAllProduct.get(product_id), quantity, size));
        }
        mldListCart.setValue(listCart);
        calcTotalItemAndPriceCart();
    }
    public void removeFromCart(Context context, String product_id, long quantity, String size){
        String customer_id = helper.getAuth().getCurrentUser().getUid();

        helper.getDb().collection("Cart").document(customer_id + "_" + product_id + "_" + size)
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d("Cart", "delete Success");
                    CustomToast.ShowToastMessage(context, 1, "Remove from cart successfully");
                })
                .addOnFailureListener(e -> {
                    Log.d("Cart", "delete Failure");
                    CustomToast.ShowToastMessage(context, 2, "Remove from cart failed");
                });
        Cart cart = new Cart(listAllProduct.get(product_id), quantity, size);
        listCart.remove(cart);
        mldListCart.setValue(listCart);
        calcTotalItemAndPriceCart();
    }
    public void updateCartToDB(){
        String customer_id = helper.getAuth().getCurrentUser().getUid();
        for (Cart cart : listCart) {
            helper.getDb().collection("Cart").document(customer_id + "_" + cart.getProduct().getId() + "_" + cart.getSize())
                    .update("quantity", cart.getQuantity())
                    .addOnSuccessListener(unused -> Log.d("Cart", "update quantity to DB"));
        }
    }
    public boolean checkVoucherApply(Voucher voucher){
        long currentTime = new Date().getTime();
        if (currentTime < voucher.getStartDate() || currentTime > voucher.getEndDate()) return false;
        if (totalPriceCart.getValue() < voucher.getCondition()) return false;
        Log.d("Voucher", voucher.getApplyFor());
        if (voucher.getApplyFor().contains("All products")) return true;
        for (Cart cart : listCart){
            if (voucher.getApplyFor().contains(cart.getProduct().getClub()) || voucher.getApplyFor().contains(cart.getProduct().getNation()))
                return true;
        }
        return false;
    }
    public MutableLiveData<String> getAddressApplied(){
        String customer_id = helper.getAuth().getCurrentUser().getUid();
        helper.getDb().collection("users").document(customer_id).collection("recep_info")
                .whereEqualTo("isApplied", true).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String name = documentSnapshot.getString("name");
                        String phone = documentSnapshot.getString("phonenumber");
                        String address = documentSnapshot.getString("address");
                        addressApplied.setValue(name + " - " + phone + "\n" + address);
                        recipientInfo = new Pair<>(new Pair<>(name, phone), address);
                    }
                });
        return addressApplied;
    }
    private void initUserCart(){
        mldListCart = new MutableLiveData<>();
        listCart = new ArrayList<>();
        String customer_id = helper.getAuth().getCurrentUser().getUid();

        helper.getCollection("Cart").whereEqualTo("customer_id", customer_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String product_id = documentSnapshot.getString("product_id");
                        String size = documentSnapshot.getString("size");
                        long quantity = documentSnapshot.getLong("quantity");

                        listCart.add(new Cart(listAllProduct.get(product_id), quantity, size));
                        Log.d("Product", product_id + "/" + size);
                    }
                    Log.d("Cart", "get Success");
                    mldListCart.setValue(listCart);
                    calcTotalItemAndPriceCart();
                });
    }
    private void calcTotalItemAndPriceCart(){
        double sum = 0;
        int count = 0;
        for (Cart cart : listCart){
            sum += cart.getProduct().getPrice() * cart.getQuantity();
            count += cart.getQuantity();
        }
        totalCartItems.setValue(count);
        totalPriceCart.setValue(sum);
    }
    public MutableLiveData<Double> getTotalPriceCart(){
        return totalPriceCart;
    }
    public MutableLiveData<Integer> getTotalCartItems(){
        return totalCartItems;
    }
    public LiveData<List<Cart>> getMldUserCart(){
        return mldListCart;
    }
    public LiveData<Boolean> isFavorite(String product_id){
        String customer_id = helper.getAuth().getCurrentUser().getUid();
        MutableLiveData<Boolean> whetherFavorite = new MutableLiveData<>();

        helper.getCollection("WishList").whereEqualTo("customer_id", customer_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    whetherFavorite.setValue(false);
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        if (product_id.equals(documentSnapshot.getString("product_id"))){
                            whetherFavorite.setValue(true);
                        }
                    }
                });
        return whetherFavorite;
    }
    private void initCategories(){
        List<String> listClub = new ArrayList<>();
        List<String> listNation = new ArrayList<>();
        List<String> listSeason = new ArrayList<>();
        HashMap<String, List<String>> categories = new HashMap<>();
        List<String> listProductID = new ArrayList<>(listAllProduct.keySet());

        for (String id : listProductID){
            Product product = listAllProduct.get(id);
            String club = product.getClub();
            String nation = product.getNation();
            String season = product.getSeason();

            Log.d("Categories", club + nation + season);

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
    }
    public MutableLiveData<HashMap<String, List<String>>> getMldCategories(){
        return mldCategories;
    }
    public MutableLiveData<List<Product>> getDetailCategory(String type, String category){
        mldDetailCategory = new MutableLiveData<>();
        List<Product> listDetailCategory = new ArrayList<>();
        List<String> listProductID = new ArrayList<>(listAllProduct.keySet());

        for (String id : listProductID){
            if (type.equals("club")){
                if (listAllProduct.get(id).getClub().equals(category)) listDetailCategory.add(listAllProduct.get(id));
            }
            if (type.equals("nation")){
                if (listAllProduct.get(id).getNation().equals(category)) listDetailCategory.add(listAllProduct.get(id));
            }
            if (type.equals("season")){
                if (category.contains(listAllProduct.get(id).getSeason())) listDetailCategory.add(listAllProduct.get(id));
            }
        }
        mldDetailCategory.setValue(listDetailCategory);

        return mldDetailCategory;
    }
    private void initListFavoriteLiveData() {
        listFavorite = new ArrayList<>();

        String customer_id = helper.getAuth().getCurrentUser().getUid();

        helper.getCollection("WishList").whereEqualTo("customer_id", customer_id).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("Get WishList", "Success");
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String product_id = documentSnapshot.getString("product_id");
                        listFavorite.add(listAllProduct.get(product_id));
                    }
                    mldListFavorite.setValue(listFavorite);
                });
    }

    public void initListBestSellerLiveData() {
        listBestSeller = new ArrayList<>();

        SQLiteDatabase db = context.openOrCreateDatabase("PD.db", 0, null);
        //get all revenue
        String query = "SELECT * FROM REVENUE";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToNext();
        while (cursor.isAfterLast() == false){
            String product_id = cursor.getString(1);
            long quantity = cursor.getLong(2);
            if (listRevenue.containsKey(product_id)){
                listRevenue.get(product_id).setQuantity(quantity);
            }
            else {
                listRevenue.put(product_id, new Revenue(product_id, quantity));
            }
            cursor.moveToNext();
        }
        //find top 3
        Set<String> keys = listRevenue.keySet();
        long max = 1;
        while (max > 0){
            max = 0;
            String best = "";
            for (String key : keys){
                if (listRevenue.get(key).getQuantity() > max) {
                    max = listRevenue.get(key).getQuantity();
                    best = key;
                }
            }
            Log.d("Best seller", best);
            if (listAllProduct.get(best) != null){
                listBestSeller.add(listAllProduct.get(best));
            }
            if (listBestSeller.size() == 4) break;
            if (!best.equals("")) listRevenue.get(best).resetQuantity();
        }
        mldListBestSeller.setValue(listBestSeller);
    }

    private void initListNewArrivalsLiveData() {
        listNewArrivals = new ArrayList<>();

        SQLiteDatabase db = context.openOrCreateDatabase("PD.db", 0, null);
        String query = "SELECT ID FROM PRODUCT WHERE Status != 'disabled' ORDER BY TimeAdded DESC Limit 4";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToNext();
        while (cursor.isAfterLast() == false){
            listNewArrivals.add(listAllProduct.get(cursor.getString(0)));
            cursor.moveToNext();
        }
        mldListNewArrivals.setValue(listNewArrivals);
    }

    public MutableLiveData<List<Product>> getMldListNewArrivals() {
        return mldListNewArrivals;
    }

    public MutableLiveData<List<Product>> getMldListBestSeller() {
        return mldListBestSeller;
    }

    public MutableLiveData<List<Product>> getMldListFavorite() {
        return mldListFavorite;
    }
    public MutableLiveData<Product> getDetailProduct(String productID) {
        MutableLiveData<Product> detailProduct = new MutableLiveData<>();

        detailProduct.setValue(listAllProduct.get(productID));

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
