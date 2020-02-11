package shoppingpager.wingstud.shopinpager.viewmodel;

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

public class LoginViewModel extends Observable implements WebCompleteTask {

    private Context context;
    public final ObservableField<String> mobileno = new ObservableField<>("");
    public final ObservableField<String> password = new ObservableField<>("");

    public LoginViewModel(Context context) {
        this.context = context;
    }

    public void LoginRequest(String device_token){
        HashMap objectNew = new HashMap();
        objectNew.put("mobile",mobileno.get());
        objectNew.put("password",password.get());
        objectNew.put("device_token",device_token);
        Log.i("Login_obj",objectNew+"");
        new WebTask(context, WebUrls.BASE_URL+WebUrls.LoginApi,objectNew,LoginViewModel.this, RequestCode.CODE_Login,1);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Login == taskcode) {
            Log.i("Login_res", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code") == 1) {

                    JSONObject dataArray = jsonObject.optJSONObject("data");
                    SharedPrefManager.setLogin(Constrants.IsLogin,true);
                    SharedPrefManager.setUserName(Constrants.UserName, dataArray.optString("username"));
                    SharedPrefManager.setUserID(Constrants.UserId, dataArray.optString("id"));
                    SharedPrefManager.setUserEmail(Constrants.UserEmail,dataArray.optString("email" ));
                    SharedPrefManager.setUserMobile(Constrants.UserMobile,dataArray.optString("mobile" ));
                    SharedPrefManager.setreferCode(Constrants.ReferCode,dataArray.optString("reff_code"));
                    SharedPrefManager.setUserPic(Constrants.UserPic, dataArray.optJSONObject("user_kyc").optString("profile_image"));
                    SharedPrefManager.setUserFirstName(Constrants.UserFirstName, dataArray.optJSONObject("user_kyc").optString("f_name"));
                    SharedPrefManager.setUserLastName(Constrants.UserLastName, dataArray.optJSONObject("user_kyc").optString("l_name"));

                    context.startActivity(new Intent(context, HomeScreen.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK));
//                    finish();
                }else {
                    Utils.Toast(context,jsonObject.optString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
