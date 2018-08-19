package com.daxstyle.recipe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.daxstyle.recipe.R;
import com.daxstyle.recipe.helper.Util;
import com.daxstyle.recipe.model.CardModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CulinaryActivity extends AppCompatActivity {
    EditText etTitle, etDirection, etIngredient;
    Uri photoUri;
    TextView tvIngredients, tvDirections, tvMinute, tvMinute2, tvServe, tvPrep;
    static ArrayList<CardModel> cardModels;
    Spinner spnTime, spnTime2;
    protected static final String[] requiredPermissions;
    private static final int PERMISSION_REQUEST = 0;
    int x = 0;
    ArrayList<String> imagesUri;
    int position;
    int tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culinary);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Lobster.otf");
        etTitle = findViewById(R.id.etTitle);
        etDirection = findViewById(R.id.etDirection);
        etIngredient = findViewById(R.id.etIngredient);
        spnTime = findViewById(R.id.spnTime);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvDirections = findViewById(R.id.tvDirections);
        tvMinute = findViewById(R.id.tvMinute);
        tvMinute2 = findViewById(R.id.tvMinute2);
        tvPrep = findViewById(R.id.tvPrep);
        tvServe = findViewById(R.id.tvServe);
        tvDirections.setTypeface(type);
        tvMinute.setTypeface(type);
        tvMinute2.setTypeface(type);
        tvPrep.setTypeface(type);
        tvServe.setTypeface(type);
        tvIngredients.setTypeface(type);
        spnTime2 = findViewById(R.id.spnTime2);
        if (imagesUri == null) {
            imagesUri = new ArrayList<>();
        } else {
            imagesUri = Util.loadUri(this, imagesUri);
        }
        Util.setSoftKeys(this);
        spnAdapter();
        cardModels = Util.loadCards(this, cardModels);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("count");
            etTitle.setText(extras.getString("title"));
            etDirection.setText(extras.getString("directions"));
            etIngredient.setText(extras.getString("ingredients"));
            spnTime.setSelection(extras.getInt("prepTime"));
            spnTime2.setSelection(extras.getInt("serveTime"));
            imagesUri = (ArrayList<String>) extras.getSerializable("uris");
            for (int i = 0; i < imagesUri.size(); i++) {
                Uri uri = Uri.parse(imagesUri.get(i));
                Bitmap photo = null;
                try {
                    photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (x == 5 || x < 0) {
                    x = 0;
                }
                x++;
                ImageView iwNumber = findViewById(getResources().getIdentifier("ivReport" + String.valueOf(x), "id", this.getPackageName()));
                if (photo != null) {
                    iwNumber.setImageBitmap(photo);
                    iwNumber.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
            Util.getPrefInt(this, "count");
        }

    }
    public void spnAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTime.setAdapter(adapter);
        spnTime2.setAdapter(adapter);

    }
    public void addPhoto(View view) {
        tag = Integer.parseInt(view.getTag().toString());
        verifyPermissions();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String stringUri;
        ImageView ivNumber = findViewById(getResources().getIdentifier("ivReport" + tag, "id", this.getPackageName()));
        TextView tvNumber = findViewById(getResources().getIdentifier("tvReport" + tag, "id", this.getPackageName()));
        tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesUri.remove(ivNumber.getTag());
                ivNumber.setClickable(true);
//                if (tag == 1) {
//                    ivNumber.setImageResource(R.drawable.ic_camera);
//                } else {
                ivNumber.setImageResource(R.drawable.ic_addimage);
                tvNumber.setVisibility(View.GONE);
            }
        });
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    photoUri = result.getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    photoUri = Uri.parse(String.valueOf(photoUri));
                    stringUri = photoUri.toString();
                    imagesUri.add(stringUri);
                    if (bitmap != null) {
                        ivNumber.setImageBitmap(bitmap);
                        tvNumber.setVisibility(View.VISIBLE);
                        ivNumber.setClickable(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }

    }
    public void saveAll(View view) {
        if (getIntent().getBooleanExtra("fromEdit", false)) {
            cardModels.get(position).setDirection(etDirection.getText().toString());
            cardModels.get(position).setIngredient(etIngredient.getText().toString());
            cardModels.get(position).setPrepTime(spnTime.getSelectedItemPosition());
            cardModels.get(position).setServeTime(spnTime2.getSelectedItemPosition());
            cardModels.get(position).setTitle(etTitle.getText().toString());
            cardModels.get(position).setImagesUri(imagesUri);
            Util.saveObject(this, imagesUri, "Uri.obj");
            Util.saveObject(this, cardModels, "CardModels.obj");
            Util.showToast(this, R.string.saved);
        } else {
            if (etDirection.getText() != null && etIngredient.getText() != null && etTitle.getText() != null) {
                Util.loadCards(this, cardModels);
                CardModel cardModel = new CardModel();
                cardModel.setDirection(etDirection.getText().toString());
                cardModel.setIngredient(etIngredient.getText().toString());
                cardModel.setPrepTime(spnTime.getSelectedItemPosition());
                cardModel.setServeTime(spnTime2.getSelectedItemPosition());
                cardModel.setTitle(etTitle.getText().toString());
                cardModel.setImagesUri(imagesUri);
                cardModels.add(cardModel);
                Util.saveObject(this, imagesUri, "Uri.obj");
                Util.saveObject(this, cardModels, "CardModels.obj");
                Util.showToast(this, R.string.saved);
            } else {
                Util.showToast(this, R.string.error_desc);
            }
        }
        startActivity(new Intent(this, MainActivity.class));
    }
    private void verifyPermissions() {
        if (!hasAllPermissions()) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    requiredPermissions,
                    PERMISSION_REQUEST
            );
        }
    }
    private boolean hasAllPermissions() {
        // Check if we have all required permissions.
        for (String perm : requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }

        }
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (!hasAllPermissions()) {
                    finish();

                }
                return;
            }
        }
    }
    static {
        List<String> perms = new ArrayList<>(Arrays.asList(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        ));
        requiredPermissions = perms.toArray(new String[perms.size()]);
    }
}



