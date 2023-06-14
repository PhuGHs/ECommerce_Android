package com.example.ecommerce_hvpp.fragments.customer_fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.adapter.FeedbackCustomerAdapter;
import com.example.ecommerce_hvpp.model.Feedback;
import com.example.ecommerce_hvpp.model.Product;
import com.google.android.material.snackbar.Snackbar;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;

import java.util.ArrayList;
import java.util.List;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailProductCustomerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailProductCustomerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailProductCustomerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailProductCustomerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailProductCustomerFragment newInstance(String param1, String param2) {
        DetailProductCustomerFragment fragment = new DetailProductCustomerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_product_customer, container, false);
    }
    boolean isSharing = false;
    TextView detailName, detailSeason, detailPrice, detailPoint, detailQuantity, sizeAvailable, detailDesc;
    ImageButton btnBackToPrevious, btnFav;
    Button btnAddToCart;
    RatingBar ratingBar;
    String productID;
    private NavController navController;
    ImageSlider detailImgSlider;
    ImageButton minusQuantity, plusQuantity;
    RadioGroup sizeGroup;
    List<Long> listSize;
    Integer MinQuantity = 1, quantityAvailable = 0;
    RecyclerView feedbackRv;
    MutableLiveData<String> sizeChosen = new MutableLiveData<>("M");
    FeedbackCustomerAdapter feedbackAdapter;
    LinearLayoutManager linearLayoutManager;
    ImageButton btnShare;
    String imageLink, productName, description;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize
        navController = Navigation.findNavController(requireView());
        btnBackToPrevious = (ImageButton) view.findViewById(R.id.btnBackToPrevious);
        btnFav = (ImageButton) view.findViewById(R.id.btnModifyWishList);
        btnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBarDetailC);
        detailImgSlider = (ImageSlider) view.findViewById(R.id.detailImageSlider);
        detailName = (TextView) view.findViewById(R.id.detailNameC);
        detailSeason = (TextView) view.findViewById(R.id.detailSeasonC);
        detailPrice = (TextView) view.findViewById(R.id.detailPriceC);
        detailPoint = (TextView) view.findViewById(R.id.detailPointC);
        detailQuantity = (TextView) view.findViewById(R.id.detailQuantityC);
        minusQuantity = (ImageButton) view.findViewById(R.id.detailMinusQuantity);
        plusQuantity = (ImageButton) view.findViewById(R.id.detailPlusQuantity);
        sizeGroup = (RadioGroup) view.findViewById(R.id.sizeGroup);
        sizeAvailable = (TextView) view.findViewById(R.id.sizeAvailable);
        feedbackRv = (RecyclerView) view.findViewById(R.id.listFeedbackC);
        detailDesc = (TextView) view.findViewById(R.id.detailDesc);
        btnShare = view.findViewById(R.id.btnShare);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        //set data
        detailQuantity.setText("0");
        getDataFromPreviousFragment();

        btnBackToPrevious.setOnClickListener(view1 -> {
            if(isSharing) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.host_fragment, new HomeFragment())
                        .commit();
            } else {
                navController.popBackStack();
            }
        });

        modifyQuantityProduct();

        setSizeQuantityAndSizeChosen();

        handleShare();
    }
    public void getDataFromPreviousFragment(){
        Bundle bundle = getArguments();
        if (bundle != null){
            productID = bundle.getString("productID");
            if(bundle.containsKey("sharing")) {
                isSharing = bundle.getBoolean("sharing");
            }

            MainActivity.PDviewModel.getDetailProduct(productID).observe(getViewLifecycleOwner(), product -> {
                detailName.setText(product.getName());
                detailSeason.setText(product.getSeason());
                detailPrice.setText("$"+Double.toString(product.getPrice()));
                detailPoint.setText(Double.toString(product.getPointAvg()));
                detailDesc.setText(product.getDescription());

                ratingBar.setRating((float)product.getPointAvg());
                listSize = new ArrayList<>();
                sizeAvailable.setText(String.valueOf(product.getSizeM()));
                quantityAvailable = (int) product.getSizeM();
                listSize.add(product.getSizeM());
                listSize.add(product.getSizeL());
                listSize.add(product.getSizeXL());

                loadDetailImage(product);
                productName = product.getName();
                description = product.getDescription();
            });
            MainActivity.PDviewModel.isFavorite(productID).observe(getViewLifecycleOwner(), favorite -> {
                if (favorite){
                    btnFav.setImageResource(R.drawable.full_heart);
                }
                else btnFav.setImageResource(R.drawable.outline_heart);
                btnFav.setOnClickListener(view -> {
                    if (favorite){
                        MainActivity.PDviewModel.removeFromWishList(getContext(), productID);
                        btnFav.setImageResource(R.drawable.outline_heart);
                    }
                    else {
                        MainActivity.PDviewModel.addToWishList(getContext(), productID);
                        btnFav.setImageResource(R.drawable.full_heart);
                    }
                });
            });
            MainActivity.PDviewModel.getFeedbackProduct(productID).observe(getViewLifecycleOwner(), feedbacks -> getDetailFeedbackAndSetFeedbackRecycleView(feedbacks));
        }
    }
    public void modifyQuantityProduct(){
        minusQuantity.setOnClickListener(view12 -> {
            int quantity = Integer.parseInt(detailQuantity.getText().toString());
            if (quantity > MinQuantity){
                quantity--;
                detailQuantity.setText(String.valueOf(quantity));
            }
        });
        plusQuantity.setOnClickListener(view13 -> {
            int quantity = Integer.parseInt(detailQuantity.getText().toString());
            if (quantity >= quantityAvailable){
                CustomToast.ShowToastMessage(getContext(), 2, "Not enough quantity!!");
            }
            else quantity++;
            detailQuantity.setText(String.valueOf(quantity));
        });
    }
    public void setSizeQuantityAndSizeChosen(){
        sizeGroup.setOnCheckedChangeListener((radioGroup, checkID) -> {
            if (listSize != null){
                if (checkID == R.id.rbtnSizeM){
                    sizeAvailable.setText(String.valueOf(listSize.get(0)));
                    quantityAvailable = (int) MainActivity.PDviewModel.listAllProduct.get(productID).getSizeM();
                    sizeChosen.setValue("M");
                }
                if (checkID == R.id.rbtnSizeL){
                    sizeAvailable.setText(String.valueOf(listSize.get(1)));
                    quantityAvailable = (int) MainActivity.PDviewModel.listAllProduct.get(productID).getSizeL();
                    sizeChosen.setValue("L");
                }
                if (checkID == R.id.rbtnSizeXL){
                    sizeAvailable.setText(String.valueOf(listSize.get(2)));
                    quantityAvailable = (int) MainActivity.PDviewModel.listAllProduct.get(productID).getSizeXL();
                    sizeChosen.setValue("XL");
                }
            }
        });
        sizeChosen.observe(getViewLifecycleOwner(), size -> {
            btnAddToCart.setOnClickListener(view -> {
                int quantity = Integer.parseInt(detailQuantity.getText().toString());
                if (quantity == 0) {
                    CustomToast.ShowToastMessage(getContext(), 2, "Quantity > 0, please");
                }
                else
                if (quantity > quantityAvailable){
                    CustomToast.ShowToastMessage(getContext(), 2, "Not enough quantity");
                }
                else
                MainActivity.PDviewModel.addToCart(getContext(), productID, size, Long.parseLong(detailQuantity.getText().toString()));
            });
        });
    }
    public void loadDetailImage(Product product){
        ArrayList<SlideModel> listImage = new ArrayList<>();

        SlideModel slideModel1 = new SlideModel(R.drawable.product_pattern_with_bg, ScaleTypes.FIT);
        SlideModel slideModel2 = new SlideModel(R.drawable.product_pattern_with_bg, ScaleTypes.FIT);
        SlideModel slideModel3 = new SlideModel(R.drawable.product_pattern_with_bg, ScaleTypes.FIT);


        slideModel1.setImageUrl(product.getUrlmain());
        slideModel2.setImageUrl(product.getUrlsub1());
        slideModel3.setImageUrl(product.getUrlsub2());
        listImage.add(slideModel1);
        listImage.add(slideModel2);
        listImage.add(slideModel3);

        detailImgSlider.setImageList(listImage, ScaleTypes.FIT);
        imageLink = product.getUrlmain();
    }
    public void getDetailFeedbackAndSetFeedbackRecycleView(List<Feedback> listFeedback){
        feedbackAdapter = new FeedbackCustomerAdapter(getContext(), (ArrayList<Feedback>) listFeedback);
        feedbackRv.setAdapter(feedbackAdapter);
        feedbackRv.setLayoutManager(linearLayoutManager);
    }

    public void handleShare() {
        btnShare.setOnClickListener(v -> {
            String path = "product/" + productID;
            BranchUniversalObject buo = new BranchUniversalObject()
                    .setCanonicalIdentifier(path)
                    .setTitle(productName)
                    .setContentDescription(description)
                    .setContentImageUrl(imageLink)
                    .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);

            io.branch.referral.util.LinkProperties properties = new io.branch.referral.util.LinkProperties()
                            .setFeature("sharing")
                            .addControlParameter("$desktop_url", "https://example.phu.com")
                            .addControlParameter("custom_data", productID);

            buo.generateShortUrl(getContext(), properties, new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError error) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(getContext(), ClipboardManager.class);
                    ClipData clip = ClipData.newPlainText("label", url);
                    clipboard.setPrimaryClip(clip);

                    if(error != null) {
                        Log.i("link fail", error.getMessage());
                    }

                    Log.d("url", url == null ? "url null" : url);
                    Log.d("properties", properties == null ? "properties null" : properties.getControlParams().get("custom_data"));

                    Snackbar.make(requireView(), "Copied to clipboard!", Snackbar.LENGTH_SHORT).show();
                }
            });
        });
    }
}