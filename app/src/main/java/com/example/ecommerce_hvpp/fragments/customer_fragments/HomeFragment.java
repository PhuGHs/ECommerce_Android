package com.example.ecommerce_hvpp.fragments.customer_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.adapter.ProductAdapter;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.viewmodel.ChatRoomViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    RecyclerView listNewArrivalsRv, listBestSellerRv, listFoundRv;
    LinearLayoutManager linearLayoutManager1, linearLayoutManager2;
    GridLayoutManager gridLayoutManager;
    ProductAdapter newArrivalAdapter, bestSellerAdapter, foundAdapter;
    private NavController navController;
    ImageSlider imgSlider;
    SearchView searchView;
    TextView productFound;
    ScrollView scrollHome;
    LinearLayout layoutSearch;
    private ImageButton btnChat;
    private ChatRoomViewModel chatRoomViewModel;
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        chatRoomViewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init
        navController = Navigation.findNavController(requireView());
        listNewArrivalsRv = (RecyclerView) view.findViewById(R.id.listNewArrivals);
        listBestSellerRv = (RecyclerView) view.findViewById(R.id.listBestSeller);
        listFoundRv = (RecyclerView) view.findViewById(R.id.listFound);
        imgSlider = (ImageSlider) view.findViewById(R.id.autoImageSlider);
        searchView = (SearchView) view.findViewById(R.id.searchViewHome);
        productFound = (TextView) view.findViewById(R.id.productFound);
        scrollHome = (ScrollView) view.findViewById(R.id.scrollHome);
        layoutSearch = (LinearLayout) view.findViewById(R.id.layoutSearch);
        layoutSearch.setEnabled(false);
        layoutSearch.setVisibility(View.INVISIBLE);
        linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        listNewArrivalsRv.setLayoutManager(linearLayoutManager1);
        listBestSellerRv.setLayoutManager(linearLayoutManager2);
        listFoundRv.setLayoutManager(gridLayoutManager);

        //load data
        loadImageSlider();
        getListNewArrivals();
        getListBestSeller();

        //navigate
        ImageButton btnNavToCart = (ImageButton) view.findViewById(R.id.btnNavToCart);
        ImageButton btnNavToMessage = (ImageButton) view.findViewById(R.id.btnNavToMessage);
        btnNavToCart.setOnClickListener(view1 -> navController.navigate(R.id.cartFragment));
        btnNavToMessage.setOnClickListener(view12 -> {
            chatRoomViewModel.checkIfHasRoomBefore().observe(getViewLifecycleOwner(), resource2 -> {
                switch (resource2.status) {
                    case SUCCESS:
                        if(resource2.data.isEmpty()) {
                            chatRoomViewModel.createNewChatRoom();
                            Log.e("come to create", "true");
                        }
                        break;
                    default:
                        break;
                }
            });

            chatRoomViewModel.getChatRoomList().observe(getViewLifecycleOwner(), resource -> {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case ERROR:
                        break;
                    case SUCCESS:
                        Bundle bundle = new Bundle();
                        bundle.putString("roomId", resource.data.get(0).getChatRoomId());
                        bundle.putString("senderId", FirebaseHelper.getInstance().getAuth().getCurrentUser().getUid());
                        bundle.putString("recipientId", "03oJJtgjDlMjZkIQl65anPzEvm62");
                        bundle.putString("roomName", resource.data.get(0).getRoomName());
                        bundle.putString("imagePath", resource.data.get(0).getImagePath());

                        navController.navigate(R.id.action_homeFragment_to_chatFragment, bundle);
                        break;
                }
            });
        });

        //search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                int result = MainActivity.PDviewModel.getListFound(s).size();
                String text = "";
                if (result > 1) text = " results"; else text = " result";
                productFound.setText("Found " + result + text);
                foundAdapter = new ProductAdapter(getContext(), (ArrayList<Product>) MainActivity.PDviewModel.getListFound(s), requireView(), false);
                listFoundRv.setAdapter(foundAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()){
                    scrollHome.setEnabled(true);
                    layoutSearch.setEnabled(false);
                    scrollHome.setVisibility(View.VISIBLE);
                    layoutSearch.setVisibility(View.INVISIBLE);
                }
                else {
                    scrollHome.setEnabled(false);
                    layoutSearch.setEnabled(true);
                    scrollHome.setVisibility(View.INVISIBLE);
                    layoutSearch.setVisibility(View.VISIBLE);

                    int result = MainActivity.PDviewModel.getListFound(s).size();
                    String text = "";
                    if (result > 1) text = " results"; else text = " result";
                    productFound.setText("Found " + result + text);
                    foundAdapter = new ProductAdapter(getContext(), (ArrayList<Product>) MainActivity.PDviewModel.getListFound(s), requireView(), false);
                    listFoundRv.setAdapter(foundAdapter);
                }
                return false;
            }
        });
    }
    public void getListNewArrivals(){
        MainActivity.PDviewModel.getMldListNewArrivals().observe(getViewLifecycleOwner(), products -> {
            newArrivalAdapter = new ProductAdapter(getContext(), (ArrayList<Product>) products, requireView(), false);
            listNewArrivalsRv.setAdapter(newArrivalAdapter);
        });
    }
    public void getListBestSeller(){
        MainActivity.PDviewModel.getMldListBestSeller().observe(getViewLifecycleOwner(), products -> {
            bestSellerAdapter = new ProductAdapter(getContext(), (ArrayList<Product>) products, requireView(), false);
            listBestSellerRv.setAdapter(bestSellerAdapter);
        });
    }
    public void loadImageSlider(){
        ArrayList<SlideModel> listImage = new ArrayList<>();

        listImage.add(new SlideModel(R.drawable.wall0_real, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.wall1_ucl, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.wall2_arg, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.wall3_dortmund, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.wall4_etihad, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.wall5_liverpool, ScaleTypes.FIT));

        imgSlider.setImageList(listImage, ScaleTypes.FIT);
    }
}