package com.example.ecommerce_hvpp.fragments.admin_fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.util.NumberInputFilter;
import com.example.ecommerce_hvpp.util.Resource;
import com.example.ecommerce_hvpp.viewmodel.admin.admin_product_management.AdminProductDetailsViewModel;
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
    private ImageView btnBack, btnAddSizeXL, btnAddSizeL, btnAddSizeM;
    private AppCompatButton btnAddMoreImages;
    private SliderView sliderView;
    private String Id;
    private TextView tvThumbnailImage;
    private EditText etName, etPrice, etDescription, etSizeXL, etSizeL, etSizeM, etNumXL, etNumL, etNumM, etTypeName;
    private TextView tvTypeName;
    private Button btnSave, btnCancel;
    private Spinner spType, spSeason;
    private Uri thumbnailImage;
    private List<EditText> editTextList;
    private boolean isFieldsModified;
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
        etSizeL = view.findViewById(R.id.etSizeL);
        etNumL = view.findViewById(R.id.etNumL);
        etNumM = view.findViewById(R.id.etNumM);
        etNumXL = view.findViewById(R.id.etNumXL);
        etSizeXL = view.findViewById(R.id.etSizeXL);
        etSizeM = view.findViewById(R.id.etSizeM);
        etTypeName = view.findViewById(R.id.etTypeName);
        tvTypeName = view.findViewById(R.id.tvHeaderForField);

        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnAddSizeXL = view.findViewById(R.id.btnAddSizeXL);
        btnAddSizeL = view.findViewById(R.id.btnAddSizeL);
        btnAddSizeM = view.findViewById(R.id.btnAddSizeM);


        editTextList = new ArrayList<>();
        editTextList.add(etName);
        editTextList.add(etPrice);
        editTextList.add(etDescription);
        editTextList.add(etSizeL);
        editTextList.add(etSizeXL);
        editTextList.add(etSizeM);
        editTextList.add(etSizeM);
        editTextList.add(etTypeName);
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

        etPrice.setFilters(new InputFilter[]{new NumberInputFilter()});
        etNumL.setFilters(new InputFilter[]{new NumberInputFilter()});
        etNumXL.setFilters(new InputFilter[]{new NumberInputFilter()});
        etNumM.setFilters(new InputFilter[]{new NumberInputFilter()});
        etSizeM.setFilters(new InputFilter[]{new NumberInputFilter()});
        etSizeL.setFilters(new InputFilter[]{new NumberInputFilter()});
        etSizeXL.setFilters(new InputFilter[]{new NumberInputFilter()});


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
                    //getData
                    implementEditFunctionality();
                } else {
                    implementAddFunctionality();
                }
            }
        });

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                tvTypeName.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothin
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
                if(isFieldsModified) {
                    showUnsavedChangesDialog();
                    return;
                }
                navController.popBackStack();
            }
        });
    }

    public void implementEditFunctionality() {
        isFieldsModified = false;
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
                    etPrice.setText(String.valueOf(resource.data.getPrice()));
                    etSizeM.setText(String.valueOf(resource.data.getSizeM()));
                    etSizeXL.setText(String.valueOf(resource.data.getSizeXL()));
                    etSizeL.setText(String.valueOf(resource.data.getSizeL()));
                    etDescription.setText(resource.data.getDescription());
                    if(resource.data.getClub() == "") {
                        spType.setSelection(TypeAdapter.getPosition("Nation"));
                        etTypeName.setText(resource.data.getNation());
                    }
                    else {
                        spType.setSelection(TypeAdapter.getPosition("Club"));
                        etTypeName.setText(resource.data.getClub());
                    }
                    spSeason.setSelection(SeasonAdapter.getPosition(resource.data.getSeason()));
                    tvThumbnailImage.setText(resource.data.getUrlthumb());
                    SlideAdapter.addItem(new ItemModel(null, resource.data.getUrlmain()));
                    SlideAdapter.addItem(new ItemModel(null, resource.data.getUrlsub1()));
                    SlideAdapter.addItem(new ItemModel(null, resource.data.getUrlsub2()));


                    setTextWatcherToEditTextFields();
                    break;
            }
            btnAddSizeM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!etNumM.getText().toString().trim().equals("")) {
                        etSizeM.setText(String.valueOf(Integer.parseInt(etNumM.getText().toString()) + Integer.parseInt(etSizeM.getText().toString())));
                        etNumM.setText("");
                    }

                }
            });

            btnAddSizeL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!etNumL.getText().toString().trim().equals("")) {
                        etSizeL.setText(String.valueOf(Integer.parseInt(etNumL.getText().toString()) + Integer.parseInt(etSizeL.getText().toString())));
                        etNumL.setText("");
                    }
                }
            });

            btnAddSizeXL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!etNumXL.getText().toString().trim().equals("")) {
                        etSizeXL.setText(String.valueOf(Integer.parseInt(etNumXL.getText().toString()) + Integer.parseInt(etSizeXL.getText().toString())));
                        etNumXL.setText("");
                    }
                }
            });


            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String Name, Type, Season, Price, Description, XL, L, M;
                    Name = etName.getText().toString();
                    Type = spType.getSelectedItem().toString();
                    Season = spSeason.getSelectedItem().toString();
                    Price = etPrice.getText().toString();
                    Description = etDescription.getText().toString();
                    XL = etSizeXL.getText().toString();
                    L = etSizeL.getText().toString();
                    M = etSizeM.getText().toString();


                    Product pd = new Product(Id, Name, Season, Price, Description, Integer.parseInt(XL), Integer.parseInt(L), Integer.parseInt(M));

                    if(Type.equals("Club")) {
                        pd.setClub(etTypeName.getText().toString());
                    } else {
                        pd.setNation(etTypeName.getText().toString());
                    }

                    pd.setUrlthumb(tvThumbnailImage.getText().toString());
                    pd.setUrlmain(SlideAdapter.getList().get(0).getLink());
                    pd.setUrlsub1(SlideAdapter.getList().get(1).getLink());
                    pd.setUrlsub2(SlideAdapter.getList().get(2).getLink());
                    editFunc_saveProduct(pd);
                    isFieldsModified = false;
                }
            });
        });
    }
    public void implementAddFunctionality() {
        btnCancel.setVisibility(View.GONE);
        etNumXL.setVisibility(View.GONE);
        etNumL.setVisibility(View.GONE);
        etNumM.setVisibility(View.GONE);

        btnAddSizeXL.setVisibility(View.GONE);
        btnAddSizeL.setVisibility(View.GONE);
        btnAddSizeM.setVisibility(View.GONE);

        if(spType.getSelectedItem().toString().equals("Club")) {
            tvTypeName.setText("Club Name");
        } else {
            tvTypeName.setText("Nation");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIfIsEmpty()) {
                    CustomToast validationToast = new CustomToast();
                    validationToast.ShowToastMessage(requireActivity(), 3, "There are fields which have no data! Please check and retry!");
                } else {
                    addFunc_addProduct();
                }
            }
        });
    }

    //region sub methods
    private void editFunc_saveProduct(Product pd) {
        viewModel.editProduct(pd).observe(getViewLifecycleOwner(), resource -> {
            if(resource.status == Resource.Status.LOADING) {
                showProgressDialog(false);
            } else {
                hideProgressDialog();

                if(resource.status == Resource.Status.SUCCESS) {
                    CustomToast toastShowSuccess = new CustomToast();
                    toastShowSuccess.ShowToastMessage(requireActivity(), 1, "Product has been updated successfully!");
                } else if (resource.status == Resource.Status.ERROR) {
                    CustomToast toastShowError = new CustomToast();
                    toastShowError.ShowToastMessage(requireActivity(), 2, resource.message);
                }
            }
        });
    }

    private void setTextWatcherToEditTextFields() {
        for (EditText editText : editTextList) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // No implementation needed
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    isFieldsModified = true;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // No implementation needed
                }
            });
        }
    }

    public void addFunc_addProduct() {
        String Name, Type, Season, Price, Description, XL, L, M;
        Name = etName.getText().toString();
        Type = spType.getSelectedItem().toString();
        Season = spSeason.getSelectedItem().toString();
        Price = etPrice.getText().toString();
        Description = etDescription.getText().toString();
        XL = etSizeXL.getText().toString();
        L = etSizeL.getText().toString();
        M = etSizeM.getText().toString();


        Product pd = new Product(Name, Season, Price, Description, Integer.parseInt(XL), Integer.parseInt(L), Integer.parseInt(M));
        if(Type.equals("Club")) {
            pd.setClub(etTypeName.getText().toString());
        } else {
            pd.setNation(etTypeName.getText().toString());
        }

        viewModel.addProduct(contentResolver, pd, thumbnailImage , SlideAdapter.getList());

        viewModel.getCombinedLiveData().observe(getViewLifecycleOwner(), resource -> {
            if(resource.status == Resource.Status.LOADING) {
                showProgressDialog(true);
            } else {
                hideProgressDialog();

                if(resource.status == Resource.Status.SUCCESS) {
                    CustomToast toastShowSuccess = new CustomToast();
                    toastShowSuccess.ShowToastMessage(requireActivity(), 1, "Add product successfully!!!");
                    Reset();
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

    private void showUnsavedChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Unsaved Changes");
        builder.setMessage("You have made changes. Do you want to discard them?");

        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                navController.popBackStack();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Stay on the current fragment
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showProgressDialog(boolean isAddFunc) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(isAddFunc ? "Adding Product..." : "Saving Product...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void Reset() {
        etName.setText("");
        etPrice.setText("");
        etDescription.setText("");
        tvThumbnailImage.setText("");
        etTypeName.setText("");
        etSizeM.setText("");
        etSizeL.setText("");
        etSizeXL.setText("");
        SlideAdapter.renewItems(null);
    }

    private boolean checkIfIsEmpty() {
        if (TextUtils.isEmpty(etName.getText().toString().trim())
        || TextUtils.isEmpty(etPrice.getText().toString().trim())
        || TextUtils.isEmpty(etDescription.getText().toString().trim())
        || TextUtils.isEmpty(tvThumbnailImage.getText().toString().trim())
        || TextUtils.isEmpty(etTypeName.getText().toString().trim())
        || TextUtils.isEmpty(etSizeM.getText().toString().trim())
        || TextUtils.isEmpty(etSizeL.getText().toString().trim())
        || TextUtils.isEmpty(etSizeXL.getText().toString().trim())
        || SlideAdapter.getList().size() == 0) {
            return true;
        }
        return false;
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

        if (isFieldsModified) {
            showUnsavedChangesDialog();
        }

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
    //endregion

}
