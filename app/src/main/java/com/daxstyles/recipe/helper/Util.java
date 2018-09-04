package com.daxstyles.recipe.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.daxstyles.recipe.R;
import com.daxstyles.recipe.model.CardModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Util {
    public static void showToast(Context context, int message) {
        showToast(context, context.getString(message));
    }

    private static void showToast(Context context, String message) {
        Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        t.show();
    }

    private static SecurePreferences getPrefClass(Context context) {
        return new SecurePreferences(context, "app", "M8d3#sfsdasd13,F3s42fNs*qwS1mn2sFRs58g2", true);
    }

    public static String getPrefString(Context context, String key, String defaultVal) {
        return getPrefClass(context).getString(key, defaultVal);
    }

    public static void setPrefString(Context context, String key, String val) {
        getPrefClass(context).put(key, val);
    }

    public static boolean getPrefBoolean(Context c, String s) {
        return Boolean.parseBoolean(getPrefString(c, s, "false"));
    }

    public static int getPrefInt(Context c, String s) {
        return Integer.parseInt(getPrefString(c, s, "-1"));
    }

    public static long getPrefLong(Context c, String s) {
        return Long.parseLong(getPrefString(c, s, "-1"));
    }

    public static double getPrefDouble(Context c, String s) {
        return Double.parseDouble(getPrefString(c, s, "-1"));
    }

    public static float getPrefFloat(Context c, String s) {
        return Float.parseFloat(getPrefString(c, s, "-1"));
    }

    public static void setPrefBoolean(Context c, String s, boolean val) {
        setPrefString(c, s, Boolean.toString(val));
    }

    public static void setPrefInt(Context c, String s, int val) {
        setPrefString(c, s, Integer.toString(val));
    }

    public static void setPrefLong(Context c, String s, long val) {
        setPrefString(c, s, Long.toString(val));
    }

    public static void setPrefDouble(Context c, String s, double val) {
        setPrefString(c, s, Double.toString(val));
    }

    public static void setPrefFloat(Context c, String s, float val) {
        setPrefString(c, s, Float.toString(val));
    }

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //progressDialog.setContentView(R.layout.progress_dialog);
        return progressDialog;
    }
    public static void startProgressAnimation(ProgressDialog pd) {
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        AnimationDrawable ad = (AnimationDrawable) ((ImageView) pd.findViewById(R.id.ivLoading)).getBackground();
        ad.start();
    }
    //endregion
    public static void saveObject(Context context, Object obj, String fileName) {
        if (checkExternalStorage()) {
            try {
                FileOutputStream fos;
                fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(obj);
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean checkExternalStorage() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        return (mExternalStorageAvailable && mExternalStorageWriteable);
    }

    public static Object loadObject(Context context, String fileName) {
        Object obj = null;
        try {
            if (isFileExists(context, fileName)) {
                FileInputStream fis = context.openFileInput(fileName);
                ObjectInputStream is = new ObjectInputStream(fis);
                obj = is.readObject();
                is.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static boolean isFileExists(Context c, String fileName) {
        return c.getFileStreamPath(fileName).isFile();
    }

    public static int getNavigationBarSize(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static boolean hasNavigationBar(Context context) {
        Resources resources = context.getResources();
        int hasNavBarId = resources.getIdentifier("config_showNavigationBar",
                "bool", "android");
        return hasNavBarId > 0 && resources.getBoolean(hasNavBarId);
    }

    public static void setSoftKeys(Activity context) {
        try {
            if (Util.hasNavigationBar(context)) {
                ViewGroup pageContainer = context.findViewById(R.id.pageContainer);
                if (pageContainer != null) {
                    int size = Util.getNavigationBarSize(context);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) pageContainer.getLayoutParams();
                    params.setMargins(0, 0, 0, size); //substitute parameters for left, top, right, bottom
                    pageContainer.setLayoutParams(params);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ArrayList<CardModel> loadCards(Context context, ArrayList<CardModel> cardModels) {
        cardModels = new ArrayList<>();

        try {
            Object cards = Util.loadObject(context, "CardModels.obj");

            if (cards != null)
                cardModels = (ArrayList<CardModel>) cards;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cardModels;
    }

    public static ArrayList<String> loadUri(Context context, ArrayList<String> cardModels) {
        cardModels = new ArrayList<>();

        try {
            Object cards = Util.loadObject(context, "Uri.obj");

            if (cards != null)
                cardModels = (ArrayList<String>) cards;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cardModels;
    }
}
