package com.daxstyle.recipe.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.daxstyle.recipe.R;
import com.daxstyle.recipe.helper.Util;
import com.daxstyle.recipe.model.CardModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CulinaryActivity extends AppCompatActivity {
    EditText etTitle, etDirection, etIngredient;
    Uri uri, photoUri;
    TextView tvIngredients, tvDirections, tvMinute, tvMinute2, tvServe, tvPrep;
    static ArrayList<CardModel> cardModels;
    Spinner spnTime, spnTime2;
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 1887;
    protected static final String[] requiredPermissions;
    private static final int PERMISSION_REQUEST = 0;
    int x = 0, count = 0;
    Bitmap bitmap;
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

                Log.d("onCreate: ", imagesUri.get(i));
            }
            Util.getPrefInt(this, "count");
        }

    }

    public void back(View view) {
        startActivity(new Intent(this, MainActivity.class));
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
        startDialog();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String stringUri;
        ImageView ivNumber = findViewById(getResources().getIdentifier("ivReport" + tag, "id", this.getPackageName()));
        TextView tvNumber = findViewById(getResources().getIdentifier("tvReport" + tag, "id", this.getPackageName()));
        tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesUri.remove(ivNumber.getTag());
                if (tag == 1) {
                    ivNumber.setImageResource(R.drawable.ic_camera);
                } else {
                    ivNumber.setImageResource(R.drawable.ic_addimage);
                }
                tvNumber.setVisibility(View.GONE);

            }
        });
       
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                photoUri = Uri.parse(String.valueOf(photoUri));
                stringUri = photoUri.toString();
                imagesUri.add(stringUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photo != null) {
                ivNumber.setImageBitmap(photo);
                tvNumber.setVisibility(View.VISIBLE);
                ivNumber.setClickable(false);
            }
        }
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            final String path = getPathFromURI(selectedImage);
            if (path != null) {
                File f = new File(path);
                uri = Uri.fromFile(f);
                bitmap = BitmapFactory.decodeFile(getPathFromURI(selectedImage));
                stringUri = uri.toString();
                imagesUri.add(stringUri);
            }
            if (bitmap != null) {
                ivNumber.setImageBitmap(bitmap);
                tvNumber.setVisibility(View.VISIBLE);
                ivNumber.setClickable(false);
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
        }
        startActivity(new Intent(this, MainActivity.class));
    }

    private void startDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(R.string.choose);
        verifyPermissions();
        builder.setPositiveButton(R.string.gallery,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, GALLERY_REQUEST);

                    }
                });

        builder.setNegativeButton(R.string.camera,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Util.getPrefInt(getApplicationContext(), "count");
                        Log.d("onClick: ", String.valueOf(Util.getPrefInt(getApplicationContext(), "count")));
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        File output = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Culinary" + count + ".jpg");
                        photoUri = Uri.fromFile(output);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                photoUri);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        count++;
                        if (count == 100) {
                            count = 0;
                        }
                        Util.setPrefInt(getApplicationContext(), "count", count);
                    }
                });
        builder.show();
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
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
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
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ));
        requiredPermissions = perms.toArray(new String[perms.size()]);
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}



