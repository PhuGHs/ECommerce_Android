package com.example.ecommerce_hvpp.fragments.admin_fragments;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminProductAdapter;
import com.example.ecommerce_hvpp.adapter.adapterItemdecorations.GridItemDecoration;
import com.example.ecommerce_hvpp.dialog.OrderFilterDialog;
import com.example.ecommerce_hvpp.dialog.ProductFilterDialog;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.viewmodel.admin.admin_product_management.AdminProductManagementViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AdminProductListFragment extends Fragment {
    private final String TAG = "AdminProductListFragment";
    private RecyclerView rclProductList;
    private AdminProductAdapter adapter;
    private Button btnAdd;
    private AdminProductManagementViewModel viewModel;
    private NavController navController;
    private List<Product> products;
    private List<String> filterOptions;
    private int mCurrentItemPosition;
    private EditText etSearchText;
    private ImageView btnFilter;
    private TextView tvFoundText;
    private SkeletonScreen skeletonScreen;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_product_management, container, false);
        initDefaultFilters();
        initView(view);
        initDataAdapterAndViewModel();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //initialize navController
        navController = Navigation.findNavController(view);
        handleEvents();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = (this).getActivity().getMenuInflater();
        inflater.inflate(R.menu.admin_product_management_menu, menu);

        menu.setHeaderTitle("Options");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.delete_btn:
                viewModel.deleteProductWithId(products.get(mCurrentItemPosition).getId(), "disabled");
                products.remove(products.get(mCurrentItemPosition));
                adapter.notifyDataSetChanged();
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

    private void showBottomSheetDialog() {
        ProductFilterDialog orderFilterDialog = new ProductFilterDialog(filterOptions);

        orderFilterDialog.setOnFilterSelectedListener(new OrderFilterDialog.OnFilterSelectedListener() {
            @Override
            public void onFilterSelected(List<String> options) {
                filterOptions.clear();
                filterOptions.addAll(options);
                Snackbar.make(requireView(), "Filters are applied!", Snackbar.LENGTH_SHORT).show();
                adapter.setTypeAdapter(filterOptions.get(1), filterOptions.get(0));
                if(adapter.getListSize() > 1) {
                    tvFoundText.setText("Found " + adapter.getListSize() + " results");
                } else {
                    tvFoundText.setText("Found " + adapter.getListSize() + " result");
                }
            }
        });
        orderFilterDialog.show(getChildFragmentManager(), orderFilterDialog.getTag());
    }

    public void initDefaultFilters() {
        filterOptions = new ArrayList<>();
        filterOptions.add("Created Date");
        filterOptions.add("All");
        filterOptions.add("Product Name Option");
    }

    public List<String> getFilterOptions() {
        return filterOptions;
    }

    private void initDataAdapterAndViewModel() {
        //Init array
        products = new ArrayList<>();
        adapter = new AdminProductAdapter(this, products);
        rclProductList.setAdapter(adapter);
        skeletonScreen = Skeleton.bind(rclProductList)
                .adapter(adapter)
                .load(com.ethanhua.skeleton.R.layout.layout_default_item_skeleton)
                .show();
        //Initialize ViewModel
        viewModel = new ViewModelProvider(getActivity()).get(AdminProductManagementViewModel.class);

        //Initialize adapter
        viewModel.getAllProductWithNoCriteria().observe(getViewLifecycleOwner(), resource -> {
            switch(resource.status) {
                case SUCCESS:
                    products.addAll(resource.data);
                    adapter.setOriginalList(products);
                    adapter.setTypeAdapter(filterOptions.get(1), filterOptions.get(0));
                    adapter.notifyDataSetChanged();
                    if(adapter.getListSize() > 1) {
                        tvFoundText.setText("Found " + adapter.getListSize() + " results");
                    } else {
                        tvFoundText.setText("Found " + adapter.getListSize() + " result");
                    }
                    adapter.setOnLongItemClickListener(new AdminProductAdapter.OnLongItemClickListener() {
                        @Override
                        public void itemLongClicked(View v, int position) {
                            mCurrentItemPosition = position;
                            v.showContextMenu();
                        }
                    });
                    skeletonScreen.hide();
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
    }
    private void initView(View view) {
        btnFilter = view.findViewById(R.id.btnFilter);
        rclProductList = view.findViewById(R.id.RclProductList);
        btnAdd = view.findViewById(R.id.btnAdd);
        etSearchText = view.findViewById(R.id.etSearchText);
        tvFoundText = view.findViewById(R.id.tvFoundText);
    }

    private void handleEvents() {
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

        btnFilter.setOnClickListener(v -> {
            showBottomSheetDialog();
        });
    }

    public TextView getTvFoundText() {
        return tvFoundText;
    }
}
