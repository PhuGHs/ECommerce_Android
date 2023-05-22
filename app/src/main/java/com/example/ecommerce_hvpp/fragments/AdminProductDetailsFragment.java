package com.example.ecommerce_hvpp.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminProductImageSlider;
import com.example.ecommerce_hvpp.model.ItemModel;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.util.CurrencyFormat;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.util.Resource;
import com.example.ecommerce_hvpp.viewmodel.admin_product_management.AdminProductDetailsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class AdminProductDetailsFragment extends Fragment {
    //region FIELDS
    private static final int PICK_IMAGE_REQUEST_CODE = 456;
    private final String TAG = "AdminProductDetailsFragment";
    private ProgressDialog progressDialog;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> thumbnailLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private AdminProductDetailsViewModel viewModel;
    private ArrayAdapter<CharSequence> TypeAdapter, SeasonAdapter;
    private AdminProductImageSlider SlideAdapter;
    private NavController navController;
    private ContentResolver contentResolver;
    private TextView tvHeader;
    private ImageView btnBack;
    private AppCompatButton btnAddMoreImages;
    private SliderView sliderView;
    private String Id;
    private TextView tvThumbnailImage;
    private EditText etName, etPrice, etDescription;
    private Button btnSave, btnCancel;
    private Spinner spType, spSeason;
    private Uri thumbnailImage;
    //endregion

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if(isGranted) {
                openGallery();
            } else {
                // do nothing
                //show message (disabled)
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == FragmentActivity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    if (data.getClipData() != null) {
                        // Multiple images were selected
                        handleMultipleImages(data);
                    } else if (data.getData() != null) {
                        // Only one image was selected
                        processSelectedImage(data.getData());
                    }
                }
            }
        });

        thumbnailLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == FragmentActivity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    thumbnailImage = data.getData();
                    tvThumbnailImage.setText(getFileNameFromUri(thumbnailImage));
                }
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_product_detail, container, false);

        //Initialize viewModel
        viewModel = new ViewModelProvider(this).get(AdminProductDetailsViewModel.class);
        //get productId
        if(getArguments() == null) {
            Id = "Add Product";
            viewModel.setIsEditMode(false);
        } else {
            Id = getArguments().getString("productId");
            viewModel.setIsEditMode(true);
        }

        contentResolver = getContext().getContentResolver();

        //region Initialize View
        tvHeader = view.findViewById(R.id.header_title);
        btnBack = view.findViewById(R.id.btnBackProductDetail);
        etName = view.findViewById(R.id.etName);
        etPrice = view.findViewById(R.id.etPrice);
        tvThumbnailImage = view.findViewById(R.id.tvThumbImageFile);
        etDescription = view.findViewById(R.id.etDescriptionName);
        spType = view.findViewById(R.id.type_Spinner);
        spSeason = view.findViewById(R.id.season_spinner);
        btnAddMoreImages = view.findViewById(R.id.abtnAddMoreImage);

        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        //endregion

        //region initialize spinner adapter
        TypeAdapter = ArrayAdapter.createFromResource(getContext() , R.array.type_array, R.layout.simple_spinner_string_item);
        SeasonAdapter = ArrayAdapter.createFromResource(getContext() , R.array.season_array, R.layout.simple_spinner_string_item);
        TypeAdapter.setDropDownViewResource(com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item);
        SeasonAdapter.setDropDownViewResource(com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item);
        spType.setAdapter(TypeAdapter);
        spSeason.setAdapter(SeasonAdapter);
        //endregion

        //region sliderView
        sliderView = view.findViewById(R.id.imageSlider);
        SlideAdapter = new AdminProductImageSlider(getContext());
        sliderView.setSliderAdapter(SlideAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setScrollTimeInSec(3);
        sliderView.stopAutoCycle();
        //endregion

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Initialize navController
        navController = Navigation.findNavController(view);

        tvHeader.setText(Id);

        btnAddMoreImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the permission is already granted
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission if it has not been granted
                    requestGalleryPermission();
                } else {
                    // Permission already granted, proceed with gallery access
                    openGallery();
                }
            }
        });

        tvThumbnailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenThumbnailGallery();
            }
        });

        viewModel.getIsEditMode().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isEditMode) {
                if(isEditMode) {
                    implementEditFunctionality();
                } else {
                    implementAddFunctionality();
                }
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // pop back stack using nav controller
                navController.popBackStack();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });
    }

    public void implementEditFunctionality() {
        // set value
        viewModel.getProduct(Id).observe(getViewLifecycleOwner(), resource -> {
            switch(resource.status) {
                case LOADING:
                    break;
                case ERROR:
                    Log.e(TAG, resource.message);
                    break;
                case SUCCESS:
                    etName.setText(resource.data.getName());
                    etPrice.setText(CurrencyFormat.getVNDCurrency(resource.data.getPrice()));
                    if(resource.data.getClub() == "") {
                        spType.setSelection(TypeAdapter.getPosition("Nation"));
                    }
                    else {
                        spType.setSelection(TypeAdapter.getPosition("Club"));
                    }
                    spSeason.setSelection(SeasonAdapter.getPosition(resource.data.getSeason()));
                    tvThumbnailImage.setText(resource.data.getUrlthumb());
                    SlideAdapter.addItem(new ItemModel(null, resource.data.getUrlmain()));
                    SlideAdapter.addItem(new ItemModel(null, resource.data.getUrlsub1()));
                    SlideAdapter.addItem(new ItemModel(null, resource.data.getUrlsub2()));
                    break;
            }
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        });
    }
    public void implementAddFunctionality() {
        btnCancel.setVisibility(View.GONE);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFunc_addProduct();
            }
        });
    }

    public void addFunc_addProduct() {
        String Name, Type, Season, Price, Description;
        Name = etName.getText().toString();
        Type = spType.getSelectedItem().toString();
        Season = spSeason.getSelectedItem().toString();
        Price = etPrice.getText().toString();
        Description = etDescription.getText().toString();

        Product pd = new Product(Name, Season, Price, Description);

        viewModel.addProduct(contentResolver, pd, thumbnailImage , SlideAdapter.getList());

        viewModel.getCombinedLiveData().observe(getViewLifecycleOwner(), resource -> {
            if(resource.status == Resource.Status.LOADING) {
                showProgressDialog();
            } else {
                hideProgressDialog();

                if(resource.status == Resource.Status.SUCCESS) {
                    CustomToast toastShowSuccess = new CustomToast();
                    toastShowSuccess.ShowToastMessage(requireActivity(), 1, "Add product successfully!!!");
                } else if (resource.status == Resource.Status.ERROR) {
                    CustomToast toastShowError = new CustomToast();
                    toastShowError.ShowToastMessage(requireActivity(), 2, resource.message);
                }
            }
        });

    }

    private void requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void OpenThumbnailGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        thumbnailLauncher.launch(intent);
    }

    private void processSelectedImage(Uri imageUri) {
        SlideAdapter.addItem(new ItemModel(imageUri, null));
    }

    private void handleMultipleImages(Intent data) {
        ClipData clipData = data.getClipData();
        List<ItemModel> list = new ArrayList<>();
        if (clipData != null) {
            int count = Math.min(clipData.getItemCount(), 3);
            for (int i = 0; i < count; i++) {
                Uri imageUri = clipData.getItemAt(i).getUri();
                list.add(new ItemModel(imageUri, null));
            }
            SlideAdapter.renewItems(list);
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            // For file URIs, simply extract the file name
            fileName = uri.getLastPathSegment();
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            // For content URIs, query the content resolver to get the file name
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        }
        return fileName;
    }


    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Adding Product...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

}
