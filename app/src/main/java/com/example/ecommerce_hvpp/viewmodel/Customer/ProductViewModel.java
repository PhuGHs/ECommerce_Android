package com.example.ecommerce_hvpp.viewmodel.Customer;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Cart;
import com.example.ecommerce_hvpp.model.Feedback;
import com.example.ecommerce_hvpp.model.Product;
import com.google.firebase.Timestamp;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ProductViewModel extends ViewModel {
    private FirebaseHelper helper;
    private List<Product> listNewArrivals, listBestSeller, listFavorite;
    private HashMap<String, Product> listAllProduct;
    private MutableLiveData<List<Product>> mldListNewArrivals, mldListBestSeller, mldListFavorite, mldDetailCategory;
    private MutableLiveData<List<Feedback>> mldListFeedback;
    private MutableLiveData<List<Cart>> mldListCart;
    private List<Cart> listCart;
    private MutableLiveData<Integer> totalCartItems = new MutableLiveData<>(0);
    private MutableLiveData<Double> totalPriceCart = new MutableLiveData<>((double)0);
    private MutableLiveData<HashMap<String, List<String>>> mldCategories;
    private String TAG = "Product ViewModel";
    public ProductViewModel(){
        //init
        helper = FirebaseHelper.getInstance();
        mldListNewArrivals = new MutableLiveData<>();
        mldListBestSeller = new MutableLiveData<>();
        mldListFavorite = new MutableLiveData<>();
        mldListFeedback = new MutableLiveData<>();
        mldCategories = new MutableLiveData<>();
        mldListCart = new MutableLiveData<>();

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> getAllProduct()); // wait get all product
        future.join();
    }
    public void initData(){
        initCategories();
        initListNewArrivalsLiveData();
//        initListBestSellerLiveData();
        initListFavoriteLiveData();
        initUserCart();
    }
    private void getAllProduct(){
        listAllProduct = new HashMap<>();

        helper.getCollection("Product")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("Get all product", "Success");
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String id = documentSnapshot.getString("id");
                        String name = documentSnapshot.getString("name");
                        String club = documentSnapshot.getString("club");
                        String nation = documentSnapshot.getString("nation");
                        String season = documentSnapshot.getString("season");
                        double Price = documentSnapshot.getDouble("price");
                        double Point = documentSnapshot.getDouble("pointAvg");
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

                        Log.d("Get all", id + "--" + name);

                        listAllProduct.put(id, new Product(id, name, club, nation, season, desc, Price, Point, sizeM, sizeL, sizeXL, urlmain, urlsub1, urlsub2, urlthumb, status, timeAdded.getSeconds() * 1000));
                    }
                })
                .addOnFailureListener(e -> Log.d("Get all", "Failure"));
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
    public void addToWishList(String product_id){
        Map<String, Object> data = new HashMap<>();
        data.put("product_id", product_id);
        String customer_id = helper.getAuth().getCurrentUser().getUid();
        data.put("customer_id", customer_id);

        helper.getDb().collection("WishList").document(customer_id + "_" + product_id)
                .set(data)
                .addOnSuccessListener(unused -> Log.d("WishList","add Success"))
                .addOnFailureListener(e -> Log.d("WishList","add Failure"));
        listFavorite.add(listAllProduct.get(product_id));
        mldListFavorite.setValue(listFavorite);
    }
    public void removeFromWishList(String product_id){
        String customer_id = helper.getAuth().getCurrentUser().getUid();

        helper.getDb().collection("WishList").document(customer_id + "_" + product_id)
                .delete()
                .addOnSuccessListener(unused -> Log.d("WishList", "delete Success"))
                .addOnFailureListener(e -> Log.d("WishList", "delete Failure"));
        listFavorite.remove(listAllProduct.get(product_id));
        Log.d("Remove",product_id);
        mldListFavorite.setValue(listFavorite);
    }
    public void addToCart(String product_id, String size, long quantity){
        String customer_id = helper.getAuth().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("product_id", product_id);
        data.put("customer_id", customer_id);
        data.put("quantity", quantity);
        data.put("size", size);

        helper.getDb().collection("Cart").document(customer_id + "_" + product_id + "_" + size)
                .set(data)
                .addOnSuccessListener(unused -> Log.d("Cart", "add Success"))
                .addOnFailureListener(e -> Log.d("Cart", "add Failure"));
        listCart.add(new Cart(listAllProduct.get(product_id), quantity, size));
        mldListCart.setValue(listCart);
        calcTotalItemAndPriceCart();
    }
    public void removeFromCart(String product_id, long quantity, String size){
        String customer_id = helper.getAuth().getCurrentUser().getUid();

        helper.getDb().collection("Cart").document(customer_id + "_" + product_id + "_" + size)
                .delete()
                .addOnSuccessListener(unused -> Log.d("Cart", "delete Success"))
                .addOnFailureListener(e -> Log.d("Cart", "delete Failure"));
        Cart cart = new Cart(listAllProduct.get(product_id), quantity, size);
        listCart.remove(cart);
        mldListCart.setValue(listCart);
        calcTotalItemAndPriceCart();
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

//    private void initListBestSellerLiveData() {
//        listBestSeller = new ArrayList<>();
//        List<Revenue> listRevenue = new ArrayList<>();
//
//        helper.getCollection("OrderDetail").orderBy("product_id").get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    //get all product revenue
//                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                        String product_id = documentSnapshot.getString("product_id");
//                        long quantity = documentSnapshot.getLong("quantity");
//
//                        if (listRevenue.size() < 1){
//                            listRevenue.add(new Revenue(product_id, quantity));
//                        }
//                        else {
//                            if (product_id.equals(listRevenue.get(listRevenue.size() - 1).getProduct_id())){
//                                listRevenue.get(listRevenue.size() - 1).setQuantity(quantity);
//                            }
//                            else {
//                                Log.d(listRevenue.get(listRevenue.size() - 1).getProduct_id(), listRevenue.get(listRevenue.size() - 1).getQuantity() + "/");
//
//                                listRevenue.add(new Revenue(product_id, quantity));
//                            }
//                        }
//                    }
//                    // find top 3 best seller
//                    for (int j = 0; j < 3; j++){
//                        long max = 0;
//                        int maxIndex = 0;
//                        String best = "";
//                        for (int i = 0; i < listRevenue.size(); i++){
//                            if (listRevenue.get(i).getQuantity() > max){
//                                max = listRevenue.get(i).getQuantity();
//                                best = listRevenue.get(i).getProduct_id();
//                                maxIndex = i;
//                            }
//                        }
//                        listRevenue.get(maxIndex).resetQuantity();
//                        listBestSeller.add(listAllProduct.get(best));
//                    }
//                    mldListBestSeller.setValue(listBestSeller);
//                });
//    }

    private void initListNewArrivalsLiveData() {
        listNewArrivals = new ArrayList<>();

        helper.getCollection("Product").orderBy("time_added").limit(4).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String id = documentSnapshot.getString("id");
                        listNewArrivals.add(listAllProduct.get(id));
                    }
                    mldListNewArrivals.setValue(listNewArrivals);
                });
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

    public void generateSharingLink(Uri deepLink, Uri preViewImageLink, final OnShareableLinkGeneratedListener listener) {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(deepLink)
                .setDomainUriPrefix("https://hvpp.page.link")
                .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder()
                        .setImageUrl(preViewImageLink)
                        .build())
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink();

        listener.onShareableLinkGenerated(dynamicLink.getUri());
        Log.i("link", dynamicLink.getUri().toString());
    }

    public interface OnShareableLinkGeneratedListener {
        void onShareableLinkGenerated(Uri shareableLink);
    }

//    public void handleIncomingDeepLinks(NavController navController, Intent intent) {
//        FirebaseDynamicLinks.getInstance()
//                .getDynamicLink(intent)
//                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
//                    @Override
//                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                        Uri deepLink = pendingDynamicLinkData.getLink();
//                        String path = deepLink.getPath();
//                    }
//                })
//    }
}
