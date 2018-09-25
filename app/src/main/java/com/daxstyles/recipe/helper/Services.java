package com.daxstyles.recipe.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;
import com.daxstyles.recipe.R;
import com.daxstyles.recipe.model.ResultModel;

import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class Services {
    private static final String REST_SERVICE = "https://daxstyle.com";
    private Context mContext;
    private static Services mInstance = new Services();
    private static int mWaitingRequestCount = 0;
    private RetryPolicy timeoutPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    private RequestQueue mRequestQueue;
    private ProgressDialog progressDialog;


    public static synchronized Services getInstance() {
        return mInstance;
    }

    public interface OnFinishListener {
        void onFinish(Object obj);
    }


    private void showProgress(Context context) {
        mContext = context;
        if (progressDialog == null) {
            createProgress();
        } else {
            if (!progressDialog.getContext().getClass().equals(mContext.getClass())) {
                progressDialog.dismiss();
                createProgress();
            } else
                progressDialog.show();
        }
        Util.startProgressAnimation(progressDialog);
    }

    private void createProgress() {
        progressDialog = Util.createProgressDialog(mContext);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelAllRequests();
            }
        });
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    public void cancelAllRequests() {
        mRequestQueue.cancelAll(TAG);
    }


    private RequestQueue.RequestFinishedListener<Object> mRequestFinishedListener = new RequestQueue.RequestFinishedListener<Object>() {
        @Override
        public void onRequestFinished(Request<Object> request) {
            mWaitingRequestCount--;
        }
    };

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
            mRequestQueue.addRequestFinishedListener(mRequestFinishedListener);
        }
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(timeoutPolicy);
        getRequestQueue().add(req);
        mWaitingRequestCount++;
    }


    public void sendPost(Context context, String surname, String birthDate, String gender, String emailAddress, String cellPhoneNumber, String insertDate, String name, OnFinishListener ofl, Response.ErrorListener errorListener) {
        showProgress(context);
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonObject.put("action", "addInfo");
            jsonBody.put("surname", surname);
            jsonBody.put("birthDate", birthDate);
            jsonBody.put("gender", gender);
            jsonBody.put("emailAddress", emailAddress);
            jsonBody.put("cellPhoneNumber", cellPhoneNumber);
            jsonBody.put("insertDate", insertDate);
            jsonBody.put("name", name);
            jsonObject.put("params", jsonBody);
        } catch (Exception e) {
            Log.e("hata", e.toString());
            e.printStackTrace();
        }
        GsonRequest<ResultModel> gsonRequest = new GsonRequest<>(Request.Method.POST, REST_SERVICE + "/", ResultModel.class, jsonObject, null, ofl::onFinish, errorListener);


        add(gsonRequest);
    }

}
