package shoppingpager.wingstud.shopinpager.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.databinding.ObservableField;

import shoppingpager.wingstud.shopinpager.activities.HomeScreen;
import shoppingpager.wingstud.shopinpager.activities.Login;
import shoppingpager.wingstud.shopinpager.activities.OtpScreen;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Observable;

public class OtpViewModel extends Observable implements WebCompleteTask {

    private Context context;
    public final ObservableField<String> pincode = new ObservableField<>("");
//    public final ObservableField<String> pincodeString = new ObservableField<>("");


    public OtpViewModel(Context context) {
        this.context = context;
    }

    public void ResendOtp(String mobile){
        HashMap objectNew = new HashMap();
        objectNew.put("mobile",mobile);
        Log.i("ResendOtp_obj",objectNew+"");
        new WebTask(context, WebUrls.BASE_URL+WebUrls.ReSendOtpApi,objectNew, OtpViewModel.this, RequestCode.CODE_ResendOtp,1);
    }
    public void OtpVerifiyRequest(String Mobile, String fname, String lname, String Email, String ReferralCode){
        HashMap objectNew = new HashMap();
        objectNew.put("mobile",Mobile);
        objectNew.put("f_name",fname);
        objectNew.put("l_name",lname);
        objectNew.put("email",Email);
        objectNew.put("reff_code",ReferralCode);
        Log.i("OtpVerifiy_obj",objectNew+"");
        new WebTask(context, WebUrls.BASE_URL+WebUrls.VerifiyOtpApi,objectNew,OtpViewModel.this, RequestCode.CODE_OtpVerifiy,1);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_ResendOtp==taskcode) {
            Log.i("Resend_res", response);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code") == 1) {
                    OtpScreen.otp = jsonObject.optString("otp_code");
                    Utils.Toast(context,jsonObject.optString("message"));
                    OtpScreen.getInstance().counterMethod();
                }else {
                    Utils.Toast(context,jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (RequestCode.CODE_OtpVerifiy==taskcode) {
            Log.i("OtpVerifiy_res", response);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code") == 1) {

//                    JSONObject dataArray = jsonObject.optJSONObject("data");
//                    SharedPrefManager.setLogin(Constrants.IsLogin,true);
//                    SharedPrefManager.setUserName(Constrants.UserName, dataArray.optString("username"));
//                    SharedPrefManager.setUserEmail(Constrants.UserEmail,dataArray.optString("email" ));
//                    SharedPrefManager.setUserPic(Constrants.UserPic, dataArray.optJSONObject("user_kyc").optString("profile_image"));

                    context.startActivity(new Intent(context, Login.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    ((Activity)context).finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
