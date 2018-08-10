package com.daxstyle.recipe.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daxstyle.recipe.R;

import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class Services {
    private static final String REST_SERVICE = "http://culinaryapi.daxstyle.com/";
    private Context mContext;
    private static Services mInstance = new Services();
    private static int mWaitingRequestCount = 0;
    private RetryPolicy timeoutPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    private RequestQueue mRequestQueue;


    public static synchronized Services getInstance() {
        return mInstance;
    }

    public interface OnFinishListener {
        public void onFinish(Object obj);
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

    public Response.ErrorListener volleyErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            try {
                int message = R.string.data_not_found;

                Util.showToast(mContext.getApplicationContext(), message);
            } catch (
                    Exception ex)

            {
                ex.printStackTrace();
            }
        }
    };

    public void sendPost(Context context, String surname, String birthDate, String gender, String emailAddress, String cellPhoneNumber, String insertDate, String name, OnFinishListener ofl, Boolean showErrors) {
        mContext = context;

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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, REST_SERVICE, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ofl.onFinish(response);

            }
        }, showErrors ? volleyErrorListener : null);


        add(request);
    }

}
