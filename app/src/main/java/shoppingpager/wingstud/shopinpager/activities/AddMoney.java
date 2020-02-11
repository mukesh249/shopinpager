package shoppingpager.wingstud.shopinpager.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.api.JsonDeserializer;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.ActivityAddMoneyBinding;
import shoppingpager.wingstud.shopinpager.model.GenerateCheckSumResponse;
import shoppingpager.wingstud.shopinpager.utils.Utils;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

public class AddMoney extends AppCompatActivity implements WebCompleteTask {

    int payment=0;
    private ActivityAddMoneyBinding binding;
    double amount_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_money);
        binding.headlyaout.searchIcon.setVisibility(View.GONE);
        binding.headlyaout.cartRL.setVisibility(View.GONE);
        binding.headlyaout.backBtn.setOnClickListener(v -> finish());
        binding.headlyaout.productCatName.setText(getString(R.string.addmoneytowallet));
        /**
         * Preload payment resources
         */
        Checkout.preload(getApplicationContext());
        if (getIntent().getExtras() != null) {
            binding.walletAmoutTv.setText(getIntent().getExtras().getString("wallet_amount", ""));
        }
        binding.submitAddmoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.enterAmmountEt.getText().toString().trim())) {
                    binding.enterAmmountEt.setError("Please Enter Amount");
                    binding.enterAmmountEt.requestFocus();
                } else {
                    generateChuckSum();
                }
            }
        });
    }
    public void generateChuckSum(){
        double pay = Double.parseDouble(binding.enterAmmountEt.getText().toString());
        HashMap objectNew = new HashMap();
        String orderId = UUID.randomUUID().toString();
        objectNew.put("ORDER_ID",orderId);
        objectNew.put("TXN_AMOUNT",pay+"");
        objectNew.put("CUST_ID",SharedPrefManager.getUserID(Constrants.UserId));

        Log.i("generateChuckSum_obj", objectNew + "");
        new WebTask(this, "https://seller.cartlay.com/paytm001/generateChecksum.php", objectNew, AddMoney.this, RequestCode.CODE_generateChuckSum, 5);
    }

    private void makePaytm(String orderNumber,String paytmChecksum) {
        amount_add = Double.parseDouble(binding.enterAmmountEt.getText().toString());

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getProductionService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();
//        PaytmPGService Service = PaytmPGService.getStagingService("https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderNumber);
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constrants.P_MID);//Provided by Paytm
        paramMap.put("ORDER_ID", orderNumber);//unique OrderId for every request
        paramMap.put("CUST_ID", SharedPrefManager.getUserID(Constrants.UserId));// unique customer identifier
        paramMap.put("INDUSTRY_TYPE_ID", Constrants.P_INDUSTRY_TYPE_ID);//Provided by Paytm
        paramMap.put("CHANNEL_ID", Constrants.CHANNEL_ID);//Provided by Paytm
        paramMap.put("TXN_AMOUNT", amount_add+""); // transaction amount
        paramMap.put("WEBSITE", Constrants.P_WEBSITE);////Provided by Paytm
        //   paramMap.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderNumber); //Provided by Paytm
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderNumber); //Provided by Paytm
        paramMap.put("CHECKSUMHASH", paytmChecksum+"");

        Log.i("Asdfsafas",paramMap+"");
        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);
        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        Toast.makeText(AddMoney.this, "someUIErrorOccurred", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.e("", "Payment Transaction is successful " + inResponse);
                        //  Bundle[{STATUS=PENDING, CHECKSUMHASH=Z4pqsucs67oVnSkdO47+tTi0Sex3SZ6sXEgEzlpXKdR9dxTIR78Wy57CCjcvRAZ1eeAf4Ksm7xfHCTVslzDbG5hwoT+H0DazIeH+Rl0rSXs=, BANKNAME=STATE BANK OF INDIA, ORDERID=213, TXNAMOUNT=270.00, TXNDATE=2018-12-03 10:58:11.0, MID=Ajissu50373323764671, TXNID=20181203111212800110168704300053372, RESPCODE=402, PAYMENTMODE=DC, BANKTXNID=, CURRENCY=INR, RESPMSG=Abondoned Transaction.}]

                        if (inResponse != null) {
                            Log.e("", "Payment Transaction is successful not null " + inResponse);
                            String statUs = inResponse.getString("STATUS");
                            String txnId = inResponse.getString("TXNID");
                            if (statUs.equalsIgnoreCase("TXN_SUCCESS")) {
                                addUserWalletAmount(amount_add,txnId);
                            } else {
//                                orderUnSuccess();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                        }


                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        Toast.makeText(AddMoney.this, "networkNotAvailable", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        Toast.makeText(AddMoney.this, "clientAuthenticationFailed", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        Toast.makeText(AddMoney.this, "onErrorLoadingWebPage", Toast.LENGTH_SHORT).show();

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(getApplicationContext(), "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.e("", "onTransactionCancel: " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void addUserWalletAmount(double amount, String transaction_id){
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        objectNew.put("amount",amount+"");
        objectNew.put("transaction_id", transaction_id);
        Log.i("addmoney_obj",objectNew+"");
        new WebTask(this, WebUrls.BASE_URL+WebUrls.AddUserWalletAmount,objectNew,
                AddMoney.this, RequestCode.CODE_AddUserWalletAmount,5);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_AddUserWalletAmount==taskcode){
            Log.i("addmoney_res",response+"");

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code")==1){
                    Utils.Toast(this,jsonObject.optString("message"));
                    finish();
                }else {
                    Utils.Toast(this,jsonObject.optString("message"));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if (RequestCode.CODE_generateChuckSum == taskcode) {
            Log.i("generateChuckSum_res", response);

            GenerateCheckSumResponse generateCheckSumResponse = JsonDeserializer.deserializeJson(response, GenerateCheckSumResponse.class);
            if (generateCheckSumResponse.paytSTATUS.equals("1")) {
                makePaytm(generateCheckSumResponse.oRDERID,generateCheckSumResponse.cHECKSUMHASH);
            }
//            else {
//                Utils.Toast(this, placeOrderCodModel.message);
//            }
        }
    }
}
