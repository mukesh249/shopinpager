package shoppingpager.wingstud.shopinpager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.ActivitySuccessBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Success extends AppCompatActivity implements WebCompleteTask {

    private ActivitySuccessBinding successBinding;
    String order_id="",order_no="",method="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        successBinding = DataBindingUtil.setContentView(this,R.layout.activity_success);


        if (getIntent().getExtras()!=null){
            order_id = getIntent().getExtras().getString("order_id","");
            order_no = getIntent().getExtras().getString("order_no","");
            method = getIntent().getExtras().getString("method","");
            successBinding.orderIdTv.setText(order_no);

            if (method.equalsIgnoreCase("cod")) {
                CodSuccess(WebUrls.UserCodSuccess);
            }else if (method.equalsIgnoreCase("razorpaySuccess")){
                CodSuccess(WebUrls.UserRazorpaySuccess);
            }else if (method.equalsIgnoreCase("razorpayFailed")){
                CodSuccess(WebUrls.UserRazorpayFailed);
            }
        }

        successBinding.continueshopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Success.this,HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Success.this,HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    public void CodSuccess(String url){
        successBinding.waitTv.setVisibility(View.VISIBLE);
        successBinding.successLL.setVisibility(View.GONE);
        successBinding.matrialProgress.setVisibility(View.VISIBLE);
        HashMap obejectNew = new HashMap();
        obejectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        obejectNew.put("order_id",order_id);
        new WebTask(this, WebUrls.BASE_URL+url,obejectNew,Success.this, RequestCode.CODE_UserCodSuccess,5);
    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_UserCodSuccess==taskcode){
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code")==1){
                    if (method.equalsIgnoreCase("cod")) {
                        successBinding.orderIv.setBackground(getResources().getDrawable(R.drawable.success));
                        successBinding.orderStatusTv.setText("Success Order");
                        successBinding.orderdesTv.setText("Your order placed succesfully for more details check my order page your Order ID is ");
                    }else if (method.equalsIgnoreCase("razorpaySuccess")){
                        successBinding.orderIv.setBackground(getResources().getDrawable(R.drawable.success));
                        successBinding.orderStatusTv.setText("Success Order");
                    }else if (method.equalsIgnoreCase("razorpayFailed")){
                        successBinding.orderIv.setBackground(getResources().getDrawable(R.drawable.error));
                        successBinding.orderStatusTv.setText("Order Failed");
                        successBinding.orderdesTv.setText("Your order not placed for more details check my order page your Order ID is ");
                    }
                    successBinding.successLL.setVisibility(View.VISIBLE);
                    successBinding.matrialProgress.setVisibility(View.GONE);
                    successBinding.waitTv.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
