package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminProductAdapter;
import com.example.ecommerce_hvpp.adapter.adapterItemdecorations.GridItemDecoration;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.viewmodel.admin_product_management.AdminProductManagementViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminProductListFragment extends Fragment {
    private final String TAG = "AdminProductListFragment";
    private SearchView svSearch;
    private RecyclerView rclProductList;
    private AdminProductAdapter adapter;
    private Button btnAdd;
    private AdminProductManagementViewModel viewModel;
    private NavController navController;
    private List<Product> products;
    private int mCurrentItemPosition;
    private EditText etSearchText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_product_management, container, false);


        products = new ArrayList<>();
        //Initialize view
//        svSearch = view.findViewById(R.id.svSearch);
        rclProductList = view.findViewById(R.id.RclProductList);
        btnAdd = view.findViewById(R.id.btnAdd);
        etSearchText = view.findViewById(R.id.etSearchText);

        //Initialize ViewModel
        viewModel = new ViewModelProvider(getActivity()).get(AdminProductManagementViewModel.class);

        //Initialize adapter
        viewModel.getAllProductWithNoCriteria().observe(getViewLifecycleOwner(), resource -> {
            switch(resource.status) {
                case SUCCESS:
                    products = resource.data;
                    adapter = new AdminProductAdapter(getContext(), resource.data);
                    rclProductList.setAdapter(adapter);
                    adapter.setOnLongItemClickListener(new AdminProductAdapter.OnLongItemClickListener() {
                        @Override
                        public void itemLongClicked(View v, int position) {
                            mCurrentItemPosition = position;
                            v.showContextMenu();
                        }
                    });
                    break;
                case ERROR:
                    Log.e(TAG, resource.message);
                    break;
                case LOADING:
                    break;
                default: break;
            }
        });
        rclProductList.setLayoutManager(new GridLayoutManager(getContext(), 2));


        rclProductList.addItemDecoration(new GridItemDecoration(2, 88, false));
        registerForContextMenu(rclProductList);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //initialize navController
        navController = Navigation.findNavController(view);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.navigate_to_productDetails);
            }
        });

        etSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //no action needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //no action needed
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = (this).getActivity().getMenuInflater();
        inflater.inflate(R.menu.admin_product_management_menu, menu);

        menu.setHeaderTitle("Options");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.delete_btn:
                break;
            case R.id.edit_btn:
                Bundle bundle = new Bundle();
                bundle.putString("productId", products.get(mCurrentItemPosition).getId());
                navController.navigate(R.id.navigate_to_productDetails, bundle);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
}
