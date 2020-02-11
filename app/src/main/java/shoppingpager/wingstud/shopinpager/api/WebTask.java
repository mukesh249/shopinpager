package shoppingpager.wingstud.shopinpager.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.common.Network;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by suarebits on 3/12/15.
 */
public class WebTask {

    String url;
    HashMap<String, String> params;
    WebCompleteTask webCompleteTask;
    int taskcode;
    Context context;
    Context contextnet;
    JSONObject object;
    boolean isProgress = true;
    int mathod;
    ProgressDialog progressDialog = null;


    public WebTask(Context context, String url, HashMap<String, String> params, WebCompleteTask webCompleteTask, int taskcode, int mathod) {
        this.url = url;
        this.params = params;
        this.webCompleteTask = webCompleteTask;
        this.taskcode = taskcode;
        this.context = context;
        this.mathod = mathod;
        progressDialog = new ProgressDialog(context);
        Network.activityname = "webview";
//        session = new_image SessionManagement(context);
        //registerReceiver(myReceiver, new_image IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


//        if (mathod==1){
//            volleyStringRequest();
//        }
//        else  if (mathod==2){
//            volleyStringRequestLogOut();
//        }
//        else {
//            volleyStringRequestGet();
//        }

        try {
            if (!Network.isConnectingToInternet(context)) {
                SharedPrefManager.showMessage(context, context.getString(R.string.network_error_msg));
                return;
            } else {
                SharedPrefManager.getInstance(context).hideSoftKeyBoard((Activity) context);
                if (mathod == 1) {
                    volleyStringRequest();
                } else if (mathod == 2) {
                    volleyStringRequestLogOut();
                } else if (mathod == 3) {
                    volleyStringRequestGetWithoutProgressBar();
                } else if (mathod == 0) {
                    volleyStringRequestGet();
                } else if (mathod == 5) {
                    volleyStringRequestwithoutProgressBar();
                } else {
                    volleyStringRequestGetLn();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public WebTask(Context context, String url, HashMap<String, String> params, WebCompleteTask webCompleteTask, int taskcode, boolean isProgress) {
        this.url = url;
        this.params = params;
        this.webCompleteTask = webCompleteTask;
        this.taskcode = taskcode;
        this.context = context;
        this.isProgress = isProgress;
        progressDialog = new ProgressDialog(context);
        volleyStringRequest();

       /* if (!MyApplication.getInstance().isConnectingToInternet(context)) {
            MyApplication.showMessage(context, context.getString(R.string.internet_issue));
            return;
        } else {
            MyApplication.getInstance().hideSoftKeyBoard((Activity) context);

            volleyStringRequest();

        }*/

    }

    public WebTask(Context context, String url, JSONObject object, WebCompleteTask webCompleteTask, int taskcode, int mathod) {
        this.url = url;
        this.object = object;
        this.webCompleteTask = webCompleteTask;
        this.taskcode = taskcode;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        try {
            if (!Network.isConnectingToInternet(context)) {
                SharedPrefManager.showMessage(context, context.getString(R.string.network_error_msg));
                return;
            } else {
                SharedPrefManager.getInstance(context).hideSoftKeyBoard((Activity) context);
                if (mathod == 1) {
                    volleyStringRequestObject();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void volleyStringRequestwithoutProgressBar() {
//        if (isProgress) {
//            progressDialog = ProgressDialog.show(context, "",  context.getString(R.string.api_hiting));
//            progressDialog.setCancelable(true);
//        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                if (progressDialog != null)
//                    progressDialog.dismiss();
                webCompleteTask.onComplete(response, taskcode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (progressDialog != null)
//                    progressDialog.dismiss();
                try {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    } else {
                        String result = new String(networkResponse.data);
                        Log.d("error response", result);
                        try {
                            JSONObject response = new JSONObject(result);
                            JSONObject errorObje = response.getJSONObject("error");
                            String status = errorObje.getString("statusCode");
                            String message = errorObje.getString("message");
                            errorMessage = message;

//                            JSONObject response = new_image JSONObject(result);
//                            JSONObject jobj=new_image JSONObject(response.getString("error"));
//
//                            String message = jobj.getString("message");
//                            String status= jobj.getString("statusCode");
                            if (status.compareTo("401") == 0) {
                                if (message.equals("Incorrect password")) {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                } else if (message.equals("Account does not exist")) {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                } else {
//                                    Toast.makeText(context,message, Toast.LENGTH_LONG).show();
//                                    Intent i = new_image Intent(context, Login.class);
//                                    session.logoutUser();
//                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    context.startActivity(i);
//                                    ((Activity)context).finish();
                                }
                            } else {
                                Log.e("Error Message", message);
//                                Toast.makeText(context,message, Toast.LENGTH_LONG).show();
                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " 401 Please login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message;
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                }
                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    Log.i("Error response", new_image String(error.networkResponse.data));
                    error.printStackTrace();
                } finally {

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header_param = new HashMap<>();
//                header_param.put("Authorization", SharedPrefManager.getInstance(context).getAccessToken());
//                header_param.put("ln", SharedPrefManager.getLangId(context, RequestCode.LangId));
                return header_param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public void volleyStringRequestObject() {
        if (isProgress) {
            progressDialog = ProgressDialog.show(context, "",  context.getString(R.string.api_hiting));
            progressDialog.setCancelable(true);
        }
        /*cvfdbvfdb*/
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                webCompleteTask.onComplete(response + "", taskcode);
                //parseDirectionsData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                if (progressDialog != null)
                    progressDialog.dismiss();
                try {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {
                        String result = new String(networkResponse.data);
                        Log.d("error response", result);
//                        try {
//                            JSONObject response = new_image JSONObject(result);
//                            JSONObject errorObje = response.getJSONObject("error");
//                            String status = errorObje.getString("statusCode");
//                            String message = errorObje.getString("message");
//                            errorMessage = message;
//
//                            if (status.compareTo("401") == 0) {
////                                if (message.equals("Incorrect password")) {
////
////                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
////                                } else if (message.equals("Account does not exist")) {
////                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
////                                } else {
////                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
////                                    Intent i = new_image Intent(context, Login.class);
////                                    session.logoutUser();
////                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
////                                    context.startActivity(i);
////                                    ((Activity)context).finish();
////                                }
//                            } else {
//                                Log.e("Error Message", message);
////                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//                                if (networkResponse.statusCode == 404) {
//                                    errorMessage = "Resource not found";
//                                } else if (networkResponse.statusCode == 401) {
//                                    errorMessage = message + " 401 Please login again";
//                                } else if (networkResponse.statusCode == 400) {
//                                    errorMessage = message;
//                                } else if (networkResponse.statusCode == 500) {
//                                    errorMessage = message + " Something is getting wrong";
//                                }
//                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
//                    Log.i("Error response", new_image String(error.networkResponse.data));
                    error.printStackTrace();
                } finally {

                }


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header_param = new HashMap<>();
//                header_param.put("Authorization", SharedPrefManager.getInstance(context).getAccessToken());
//                header_param.put("ln", SharedPrefManager.getLangId(context, Constrants.UserLang));
                return header_param;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);


    }

    public void volleyJsonRequest() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, "", context.getString(R.string.api_hiting));
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            webCompleteTask.onComplete(response.toString(), taskcode);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }
        );
        Volley.newRequestQueue(context).add(jsObjRequest);
    }

    public void volleyStringRequest() {
        if (isProgress) {
            progressDialog = ProgressDialog.show(context, "",  context.getString(R.string.api_hiting));
            progressDialog.setCancelable(false);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                webCompleteTask.onComplete(response, taskcode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null)
                    progressDialog.dismiss();

//                try {
//                    NetworkResponse networkResponse = error.networkResponse;
//                    String errorMessage = "Unknown error";
//                    if (networkResponse == null) {
//                        if (error.getClass().equals(TimeoutError.class)) {
//                            errorMessage = "Request timeout";
//                        } else if (error.getClass().equals(NoConnectionError.class)) {
//                            errorMessage = "Failed to connect server";
//                        }
//                        Toast.makeText(context, "" + errorMessage, Toast.LENGTH_LONG).show();
//                        Log.e("failedTTOOO", String.valueOf(error));
//                    } else {
//                        String result = new_image String(networkResponse.data);
//                        Log.d("error response", result);
//                        try {
//                            JSONObject response = new_image JSONObject(result);
//                            JSONObject errorObje = response.getJSONObject("error");
//                            String status = errorObje.getString("statusCode");
//                            String message = errorObje.getString("message");
//                            errorMessage = message;
//
////                            JSONObject response = new_image JSONObject(result);
////                            JSONObject jobj=new_image JSONObject(response.getString("error"));
////
////                            String message = jobj.getString("message");
////                            String status= jobj.getString("statusCode");
//                            if (status.compareTo("401") == 0) {
//                                if (message.equals("Incorrect password")) {
//                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//                                } else if (message.equals("Account does not exist")) {
//                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//                                } else {
////                                    Toast.makeText(context,message, Toast.LENGTH_LONG).show();
////                                    Intent i = new_image Intent(context, Login.class);
////                                    session.logoutUser();
////                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
////                                    context.startActivity(i);
////                                    ((Activity)context).finish();
//                                }
//                            } else {
//                                Log.e("Error Message", message);
////                                Toast.makeText(context,message, Toast.LENGTH_LONG).show();
//                                if (networkResponse.statusCode == 404) {
//                                    errorMessage = "Resource not found";
//                                } else if (networkResponse.statusCode == 401) {
//                                    errorMessage = message + " 401 Please login again";
//                                } else if (networkResponse.statusCode == 400) {
//                                    errorMessage = message + " Check your inputs";
//                                } else if (networkResponse.statusCode == 500) {
//                                    errorMessage = message + " Something is getting wrong";
//                                }
//                            }
//                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
////                    Log.i("Error response", new_image String(error.networkResponse.data));
//                    error.printStackTrace();
//                } finally {
//
//                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header_param = new HashMap<>();
//                header_param.put("Authorization", SharedPrefManager.getInstance(context).getAccessToken());
//                header_param.put("ln", SharedPrefManager.getLangId(context, RequestCode.LangId));
                return header_param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public void volleyStringRequestDelete() {
        if (isProgress) {
            progressDialog = ProgressDialog.show(context, "",  context.getString(R.string.api_hiting));
            progressDialog.setCancelable(true);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                webCompleteTask.onComplete(response, taskcode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                try {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    } else {

                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            JSONObject errorObje = response.getJSONObject("error");
                            String status = errorObje.getString("statusCode");
                            String message = errorObje.getString("message");
                            errorMessage = message;

                            if (status.compareTo("401") == 0) {
//                                Intent i = new_image Intent(context, Login.class);
//                                session.logoutUser();
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(i);
//                                ((Activity)context).finish();
                            } else {
                                Log.e("Error Message", message);
//                                Toast.makeText(context,message, Toast.LENGTH_LONG).show();
                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " 401 Please login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                }
                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    Log.i("Error", errorMessage);
                    error.printStackTrace();
                } finally {

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header_param = new HashMap<>();
                // header_param.put("Authorization", MyApplication.getInstance().getSid(Constants.AccessToken));
                return header_param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public void volleyStringRequestGet() {
        if (isProgress) {
            progressDialog = ProgressDialog.show(context, "",  context.getString(R.string.api_hiting));
            progressDialog.setCancelable(true);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                webCompleteTask.onComplete(response, taskcode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                try {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    } else {

                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            JSONObject errorObje = response.getJSONObject("error");
                            String status = errorObje.getString("statusCode");
                            String message = errorObje.getString("message");
                            errorMessage = message;
                            if (status.compareTo("401") == 0) {
//                                SharedPrefManager.showMessage(context, message);

//                                Intent i = new_image Intent(context, Login.class);
//                                session.logoutUser();
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(i);
//                                ((Activity)context).finish();
                            } else {
                                Log.e("Error Message", message);
                                //  Toast.makeText(context,message, Toast.LENGTH_LONG).show();
                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " 401 Please login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                }
                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    Log.i("Error", errorMessage);
                    error.printStackTrace();
                } finally {

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header_param = new HashMap<>();
//                header_param.put("Authorization", SharedPrefManager.getInstance(context).getAccessToken());
//                header_param.put("ln", SharedPrefManager.getLangId(context, RequestCode.LangId));
                return header_param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public void volleyStringRequestGetLn() {
        if (isProgress) {
            progressDialog = ProgressDialog.show(context, "",  context.getString(R.string.api_hiting));
            progressDialog.setCancelable(true);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                webCompleteTask.onComplete(response, taskcode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                try {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    } else {

                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            JSONObject errorObje = response.getJSONObject("error");
                            String status = errorObje.getString("statusCode");
                            String message = errorObje.getString("message");
                            errorMessage = message;
                            if (status.compareTo("401") == 0) {
//                                SharedPrefManager.showMessage(context, message);

//                                Intent i = new_image Intent(context, Login.class);
//                                session.logoutUser();
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(i);
//                                ((Activity)context).finish();
                            } else {
                                Log.e("Error Message", message);
//                                Toast.makeText(context,message, Toast.LENGTH_LONG).show();
                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " 401 Please login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                }
                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    Log.i("Error", errorMessage);
                    error.printStackTrace();
                } finally {

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header_param = new HashMap<>();
//                header_param.put("Authorization",SharedPrefManager.getInstance(context).getAccessToken());
//                header_param.put("ln", SharedPrefManager.getLangId(context, RequestCode.LangId));
                return header_param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(stringRequest);
    }


    public void volleyStringRequestGetWithoutProgressBar() {
//        if (isProgress) {
//            progressDialog = ProgressDialog.show(context, "",  context.getString(R.string.api_hiting));
//            progressDialog.setCancelable(true);
//        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                if (progressDialog != null)
//                    progressDialog.dismiss();
                webCompleteTask.onComplete(response, taskcode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (progressDialog != null)
//                    progressDialog.dismiss();
                try {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    } else {

                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            JSONObject errorObje = response.getJSONObject("error");
                            String status = errorObje.getString("statusCode");
                            String message = errorObje.getString("message");
                            errorMessage = message;
                            if (status.compareTo("401") == 0) {

//                                Intent i = new_image Intent(context, Login.class);
//                                session.logoutUser();
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(i);
//                                ((Activity)context).finish();

                            } else {
                                Log.e("Error Message", message);
//                                Toast.makeText(context,message, Toast.LENGTH_LONG).show();
                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " 401 Please login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                }
                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    Log.i("Error", errorMessage);
                    error.printStackTrace();
                } finally {

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header_param = new HashMap<>();
//                header_param.put("Authorization", SharedPrefManager.getInstance(context).getAccessToken());
//                header_param.put("ln", SharedPrefManager.getLangId(context, RequestCode.LangId));
                return header_param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public void volleyStringRequestLogOut() {
        if (isProgress) {
            progressDialog = ProgressDialog.show(context, "",  context.getString(R.string.api_hiting));
            progressDialog.setCancelable(true);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                webCompleteTask.onComplete(response, taskcode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                try {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {

                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            JSONObject jobj = new JSONObject(response.getString("error"));

                            String message = jobj.getString("message");
                            String status = jobj.getString("statusCode");
                            if (status.compareTo("401") == 0) {
//                                SharedPrefManager.showMessage(context, message);
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//                                Intent i = new_image Intent(context, Login.class);
//                                session.logoutUser();
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(i);
//                                ((Activity)context).finish();
                            } else {
                                Log.e("Error Message", message);
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " 401 Please login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    Log.i("Error", errorMessage);
                    error.printStackTrace();
                } finally {

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header_param = new HashMap<>();
//                header_param.put("Authorization", SharedPrefManager.getInstance(context).getRegPeopleId());
//                header_param.put("ln", SharedPrefManager.getLangId(context, RequestCode.LangId));
                return header_param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(stringRequest);
    }

}
